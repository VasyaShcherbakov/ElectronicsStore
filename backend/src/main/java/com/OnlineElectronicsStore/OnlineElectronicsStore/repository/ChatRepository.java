package com.OnlineElectronicsStore.OnlineElectronicsStore.repository;

import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Chat;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    List<Chat> findByBuyerOrSeller(User buyer, User seller);

}
