package kr.co.emfo.kpro_test.domain.message.repository;

import kr.co.emfo.kpro_test.domain.message.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {

    Optional<List<Message>> findTop1000ByCurState(Character curState);
    Optional<List<Message>> findAllByCurState(Character curState);
}
