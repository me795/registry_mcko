package ru.mcko.registry.repository.write;

import org.springframework.stereotype.Repository;
import ru.mcko.registry.entity.Delegate;

@Repository
public interface DelegateRepositoryWrite {

    Integer create(Delegate delegate);

    void update(Delegate delegate);

    void delete(Integer delegateId);

}
