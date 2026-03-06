package edu.cit.chan.restoradar.repository;

import edu.cit.chan.restoradar.entity.RefreshTokenEntity;
import edu.cit.chan.restoradar.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, UUID> {
    Optional<RefreshTokenEntity> findByToken(String token);
    void deleteByUser(UserEntity user); 
}