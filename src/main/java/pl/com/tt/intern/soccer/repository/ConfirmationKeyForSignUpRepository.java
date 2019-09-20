package pl.com.tt.intern.soccer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.model.ConfirmationKeyForSignUp;

import java.util.Optional;

@Repository
public interface ConfirmationKeyForSignUpRepository extends JpaRepository<ConfirmationKeyForSignUp, Long> {

    Optional<ConfirmationKeyForSignUp> findByUuid(String uuid) throws NotFoundException;

    @Transactional
    @Modifying
    @Query(value = "delete from confirmation_key where expiration_time < NOW() ", nativeQuery = true)
    void deleteByExpirationTime();
}
