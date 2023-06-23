package com.madreTierra.repository;

import com.madreTierra.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository <UserEntity, Long> {

    UserEntity findByEmail(String email);
}
