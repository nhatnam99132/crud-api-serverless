package com.example.crudapiserverless.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.net.URI;
import com.example.crudapiserverless.entities.Order;

@Configuration
public class DynamoDBConfiguration {

    @Value("${aws.dynamodb.endpoint}")
    private String dynamoDbEndpoint;

    @Value("${aws.dynamodb.region}")
    private String region;

    @Value("${aws.dynamodb.access-key}")
    private String accessKey;

    @Value("${aws.dynamodb.secret-key}")
    private String secretKey;

    @Value("${aws.dynamodb.use-local-credentials}")
    private boolean useLocalCredentials;

    @Value("${aws.dynamodb.table-name}")
    private String tableName;

    @Bean
    public DynamoDbClient dynamoDbClient() {
        if (useLocalCredentials) {
            return DynamoDbClient.builder()
                    .endpointOverride(URI.create(dynamoDbEndpoint))
                    .region(Region.of(region))
                    .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                    .build();
        } else {
            return DynamoDbClient.builder()
                    .endpointOverride(URI.create(dynamoDbEndpoint))
                    .region(Region.of(region))
                    .credentialsProvider(DefaultCredentialsProvider.create())
                    .build();
        }
    }
    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }
    @Bean
    public DynamoDbTable<Order> orderTable(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        return dynamoDbEnhancedClient.table(tableName, TableSchema.fromBean(Order.class));
    }
}

