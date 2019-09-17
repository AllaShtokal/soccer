package pl.com.tt.intern.soccer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.com.tt.intern.soccer.model.ConfirmationKey;

import java.util.Optional;

@Repository
public interface ConfirmationKeyRepository extends JpaRepository<ConfirmationKey, Long> {

    Optional<ConfirmationKey> findByUuid(String uuid);

}
