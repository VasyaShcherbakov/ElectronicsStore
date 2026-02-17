package com.OnlineElectronicsStore.OnlineElectronicsStore.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "DTO сообщения")
public class MessageDto {
    private Long id;
    private String text;
    private UserSummaryDto sender;
    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public UserSummaryDto getSender() { return sender; }
    public void setSender(UserSummaryDto sender) { this.sender = sender; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

