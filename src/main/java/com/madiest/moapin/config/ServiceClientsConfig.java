package com.madiest.moapin.config;

import com.meilisearch.sdk.Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.sesv2.SesAsyncClient;

import java.net.URI;

/**
 * Configuration to create clients for external services.
 */
@Configuration
public class ServiceClientsConfig {

    @Bean
    public S3Client s3Client(AppProperties props) {
        AwsBasicCredentials creds = AwsBasicCredentials.create(
            props.getStorage().getAccessKey(),
            props.getStorage().getSecretKey()
        );
        return S3Client.builder()
            .endpointOverride(URI.create(props.getStorage().getEndpoint()))
            .credentialsProvider(StaticCredentialsProvider.create(creds))
            .serviceConfiguration(S3Configuration.builder().pathStyleAccessEnabled(true).build())
            .region(Region.of(props.getEmail().getRegion()))
            .build();
    }

    @Bean
    public Client meiliSearchClient(AppProperties props) {
        return new Client(
            props.getSearch().getHost(),
            props.getSearch().getApiKey()
        );
    }

    @Bean
    public SesAsyncClient sesAsyncClient(AppProperties props) {
        AwsBasicCredentials creds = AwsBasicCredentials.create(
            props.getEmail().getAccessKey(),
            props.getEmail().getSecretKey()
        );
        return SesAsyncClient.builder()
            .credentialsProvider(StaticCredentialsProvider.create(creds))
            .region(Region.of(props.getEmail().getRegion()))
            .build();
    }
}