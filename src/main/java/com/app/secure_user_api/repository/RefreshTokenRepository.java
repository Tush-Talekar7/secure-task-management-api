package com.app.secure_user_api.repository;

import com.app.secure_user_api.entity.RefreshToken;
import com.app.secure_user_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository
        extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByUser_Id(Long userId);

    Optional<RefreshToken> findByUser(User user);
}
