package ru.mcko.registry.repository.write;

import org.springframework.stereotype.Repository;
import ru.mcko.registry.entity.User;

@Repository
public interface UserRepositoryWrite {

    Integer create(User user);

    void update(User user);

    void delete(Integer userId);

    void addRole(Integer userId, Integer roleId);

    void deleteAllRoles(Integer userId);
}
