package ru.mcko.registry.service;

import org.springframework.stereotype.Service;
import ru.mcko.registry.entity.Stamp;
import ru.mcko.registry.repository.read.StampRepositoryRead;
import ru.mcko.registry.repository.write.StampRepositoryWrite;

import java.util.List;

@Service
public class StampServiceImpl implements StampService {

    private final StampRepositoryWrite stampRepositoryWrite;
    private final StampRepositoryRead stampRepositoryRead;

    public StampServiceImpl(StampRepositoryWrite stampRepositoryWrite, StampRepositoryRead stampRepositoryRead) {
        this.stampRepositoryWrite = stampRepositoryWrite;
        this.stampRepositoryRead = stampRepositoryRead;
    }

    @Override
    public List<Stamp> listByOrganization(Integer organizationId) {
        return stampRepositoryRead.listByOrganization(organizationId);
    }

    @Override
    public List<Stamp> listActiveByOrganization(Integer organizationId) {
        return stampRepositoryRead.listActiveByOrganization(organizationId);
    }

    @Override
    public Stamp getById(Integer stampId) {
        return stampRepositoryRead.getById(stampId);
    }

    @Override
    public Integer create(Stamp stamp) {
        return stampRepositoryWrite.create(stamp);
    }

    @Override
    public void update(Stamp stamp) {
        stampRepositoryWrite.update(stamp);
    }

    @Override
    public void delete(Integer stampId) {
        stampRepositoryWrite.delete(stampId);
    }

    @Override
    public void deleteAllByOrganization(Integer organizationId) {
        stampRepositoryWrite.deleteAllByOrganization(organizationId);
    }
}
