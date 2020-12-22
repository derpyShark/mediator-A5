package kpi.trspo.mediator.services.interfaces;

import kpi.trspo.mediator.services.model.CargoType;


import java.util.List;
import java.util.UUID;

public interface CargoTypeService {
    List<CargoType> getAll();
    CargoType getById(UUID cargoTypeId);
    void create(CargoType cargoTypeObject);
    CargoType update(UUID cargoTypeId, CargoType cargoTypeDetails);
    void delete(UUID cargoTypeId);
}
