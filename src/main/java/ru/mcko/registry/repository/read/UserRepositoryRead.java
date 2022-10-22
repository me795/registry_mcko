package ru.mcko.registry.repository.read;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import ru.mcko.registry.entity.User;

import java.util.List;

@Repository
public interface UserRepositoryRead {

    User findByName(@Param("email") String email);

    User findActiveByName(@Param("email") String email);

    User findById(@Param("id") Integer id);

    List<User> listAll();

}
