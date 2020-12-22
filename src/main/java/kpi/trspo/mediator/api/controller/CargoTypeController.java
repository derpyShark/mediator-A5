package kpi.trspo.mediator.api.controller;

import kpi.trspo.mediator.services.interfaces.CargoTypeService;
import kpi.trspo.mediator.services.model.CargoType;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/cargoTypes")

public final class CargoTypeController {

    private final CargoTypeService cargoTypeService;

    @Autowired
    public CargoTypeController(CargoTypeService cargoTypeService){
        this.cargoTypeService = cargoTypeService;
    }

    @Autowired
    RabbitTemplate rabbitTemplate;

    @GetMapping
    public List<CargoType> getCargoType(){ return  cargoTypeService.getAll();}

    @GetMapping(path = "{id}")
    public CargoType getCargoTypeById(@PathVariable("id") UUID id)
    {
        return cargoTypeService.getById(id);
    }

    @PostMapping
    public void addCargoType(@RequestBody CargoType addedCargoType)
    {
        rabbitTemplate.convertAndSend("direct-exchange","cargoType",addedCargoType);
    }

    @PutMapping(path = "{id}")
    public void changeCargoType(@PathVariable("id") UUID id, @RequestBody CargoType cargoTypeDetails)

    {
        cargoTypeService.update(id, cargoTypeDetails);
    }

    @DeleteMapping(path = "{id}")
    public void deleteCargoTypeById(@PathVariable("id") UUID id)
    {
        cargoTypeService.delete(id);
    }

}
