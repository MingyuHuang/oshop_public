package com.melon.repository;

import com.melon.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<AppUser, UUID> {

    Optional<AppUser> findByAppUserName(String userName);
    Optional<AppUser> findByEmail(String email);
    Optional<AppUser> findByProviderUserId(String providerUserId);

}
