package com.madiest.moapin;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "app.jwt.secret=dGhpcy1pcy1hLXZlcnktbG9uZy1hbmQtc2VjdXJlLXNlY3JldC1rZXktZm9yLWj3dC10ZXN0aW5nCg==",
    "app.storage.endpoint=http://localhost:9000",
    "app.storage.access-key=dummy",
    "app.storage.secret-key=dummy",
    "app.storage.bucket=test",
    "app.search.host=http://localhost:7700",
    "app.search.api-key=dummy",
    "app.email.access-key=dummy",
    "app.email.secret-key=dummy",
    "app.email.region=us-east-1",
    "spring.test.aot.enabled=false"
})

class MoapinApplicationTests {

    @MockBean
    private S3Presigner s3Presigner;

    @Test
    void contextLoads() {
    }

}