package com.OnlineElectronicsStore.OnlineElectronicsStore.controller.rest;

import com.OnlineElectronicsStore.OnlineElectronicsStore.dto.ChatDto;
import com.OnlineElectronicsStore.OnlineElectronicsStore.dto.MessageDto;
import com.OnlineElectronicsStore.OnlineElectronicsStore.mapper.ChatMapper;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Chat;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Message;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.User;
import com.OnlineElectronicsStore.OnlineElectronicsStore.security.JwtAuthFilter;
import com.OnlineElectronicsStore.OnlineElectronicsStore.service.ChatService;
import com.OnlineElectronicsStore.OnlineElectronicsStore.service.MessageService;
import com.OnlineElectronicsStore.OnlineElectronicsStore.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChatRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class ChatRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ChatService chatService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private MessageService messageService;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;


    private User testUser;
    private Chat testChat;
    private Message testMessage;


    @BeforeEach
    void setup() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUser");

        testChat = new Chat();
        testChat.setId(1L);
        testChat.setBuyer(testUser);
        testChat.setSeller(testUser);

        testMessage = new Message();
        testMessage.setId(1L);
        testMessage.setText("Hello");
        testMessage.setSender(testUser);

        when(userService.getCurrentUser()).thenReturn(testUser);
        when(chatService.getUserChats(testUser)).thenReturn(List.of(testChat));
        when(chatService.getChatForUser(anyLong(), any(User.class))).thenReturn(testChat);
        when(messageService.getMessages(testChat)).thenReturn(List.of(testMessage));
    }

    @Test
    void getMyChats() throws Exception {
        mockMvc.perform(get("/api/chats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testChat.getId()));
    }

    @Test
    void getChat() throws Exception {
        mockMvc.perform(get("/api/chats/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testChat.getId()))
                .andExpect(jsonPath("$.messages[0].text").value("Hello"));
    }
}