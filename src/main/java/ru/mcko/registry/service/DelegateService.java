package ru.mcko.registry.service;

import ru.mcko.registry.datatables.DataTablesInput;
import ru.mcko.registry.datatables.DataTablesOutput;
import ru.mcko.registry.entity.Delegate;

import java.util.List;

public interface DelegateService {

    List<Delegate> listByFIO(String fio);
    List<Delegate> listAll();

    DataTablesOutput listForDataTable(DataTablesInput dataTablesInput);

    Long count();

    Long countAll();

    Delegate getById(Integer delegateId);

    Integer create(Delegate delegate);

    void update(Delegate delegate);

    void delete(Integer delegateId);
}
