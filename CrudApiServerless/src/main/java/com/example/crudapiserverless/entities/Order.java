package com.example.crudapiserverless.entities;

import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.time.LocalDateTime;

@Data
@DynamoDbBean
public class Order {

    private String id;
    private String status;
    private String description;
    private LocalDateTime timestamp;

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }

    @DynamoDbSortKey
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
