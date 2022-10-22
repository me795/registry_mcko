package ru.mcko.registry.service;

import org.springframework.stereotype.Service;
import ru.mcko.registry.datatables.DataTablesInput;
import ru.mcko.registry.datatables.DataTablesOutput;
import ru.mcko.registry.entity.Delegate;
import ru.mcko.registry.repository.read.DelegateRepositoryRead;
import ru.mcko.registry.repository.write.DelegateRepositoryWrite;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DelegateServiceImpl implements DelegateService {

    private final DelegateRepositoryWrite delegateRepositoryWrite;
    private final DelegateRepositoryRead delegateRepositoryRead;

    public DelegateServiceImpl(DelegateRepositoryWrite delegateRepositoryWrite, DelegateRepositoryRead delegateRepositoryRead) {
        this.delegateRepositoryWrite = delegateRepositoryWrite;
        this.delegateRepositoryRead = delegateRepositoryRead;
    }

    @Override
    public List<Delegate> listByFIO(String fio) {
        var params = getPartsOfFIO(fio);
        return delegateRepositoryRead.listByFIO(params);
    }

    @Override
    public List<Delegate> listAll() {
        return delegateRepositoryRead.listAll();
    }

    @Override
    public DataTablesOutput listForDataTable(DataTablesInput dataTablesInput) {

        var params = getPartsOfFIO(dataTablesInput.getSearch().getValue());

        params.put("limit",dataTablesInput.getLength());
        params.put("offset",dataTablesInput.getStart());
        var orderColumnNum = dataTablesInput.getOrder().get(0).getColumn();
        var orderColumn = dataTablesInput.getColumns().get(orderColumnNum).getName();
        params.put("orderColumn",orderColumn);
        if (dataTablesInput.getOrder().get(0).getDir().equals("asc")){
            params.put("asc",1);
        }


        var dataTablesOutput = new DataTablesOutput<Delegate>();
        dataTablesOutput.setDraw(dataTablesInput.getDraw());
        dataTablesOutput.setRecordsTotal(delegateRepositoryRead.count());
        dataTablesOutput.setRecordsFiltered(delegateRepositoryRead.countWithFilter(params));
        dataTablesOutput.setData(delegateRepositoryRead.list(params));

        return dataTablesOutput;
    }

    @Override
    public Long count() {
        return delegateRepositoryRead.count();
    }

    @Override
    public Long countAll() {
        return delegateRepositoryRead.countAll();
    }

    @Override
    public Delegate getById(Integer delegateId) {
        return delegateRepositoryRead.getById(delegateId);
    }

    @Override
    public Integer create(Delegate delegate) {
        return delegateRepositoryWrite.create(delegate);
    }

    @Override
    public void update(Delegate delegate) {
        delegateRepositoryWrite.update(delegate);
    }

    @Override
    public void delete(Integer delegateId) {
        delegateRepositoryWrite.delete(delegateId);
    }


    private Map<String, Object> getPartsOfFIO(String fio) {

        Map<String, Object> fioMap = new HashMap<>();

        if (!((fio == null) || (fio.length() == 0))){
            var partsOfFIO = fio.split(" ", 3);
            var count = (int) Arrays.stream(partsOfFIO).count();

            switch (count) {
                case 2:
                    fioMap.put("surname", partsOfFIO[0].toLowerCase());
                    fioMap.put("name", partsOfFIO[1].toLowerCase());
                    break;
                case 3:
                    fioMap.put("surname", partsOfFIO[0].toLowerCase());
                    fioMap.put("name", partsOfFIO[1].toLowerCase());
                    fioMap.put("middleName", partsOfFIO[2].toLowerCase());
                    break;
                default:
                    fioMap.put("surname", "%" + partsOfFIO[0].toLowerCase() + "%");
                    break;
            }
        }
        return fioMap;
    }
}
