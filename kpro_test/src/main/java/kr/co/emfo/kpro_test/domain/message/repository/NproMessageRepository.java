package kr.co.emfo.kpro_test.domain.message.repository;

import kr.co.emfo.kpro_test.domain.message.entity.NproMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface NproMessageRepository extends JpaRepository<NproMessage, Long> {

    Optional<List<NproMessage>> findTop1000ByState(String state);
    Optional<List<NproMessage>> findAllByState(String state);
}
