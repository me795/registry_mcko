package ru.mcko.registry.repository.read;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import ru.mcko.registry.entity.Role;

@Repository
public interface RoleRepositoryRead {

    @Select("SELECT id, name FROM roles WHERE id = #{id}")
    Role getOne(@Param("id") Long id);
}
