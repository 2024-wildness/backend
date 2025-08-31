APP_IMAGE?=moapin-app:dev
JIB_IMAGE?=moapin-app:jib
REGISTRY?=registry.yukey.site
IMAGE_NAME?=moapin-backend
TAG?=$(shell git rev-parse --short=12 HEAD 2>/dev/null || echo dev)

.PHONY: help build jar image jib docker-build up logs down clean test run smoke infra-up infra-down wait-db health compose-only registry-build registry-push registry-all deploy-hint
.PHONY: container-smoke

help:
	@echo "Targets: build, jar, image, jib, docker-build, up, logs, down, test, clean"
	@echo "Extra: run (jar direct), smoke (health check), infra-up (deps only), infra-down"

jar:
	./gradlew clean bootJar -x test

build: jar
	docker build -t $(APP_IMAGE) .

jib:
	./gradlew jibDockerBuild -x test

image: build
	echo "Built $(APP_IMAGE)"

docker-build: build

# ===== Registry build/push (external build pattern) =====
registry-build: jar ## Build jar + runtime image tagged with git SHA and latest (if main)
	docker build -t $(REGISTRY)/$(IMAGE_NAME):$(TAG) .
	@if [ "$(BRANCH)" = "main" ] || git branch --show-current | grep -q '^main$$'; then \
	  docker tag $(REGISTRY)/$(IMAGE_NAME):$(TAG) $(REGISTRY)/$(IMAGE_NAME):latest; \
	fi
	@echo "Image(s) built: $(REGISTRY)/$(IMAGE_NAME):$(TAG)"

registry-push: ## Push image(s) to registry (requires docker login)
	@echo "Pushing $(REGISTRY)/$(IMAGE_NAME):$(TAG)"; \
	docker push $(REGISTRY)/$(IMAGE_NAME):$(TAG); \
	if docker image inspect $(REGISTRY)/$(IMAGE_NAME):latest >/dev/null 2>&1; then \
	  echo "Pushing latest tag"; docker push $(REGISTRY)/$(IMAGE_NAME):latest; \
	fi

registry-all: registry-build registry-push ## Build & push (convenience)

deploy-hint: ## Show compose deploy instructions for this tag
	@echo "To deploy:"; \
	echo "export APP_IMAGE_TAG=$(TAG)"; \
	echo "docker compose pull app && docker compose up -d app"

container-smoke: jar ## Build image and run container with dev profile + health check (no infra deps)
	docker build -t moapin-smoke:dev .
	CID=$$(docker run -d -e SPRING_PROFILES_ACTIVE=dev -p 18080:8080 moapin-smoke:dev); \
	echo "[container-smoke] container=$$CID waiting for health"; \
	for i in $$(seq 1 40); do \
	  if curl -fsS http://localhost:18080/actuator/health >/dev/null 2>&1; then echo "[container-smoke] OK"; break; fi; \
	  sleep 2; \
	done; \
	if ! curl -fsS http://localhost:18080/actuator/health >/dev/null 2>&1; then echo "[container-smoke] FAILED -> logs:"; docker logs $$CID | tail -n 60; docker rm -f $$CID >/dev/null; exit 1; fi; \
	echo "[container-smoke] stopping"; docker rm -f $$CID >/dev/null

up:
	docker compose up -d

logs:
	docker compose logs -f app

down:
	docker compose down -v

clean:
	./gradlew clean
	rm -f build/jib-image.tar || true

dev:
	SPRING_PROFILES_ACTIVE=dev ./gradlew bootRun

test:
	./gradlew test

# ===== Local convenience targets =====
run: ## Run the built JAR with dev profile (build if missing)
	@if [ -z "$(shell ls build/libs/*SNAPSHOT.jar 2>/dev/null)" ]; then \
		echo "[run] jar not found → building"; ./gradlew bootJar -x test; \
	fi; \
	JAR=$$(ls build/libs/*SNAPSHOT.jar | head -n1); \
	echo "[run] starting $$JAR (dev profile)"; \
	SPRING_PROFILES_ACTIVE=dev java -jar $$JAR

infra-up: ## Start only infra services (no app) via docker compose
	docker compose up -d postgres redis rabbitmq

infra-down: ## Stop infra services
	docker compose rm -sfv postgres redis rabbitmq || true

compose-only: ## Build app image then start only app container (assuming infra already up)
	$(MAKE) build
	docker compose up -d app

health: ## Check actuator health endpoint
	@curl -fsS http://localhost:8080/actuator/health || (echo "Health check failed" && exit 1)

smoke: ## Build + run (background) + health + shutdown
	$(MAKE) jar
	JAR=$$(ls build/libs/*SNAPSHOT.jar | head -n1); \
	SPRING_PROFILES_ACTIVE=dev java -jar $$JAR & echo $$! > .app.pid; \
	echo "[smoke] PID=$$(cat .app.pid) waiting for health"; \
	for i in $$(seq 1 40); do \
		if curl -fsS http://localhost:8080/actuator/health >/dev/null 2>&1; then echo "[smoke] OK"; break; fi; \
		sleep 2; \
	done; \
	if ! curl -fsS http://localhost:8080/actuator/health >/dev/null 2>&1; then echo "[smoke] FAILED"; kill $$(cat .app.pid) || true; exit 1; fi; \
	echo "[smoke] shutting down"; kill $$(cat .app.pid) || true; rm -f .app.pid
