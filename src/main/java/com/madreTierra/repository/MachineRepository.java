package com.madreTierra.repository;

import com.madreTierra.entity.MachinEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MachineRepository extends JpaRepository<MachinEntity,Long> {
    MachinEntity findByMachineId(String machineId);

}
