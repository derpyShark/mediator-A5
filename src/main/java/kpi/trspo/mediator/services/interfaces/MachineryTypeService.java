package kpi.trspo.mediator.services.interfaces;

import kpi.trspo.mediator.services.model.MachineryType;

import java.util.List;
import java.util.UUID;

public interface MachineryTypeService {
    List<MachineryType> getAll();
    MachineryType getById(UUID machineryTypeId);
    MachineryType create(MachineryType machineryTypeObject);
    MachineryType update(UUID machineryTypeId, MachineryType machineryTypeDetails);
    void delete(UUID machineryTypeId);
}
