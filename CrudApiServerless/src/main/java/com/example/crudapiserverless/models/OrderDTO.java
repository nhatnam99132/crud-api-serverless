package com.example.crudapiserverless.models;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderDTO {
    private String id; // Change to String for UUID
    private String status;
    private String description;
    private LocalDateTime timestamp;
}
