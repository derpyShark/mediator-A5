package kpi.trspo.mediator.services.interfaces;

import kpi.trspo.mediator.services.model.PersonRole;

import java.util.List;
import java.util.UUID;

public interface PersonRoleService {
    List<PersonRole> getAll();
    PersonRole getById(UUID roleId);
    void create(PersonRole personRoleObject);
    PersonRole update(UUID roleId, PersonRole personRoleDetails);
    void delete(UUID roleId);
}
