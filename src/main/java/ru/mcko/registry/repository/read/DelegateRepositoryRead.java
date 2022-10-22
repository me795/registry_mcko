package ru.mcko.registry.repository.read;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import ru.mcko.registry.entity.Delegate;

import java.util.List;
import java.util.Map;

@Repository
public interface DelegateRepositoryRead {

    List<Delegate> listByFIO(@Param("params") Map<String,Object> params);

    List<Delegate> listAll();

    List<Delegate> list(@Param("params") Map<String,Object> params);

    Long countAll();

    Long count();

    Long countWithFilter(@Param("params") Map<String,Object> params);

    Delegate getById(@Param("id") Integer id);

}
