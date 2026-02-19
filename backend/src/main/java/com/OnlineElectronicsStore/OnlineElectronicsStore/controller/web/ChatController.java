package com.OnlineElectronicsStore.OnlineElectronicsStore.controller.web;


import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Chat;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Product;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.User;
import com.OnlineElectronicsStore.OnlineElectronicsStore.service.ChatService;
import com.OnlineElectronicsStore.OnlineElectronicsStore.service.UserService;
import com.OnlineElectronicsStore.OnlineElectronicsStore.service.MessageService;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.ChatRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/chats")
public class ChatController {
    private final ChatService chatService;
    private final UserService userService;
    private final MessageService messageService;


    public ChatController(ChatService chatService, UserService userService, MessageService messageService) {
        this.chatService = chatService;
        this.userService = userService;
        this.messageService = messageService;
    }

    private static final Logger log = LoggerFactory.getLogger(ChatController.class);


    @GetMapping("/{chatId:\\d+}")
    public String openChat(@PathVariable Long chatId, Model model) {
        User currentUser = userService.getCurrentUser();

        // 🔒 Используем проверку доступа
        Chat chat = chatService.getChatForUser(chatId, currentUser);

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("messages", messageService.getMessages(chat));
        model.addAttribute("chat", chat);

        return "chats/chat";
    }

    @PostMapping("/{chatId}/send")

    public String sendMessage(
            @PathVariable Long chatId,
            @RequestParam String text) {
        log.info("Користувач відкриває чат");
        User currentUser = userService.getCurrentUser();
        Chat chat = chatService.getById(chatId);

        messageService.sendMessage(chat, currentUser, text);

        return "redirect:/chats/" + chatId;
    }



    @GetMapping
    public String myChats(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.getCurrentUser();

        List<Chat> chats = chatService.getUserChats(user);

        model.addAttribute("currentUser", userService.getCurrentUser());
        model.addAttribute("chats", chats);
        model.addAttribute("user", user);

        return "chats/list";
    }

}
