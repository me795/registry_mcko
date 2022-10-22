package ru.mcko.registry.controller;

import org.springframework.web.bind.annotation.*;
import ru.mcko.registry.datatables.DataTablesInput;
import ru.mcko.registry.datatables.DataTablesOutput;
import ru.mcko.registry.entity.Delegate;
import ru.mcko.registry.service.DelegateService;

import java.util.List;

@RestController
public class DelegateRestController {

    private final DelegateService delegateService;

    public DelegateRestController(DelegateService delegateService) {
        this.delegateService = delegateService;
    }

    @PostMapping("/delegates/data")
    public DataTablesOutput<Delegate> listDelegates(@RequestBody DataTablesInput dataTablesInput) {

        return delegateService.listForDataTable(dataTablesInput);
    }

    @GetMapping("/delegate/{id}/info")
    public Delegate getDelegateInfoById(@PathVariable("id") Integer id) {

        return delegateService.getById(id);
    }

    @PostMapping("/delegates/search")
    public List<Delegate> searchDelegates(@RequestParam("searchValue") String searchValue) {

        return delegateService.listByFIO(searchValue);
    }


    @PutMapping("/delegate")
    public Delegate createDelegate(@RequestBody Delegate delegate) {

        var delegateId = delegateService.create(delegate);
        delegate.setId(delegateId);

        return delegate;
    }


    @PutMapping("/delegate/{id}")
    public Delegate updateDelegate(@PathVariable("id") Integer id,
                       @RequestBody Delegate delegate) {
        delegate.setId(id);
        delegateService.update(delegate);

        return delegate;
    }

    @DeleteMapping("/delegate/{id}")
    public Delegate deleteDelegate(@PathVariable("id") Integer id) {

        var delegate = delegateService.getById(id);
        if (delegate != null) {
            delegateService.delete(id);
        }

        return delegate;
    }




}
