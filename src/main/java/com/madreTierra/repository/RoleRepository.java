package com.madreTierra.repository;

import com.madreTierra.entity.RoleEntity;
import com.madreTierra.enumeration.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity,Long> {
    RoleEntity findByRoleName(RoleName roleName);
}

