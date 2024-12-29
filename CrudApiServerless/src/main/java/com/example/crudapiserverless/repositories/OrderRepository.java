package com.example.crudapiserverless.repositories;

import com.example.crudapiserverless.entities.Order;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class OrderRepository {

    private final DynamoDbTable<Order> orderTable;

    public OrderRepository(DynamoDbTable<Order> orderTable) {
        this.orderTable = orderTable;
    }

    public void saveOrder(Order order) {
        orderTable.putItem(order);
    }

    public Order getOrder(String id) {
        return orderTable.getItem(r -> r.key(k -> k.partitionValue(id)));
    }

    public void deleteOrder(String id) {
        orderTable.deleteItem(r -> r.key(k -> k.partitionValue(id)));
    }

    public List<Order> listOrders() {
        return orderTable.scan().items().stream().collect(Collectors.toList());
    }
}
