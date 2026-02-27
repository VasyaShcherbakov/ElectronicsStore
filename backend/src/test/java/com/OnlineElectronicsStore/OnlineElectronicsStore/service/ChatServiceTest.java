package com.OnlineElectronicsStore.OnlineElectronicsStore.service;

import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Chat;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.User;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.ChatRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    private ChatRepository chatRepository;

    @InjectMocks
    private ChatService chatService;

    // ================= getById =================

    @Test
    void shouldReturnChatWhenExists() {
        Chat chat = new Chat();
        chat.setId(1L);

        when(chatRepository.findById(1L)).thenReturn(Optional.of(chat));

        Chat result = chatService.getById(1L);

        assertEquals(1L, result.getId());
        verify(chatRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenChatNotFound() {
        when(chatRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> chatService.getById(1L));

        assertEquals("Chat not found", ex.getMessage());
    }

    // ================= getUserChats =================

    @Test
    void shouldReturnUserChats() {
        User user = new User();
        List<Chat> chats = List.of(new Chat(), new Chat());

        when(chatRepository.findByBuyerOrSeller(user, user))
                .thenReturn(chats);

        List<Chat> result = chatService.getUserChats(user);

        assertEquals(2, result.size());
        verify(chatRepository).findByBuyerOrSeller(user, user);
    }

    // ================= getChatForUser =================

    @Test
    void shouldReturnChatWhenUserIsBuyer() {
        User buyer = new User();
        Chat chat = new Chat();
        chat.setBuyer(buyer);

        when(chatRepository.findById(1L)).thenReturn(Optional.of(chat));

        Chat result = chatService.getChatForUser(1L, buyer);

        assertEquals(chat, result);
    }

    @Test
    void shouldReturnChatWhenUserIsSeller() {
        User seller = new User();
        Chat chat = new Chat();
        chat.setSeller(seller);

        when(chatRepository.findById(1L)).thenReturn(Optional.of(chat));

        Chat result = chatService.getChatForUser(1L, seller);

        assertEquals(chat, result);
    }

    @Test
    void shouldThrowExceptionWhenUserNotParticipant() {
        User buyer = new User();
        User seller = new User();
        User stranger = new User();

        Chat chat = new Chat();
        chat.setBuyer(buyer);
        chat.setSeller(seller);

        when(chatRepository.findById(1L)).thenReturn(Optional.of(chat));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> chatService.getChatForUser(1L, stranger));

        assertEquals("Access denied", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenChatNotFoundInGetChatForUser() {
        when(chatRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> chatService.getChatForUser(1L, new User()));

        assertEquals("Chat not found", ex.getMessage());
    }
}