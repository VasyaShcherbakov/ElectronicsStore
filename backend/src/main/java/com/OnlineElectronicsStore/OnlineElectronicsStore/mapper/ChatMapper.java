package com.OnlineElectronicsStore.OnlineElectronicsStore.mapper;

import com.OnlineElectronicsStore.OnlineElectronicsStore.dto.ChatDto;
import com.OnlineElectronicsStore.OnlineElectronicsStore.dto.MessageDto;
import com.OnlineElectronicsStore.OnlineElectronicsStore.dto.UserSummaryDto;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Chat;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Message;

import java.util.stream.Collectors;

public class ChatMapper {

    public static ChatDto toDto(Chat chat, java.util.List<Message> messages) {
        if (chat == null) return null;

        ChatDto dto = new ChatDto();
        dto.setId(chat.getId());
        dto.setOrderId(chat.getCustomerOrder() != null ? chat.getCustomerOrder().getId() : null);
        dto.setSeller(new UserSummaryDto(chat.getSeller().getId(), chat.getSeller().getUsername()));
        dto.setBuyer(new UserSummaryDto(chat.getBuyer().getId(), chat.getBuyer().getUsername()));
        dto.setMessages(messages.stream().map(ChatMapper::toMessageDto).collect(Collectors.toList()));
        return dto;
    }

    public static MessageDto toMessageDto(Message message) {
        if (message == null) return null;

        MessageDto dto = new MessageDto();
        dto.setId(message.getId());
        dto.setText(message.getText());
        dto.setSender(new UserSummaryDto(message.getSender().getId(), message.getSender().getUsername()));
        dto.setCreatedAt(message.getCreatedAt());
        return dto;
    }
}