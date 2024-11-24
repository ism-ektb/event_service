package ru.yandex.workshop.service;

import ru.yandex.workshop.model.OrganizerRole;

import java.util.List;

public interface RoleService {
    List<OrganizerRole> findAll();
}
