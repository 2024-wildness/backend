APP_IMAGE?=moapin-app:dev
JIB_IMAGE?=moapin-app:jib

.PHONY: help build jar image jib docker-build up logs down clean test

help:
	@echo "Targets: build, jar, image, jib, docker-build, up, logs, down, test, clean"

jar:
	./gradlew clean bootJar -x test

build: jar
	docker build -t $(APP_IMAGE) .

jib:
	./gradlew jibDockerBuild -x test

image: build
	echo "Built $(APP_IMAGE)"

docker-build: build

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
