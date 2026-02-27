package com.OnlineElectronicsStore.OnlineElectronicsStore.controller.web;

import com.OnlineElectronicsStore.OnlineElectronicsStore.dto.OrderCreateDto;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.CustomerOrder;
import com.OnlineElectronicsStore.OnlineElectronicsStore.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


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

    @GetMapping("/order/success/{orderId}")
    public String success(@PathVariable Long orderId, Model model) {
        CustomerOrder order = orderService.getById(orderId);
        model.addAttribute("order", order);
        return "order/success";
    }

    @PostMapping("/order/checkout")
        public String checkout(@ModelAttribute OrderCreateDto orderDto,
                               RedirectAttributes redirectAttributes) {

            CustomerOrder order = orderService.createOrder(orderDto);
            redirectAttributes.addAttribute("orderId", order.getId());

            return "redirect:/order/success/{orderId}";
        }

    }

