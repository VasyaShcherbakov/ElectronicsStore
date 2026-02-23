package com.OnlineElectronicsStore.OnlineElectronicsStore.service;

import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Chat;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Message;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.User;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.ChatRepository;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void sendMessage(Chat chat, User sender, String text) {
        Message message = new Message();
        message.setChat(chat);      // важно
        message.setSender(sender);  // важно
        message.setText(text);
        message.setCreatedAt(LocalDateTime.now());
        message.setRead(false);

        messageRepository.save(message);
    }

    public List<Message> getMessages(Chat chat) {
        return messageRepository.findByChatOrderByCreatedAtAsc(chat);
    }

    public int getUnreadCount(User user) {
        return messageRepository.countByRecipientAndIsReadFalse(user);
    }

    public void markMessagesAsRead(User user) {
        List<Message> unreadMessages = messageRepository.findByRecipientAndIsReadFalse(user);

        unreadMessages.forEach(m -> m.setRead(true));

        messageRepository.saveAll(unreadMessages);
    }

   /* @Transactional
    public void markMessagesAsRead(User user) {

        List<Message> unreadMessages =
                messageRepository.findByRecipientAndIsReadFalse(user);

        unreadMessages.forEach(message -> message.setRead(true));
    }*/

}

