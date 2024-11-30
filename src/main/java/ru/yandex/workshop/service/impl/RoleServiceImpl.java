package ru.yandex.workshop.service.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.workshop.model.OrganizerRole;
import ru.yandex.workshop.service.RoleService;
import ru.yandex.workshop.storage.RoleStorage;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleStorage roleStorage;

    @Autowired
    public RoleServiceImpl(RoleStorage roleStorage) {
        this.roleStorage = roleStorage;
    }

    public List<OrganizerRole> findAll() {
        return roleStorage.findAll();
    }

    public OrganizerRole getById(Long id) {
        return roleStorage.getById(id);
    }
}