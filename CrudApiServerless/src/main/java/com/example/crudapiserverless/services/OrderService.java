package com.example.crudapiserverless.services;

import com.example.crudapiserverless.entities.Order;
import com.example.crudapiserverless.repositories.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order saveOrder(Order order) {
        if (order.getId() == null || order.getId().isEmpty()) {
            order.setId(UUID.randomUUID().toString());
        }
        orderRepository.saveOrder(order);
        return order;
    }

    public Order getOrder(String id) {
        return orderRepository.getOrder(id);
    }

    public void deleteOrder(String id) {
        orderRepository.deleteOrder(id);
    }

    public List<Order> listOrders() {
        return orderRepository.listOrders();
    }
}
