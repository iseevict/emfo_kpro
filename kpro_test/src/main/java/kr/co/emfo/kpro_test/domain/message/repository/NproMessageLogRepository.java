package kr.co.emfo.kpro_test.domain.message.repository;

import kr.co.emfo.kpro_test.domain.message.entity.NproMessageLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NproMessageLogRepository extends JpaRepository<NproMessageLog, Long> {
}
