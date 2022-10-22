package ru.mcko.registry.repository.write;

import org.springframework.stereotype.Repository;
import ru.mcko.registry.entity.Stamp;

@Repository
public interface StampRepositoryWrite {

    Integer create(Stamp stamp);

    void update(Stamp stamp);

    void delete(Integer stampId);

    void deleteAllByOrganization(Integer organizationId);

}
