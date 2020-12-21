package kpi.trspo.mediator.api.controller;

import kpi.trspo.mediator.services.interfaces.PersonRoleService;
import kpi.trspo.mediator.services.model.PersonRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/personRoles")
public class PersonRoleController {
    private final PersonRoleService personRoleService;

    @Autowired
    public PersonRoleController(PersonRoleService personRoleService){
        this.personRoleService = personRoleService;
    }

    @GetMapping
    public List<PersonRole> getPersonRole(){ return personRoleService.getAll();}

    @GetMapping(path = "{id}")
    public PersonRole getPersonRoleById(@PathVariable("id") UUID id)
    {
        return personRoleService.getById(id);
    }

    @PostMapping
    public PersonRole addPersonRole(@RequestBody PersonRole addedPersonRole)
    {
        return personRoleService.create(addedPersonRole);
    }

    @PutMapping(path = "{id}")
    public void changePersonRole(@PathVariable("id") UUID id, @RequestBody PersonRole personRoleDetails)

    {
        personRoleService.update(id, personRoleDetails);
    }

    @DeleteMapping(path = "{id}")
    public void deletePersonRoleById(@PathVariable("id") UUID id)
    {
        personRoleService.delete(id);
    }
}
