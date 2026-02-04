package com.OnlineElectronicsStore.OnlineElectronicsStore.service;

import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Chat;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Message;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.User;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.ChatRepository;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.MessageRepository;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;

    public MessageService(MessageRepository messageRepository, ChatRepository chatRepository) {
        this.messageRepository = messageRepository;
        this.chatRepository = chatRepository;
    }


    public void send(Long chatId, User sender, String text) {
        Chat chat = chatRepository.findById(chatId).orElseThrow();

        Message message = new Message();
        message.setChat(chat);
        message.setSender(sender);
        message.setText(text);

        messageRepository.save(message);
    }
}

