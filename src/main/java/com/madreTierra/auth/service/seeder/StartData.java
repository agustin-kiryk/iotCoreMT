package com.madreTierra.auth.service.seeder;

import com.madreTierra.entity.RoleEntity;
import com.madreTierra.enumeration.RoleName;
import com.madreTierra.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.websocket.server.ServerEndpoint;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StartData {

    private final RoleRepository roleRepository;

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        List<RoleEntity> roles = roleRepository.findAll();

        if (roles.isEmpty()) {
            this.createRoles();

        }

    }

    private void createRoles() {
        this.createRol(1L, RoleName.ADMIN);
        this.createRol(2L, RoleName.USER);
    }

    private void createRol(long l, RoleName name) {
        RoleEntity role = new RoleEntity();
        role.setId(l);
        role.setRoleName(name.getName());
        roleRepository.save(role);
    }


}
