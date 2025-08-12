package com.madiest.moapin.common.config;

import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.sesv2.SesV2AsyncClient;

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

    /**
     * MeiliSearch 클라이언트 인스턴스를 생성하여 반환합니다.
     *
     * 검색 서비스의 호스트와 API 키를 `AppProperties`에서 읽어와 MeiliSearch 클라이언트를 구성합니다.
     *
     * @return MeiliSearch 서비스와 통신할 수 있는 클라이언트 인스턴스
     */
    @Bean
    public Client meiliSearchClient(AppProperties props) {
        return new Client(new Config(
            props.getSearch().getHost(),
            props.getSearch().getApiKey()
        ));
    }

    /**
     * 애플리케이션의 이메일 설정 정보를 기반으로 AWS SES V2 비동기 클라이언트를 생성하여 반환합니다.
     *
     * @return 구성된 SesV2AsyncClient 인스턴스
     */
    @Bean
    public SesV2AsyncClient sesAsyncClient(AppProperties props) {
        AwsBasicCredentials creds = AwsBasicCredentials.create(
            props.getEmail().getAccessKey(),
            props.getEmail().getSecretKey()
        );
        return SesV2AsyncClient.builder()
            .credentialsProvider(StaticCredentialsProvider.create(creds))
            .region(Region.of(props.getEmail().getRegion()))
            .build();
    }

    /**
     * AWS S3 Presigner 인스턴스를 생성하여 반환합니다.
     *
     * S3 Presigner는 지정된 AppProperties의 스토리지 접근 키, 시크릿 키, 엔드포인트, 그리고 이메일 리전 정보를 사용하여 구성됩니다.
     *
     * @return 구성된 S3Presigner 인스턴스
     */
    @Bean
    public S3Presigner s3Presigner(AppProperties props) {
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(
            props.getStorage().getAccessKey(),
            props.getStorage().getSecretKey()
        );
        return S3Presigner.builder()
            .endpointOverride(URI.create(props.getStorage().getEndpoint()))
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
            .region(Region.of(props.getEmail().getRegion()))
            .build();
    }
}