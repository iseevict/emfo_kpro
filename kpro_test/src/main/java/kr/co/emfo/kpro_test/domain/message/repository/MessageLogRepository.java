package kr.co.emfo.kpro_test.domain.message.repository;

import kr.co.emfo.kpro_test.domain.message.entity.MessageLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageLogRepository extends JpaRepository<MessageLog, Long> {
}
