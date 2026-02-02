package com.OnlineElectronicsStore.OnlineElectronicsStore.service;


import com.OnlineElectronicsStore.OnlineElectronicsStore.model.CustomerOrder;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void save(CustomerOrder customerOrder) {
        orderRepository.save(customerOrder);
    }
}
