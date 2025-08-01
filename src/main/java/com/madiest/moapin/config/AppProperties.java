package com.madiest.moapin.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * Binds external configuration properties from environment variables.
 */
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private DataSource dataSource = new DataSource();
    private Storage storage = new Storage();
    private Search search = new Search();
    private Email email = new Email();
    private Jwt jwt = new Jwt();

    @Setter
    @Getter
    public static class DataSource {
        private String url;
        private String username;
        private String password;

    }

    @Setter
    @Getter
    public static class Storage {
        private String endpoint;
        private String accessKey;
        private String secretKey;
        private String bucket;

    }

    @Setter
    @Getter
    public static class Search {
        private String host;
        private String apiKey;

    }

    @Setter
    @Getter
    public static class Email {
        private String accessKey;
        private String secretKey;
        private String region;

    }

    @Setter
    @Getter
    public static class Jwt {
        private String secret;
        private Duration expiration;

    }
}