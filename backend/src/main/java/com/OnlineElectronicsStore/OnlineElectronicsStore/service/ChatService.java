package com.OnlineElectronicsStore.OnlineElectronicsStore.service;

import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Chat;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.CustomerOrder;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.User;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.ChatRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final ChatRepository chatRepository;

    public User get() {
        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        if (principal instanceof User user) {
            return user;
        }

        throw new RuntimeException("Пользователь не аутентифицирован");
    }
    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public Chat createChatForOrder(CustomerOrder order) {

        User seller = order.getItems()
                .get(0)
                .getProduct()
                .getOwner();

        Chat chat = new Chat();
        chat.setCustomerOrder(order);
        chat.setBuyer(order.getBuyer());
        chat.setSeller(seller);

        Chat savedChat = chatRepository.save(chat);
        order.setChat(savedChat);

        return savedChat;
    }

    public Chat getById(Long chatId) {
        return chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat not found"));
    }

}

