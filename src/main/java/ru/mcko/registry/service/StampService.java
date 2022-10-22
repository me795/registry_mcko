package ru.mcko.registry.service;

import ru.mcko.registry.entity.Stamp;

import java.util.List;

public interface StampService {

    List<Stamp> listByOrganization(Integer organizationId);
    List<Stamp> listActiveByOrganization(Integer organizationId);

    Stamp getById(Integer stampId);

    Integer create(Stamp stamp);

    void update(Stamp stamp);

    void delete(Integer stampId);

    void deleteAllByOrganization(Integer organizationId);
}
