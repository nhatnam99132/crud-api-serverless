package com.example.crudapiserverless.controllers;

import com.example.crudapiserverless.entities.Order;
import com.example.crudapiserverless.models.OrderDTO;
import com.example.crudapiserverless.models.ApiResponse;
import com.example.crudapiserverless.models.ErrorResponse;
import com.example.crudapiserverless.services.OrderService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;
    private final ModelMapper modelMapper;

    public OrderController(OrderService orderService, ModelMapper modelMapper) {
        this.orderService = orderService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OrderDTO>> createOrder(@Validated @RequestBody OrderDTO orderDTO) {
        orderDTO.setTimestamp(LocalDateTime.now());
        logger.info("Creating new order: {}", orderDTO);
        Order order = modelMapper.map(orderDTO, Order.class);
        orderService.saveOrder(order);
        Order createdOrder = orderService.getOrder(order.getId());

        // Trigger camel route (assuming producerTemplate is configured and available)
        // producerTemplate.sendBody("direct:logOrderEvent", createdOrder);

        OrderDTO createdOrderDTO = modelMapper.map(createdOrder, OrderDTO.class);
        logger.info("Order with ID: {}", createdOrderDTO.getId());

        ApiResponse<OrderDTO> response = new ApiResponse<>(createdOrderDTO, null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderDTO>> getOrder(@PathVariable String id) {
        Order order = orderService.getOrder(id);
        if (order == null) {
            ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), "Order Not Found", "Order with ID " + id + " not found", "/orders/" + id);
            ApiResponse<OrderDTO> response = new ApiResponse<>(null, errorResponse);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);
        ApiResponse<OrderDTO> response = new ApiResponse<>(orderDTO, null);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteOrder(@PathVariable String id) {
        Order order = orderService.getOrder(id);
        if (order == null) {
            ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), "Order Not Found", "Order with ID " + id + " not found", "/orders/" + id);
            ApiResponse<Void> response = new ApiResponse<>(null, errorResponse);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        orderService.deleteOrder(id);
        ApiResponse<Void> response = new ApiResponse<>(null, null);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderDTO>>> listOrders() {
        List<Order> orders = orderService.listOrders();
        List<OrderDTO> orderDTOs = orders.stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
        ApiResponse<List<OrderDTO>> response = new ApiResponse<>(orderDTOs, null);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderDTO>> updateOrder(@PathVariable String id, @Validated @RequestBody OrderDTO orderDTO) {
        Order existingOrder = orderService.getOrder(id);
        if (existingOrder == null) {
            ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), "Order Not Found", "Order with ID " + id + " not found", "/orders/" + id);
            ApiResponse<OrderDTO> response = new ApiResponse<>(null, errorResponse);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        orderDTO.setId(id);
        orderDTO.setTimestamp(LocalDateTime.now());
        Order updatedOrder = modelMapper.map(orderDTO, Order.class);
        orderService.saveOrder(updatedOrder);
        OrderDTO updatedOrderDTO = modelMapper.map(updatedOrder, OrderDTO.class);
        ApiResponse<OrderDTO> response = new ApiResponse<>(updatedOrderDTO, null);
        return ResponseEntity.ok(response);
    }
}
