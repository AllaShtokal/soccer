package pl.com.tt.intern.soccer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.com.tt.intern.soccer.model.ConfirmationKey;

import java.util.Optional;

@Repository
public interface ConfirmationKeyRepository extends JpaRepository<ConfirmationKey, Long> {

    Optional<ConfirmationKey> findByUuid(String uuid);

    @Transactional
    @Modifying
    @Query(value = "delete from confirmation_key where expiration_time < NOW() ", nativeQuery = true)
    void deleteByExpirationTime();
}
