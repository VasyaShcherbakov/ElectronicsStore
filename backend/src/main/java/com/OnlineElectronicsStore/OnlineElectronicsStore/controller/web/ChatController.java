package com.OnlineElectronicsStore.OnlineElectronicsStore.controller.web;


import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Chat;
import com.OnlineElectronicsStore.OnlineElectronicsStore.service.ChatService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chats")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/{chatId:\\d+}")
    public String openChat(@PathVariable Long chatId, Model model) {

        Chat chat = chatService.getById(chatId);

        model.addAttribute("chat", chat);
        model.addAttribute("messages", chat.getMessages());

        return "chat/chat";
    }


}
