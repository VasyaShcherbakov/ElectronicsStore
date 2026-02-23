package com.OnlineElectronicsStore.OnlineElectronicsStore.repository;

import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Chat;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Message;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByChatOrderByCreatedAtAsc(Chat chat);

    int countByRecipientAndIsReadFalse(User recipient);

    List<Message> findByRecipientAndIsReadFalse(User recipient);

}



