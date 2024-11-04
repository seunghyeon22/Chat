package com.chat.chat.domain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.chat.chat.domain.repository.entity.Chat;
import com.chat.chat.domain.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat,Long>{
    List<Chat> findTop10BySenderOrReceiverOrderByTIDDesc(String sender, String receiver);

//    @Query("select c from chat as c where c.sender =:sender OR c.receiver = :receiver order by c.t_id desc limit  10")
//    List<Chat> findTop10Chrts(@Param("sender") String sender, @Param("receiver") String receiver);

}