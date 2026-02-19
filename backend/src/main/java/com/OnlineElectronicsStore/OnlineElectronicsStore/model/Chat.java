package com.OnlineElectronicsStore.OnlineElectronicsStore.model;


import jakarta.persistence.*;
import java.util.List;

@Entity
public class Chat {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User buyer;

    @ManyToOne
    private User seller;

    @OneToMany(mappedBy = "chat")
    private List<Message> messages;

    @OneToOne
    @JoinColumn(name = "order_id")
    private CustomerOrder customerOrder;


    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Chat() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CustomerOrder getCustomerOrder() {
        return customerOrder;
    }

    public void setCustomerOrder(CustomerOrder customerOrder) {
        this.customerOrder = customerOrder;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }
}
