package com.OnlineElectronicsStore.OnlineElectronicsStore.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "DTO чата")
public class ChatDto {
    private Long id;

    @Schema(description = "ID заказа, связанного с чатом")
    private Long orderId;

    @Schema(description = "Продавец")
    private UserSummaryDto seller;

    @Schema(description = "Покупатель")
    private UserSummaryDto buyer;

    @Schema(description = "Сообщения в чате")
    private List<MessageDto> messages;

    // ====== Геттеры и сеттеры ======

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public UserSummaryDto getSeller() { return seller; }
    public void setSeller(UserSummaryDto seller) { this.seller = seller; }

    public UserSummaryDto getBuyer() { return buyer; }
    public void setBuyer(UserSummaryDto buyer) { this.buyer = buyer; }

    public List<MessageDto> getMessages() { return messages; }
    public void setMessages(List<MessageDto> messages) { this.messages = messages; }
}




