package com.OnlineElectronicsStore.OnlineElectronicsStore.controller.rest;

import com.OnlineElectronicsStore.OnlineElectronicsStore.dto.ChatDto;
import com.OnlineElectronicsStore.OnlineElectronicsStore.dto.MessageDto;
import com.OnlineElectronicsStore.OnlineElectronicsStore.mapper.ChatMapper;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Chat;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Message;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.User;
import com.OnlineElectronicsStore.OnlineElectronicsStore.service.ChatService;
import com.OnlineElectronicsStore.OnlineElectronicsStore.service.MessageService;
import com.OnlineElectronicsStore.OnlineElectronicsStore.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Chats", description = "Операции с чатами")
@RestController
@RequestMapping("/api/chats")
public class ChatRestController {

    private final ChatService chatService;
    private final UserService userService;
    private final MessageService messageService;

    public ChatRestController(ChatService chatService, UserService userService, MessageService messageService) {
        this.chatService = chatService;
        this.userService = userService;
        this.messageService = messageService;
    }

    // ================= Получить все чаты текущего пользователя =================
    @GetMapping
    public ResponseEntity<List<ChatDto>> getMyChats() {
        User currentUser = userService.getCurrentUser();
        List<Chat> chats = chatService.getUserChats(currentUser);

        List<ChatDto> dtos = chats.stream()
                .map(chat -> ChatMapper.toDto(chat, messageService.getMessages(chat)))
                .toList();

        return ResponseEntity.ok(dtos);
    }

    // ================= Получить конкретный чат =================
    @GetMapping("/{chatId}")
    public ResponseEntity<ChatDto> getChat(
            @PathVariable Long chatId
    ) {
        User currentUser = userService.getCurrentUser();

        Chat chat = chatService.getChatForUser(chatId, currentUser);
        List<Message> messages = messageService.getMessages(chat);

        return ResponseEntity.ok(ChatMapper.toDto(chat, messages));
    }

    // ================= Отправить сообщение =================
    @PostMapping("/{chatId}/send")
    public ResponseEntity<MessageDto> sendMessage(
            @PathVariable Long chatId,
            @RequestParam String text
    ) {
        User currentUser = userService.getCurrentUser();

        Chat chat = chatService.getChatForUser(chatId, currentUser);
        messageService.sendMessage(chat, currentUser, text);

        List<Message> messages = messageService.getMessages(chat);

        Message lastMessage = messages.get(messages.size() - 1);
        MessageDto dto = ChatMapper.toMessageDto(lastMessage);

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);

    }
}