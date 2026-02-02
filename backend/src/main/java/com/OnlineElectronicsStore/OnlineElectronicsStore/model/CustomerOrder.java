package com.OnlineElectronicsStore.OnlineElectronicsStore.model;


import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class CustomerOrder {

    @Id
    @GeneratedValue
    private Long id;

    // ДАННЫЕ НА МОМЕНТ ЗАКАЗА
    private String customerName;
    private String phone;
    private String address;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public CustomerOrder() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

}

