package com.OnlineElectronicsStore.OnlineElectronicsStore.controller.web;
import com.OnlineElectronicsStore.OnlineElectronicsStore.dto.OrderCreateDto;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.CustomerOrder;
import com.OnlineElectronicsStore.OnlineElectronicsStore.service.OrderService;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.OrderStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


    @Controller
    public class OrderController {

        private final OrderService orderService;

        public OrderController(OrderService orderService) {
            this.orderService = orderService;
        }

        @GetMapping("/order/checkout")
        public String showCheckoutForm(Model model) {
            model.addAttribute("orderDto", new OrderCreateDto());
            return "checkout"; // имя Thymeleaf шаблона checkout.html
        }

        @PostMapping("/order/checkout")
        public String checkout(@ModelAttribute OrderCreateDto orderDto) {

            CustomerOrder customerOrder = new CustomerOrder();
            customerOrder.setCustomerName(orderDto.getCustomerName());
            customerOrder.setPhone(orderDto.getPhone());
            customerOrder.setAddress(orderDto.getAddress());
            customerOrder.setStatus(OrderStatus.PAID);

            orderService.save(customerOrder);

            return "redirect:/order/success";
        }
    }

