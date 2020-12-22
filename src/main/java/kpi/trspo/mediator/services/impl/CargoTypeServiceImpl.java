package kpi.trspo.mediator.services.impl;

import kpi.trspo.mediator.services.interfaces.CargoTypeService;
import kpi.trspo.mediator.services.model.CargoType;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@AllArgsConstructor
public class CargoTypeServiceImpl implements CargoTypeService {

    final RestTemplate restTemplate = new RestTemplate();

    final String URL = "http://10.98.193.235:80/api/cargoTypes";

    public void delete(UUID id)
    {
        String workingURL = URL + "/" + id;
        restTemplate.delete(workingURL);
    }

    @RabbitListener(queues = "cargoTypeQueue")
    public void create(CargoType addedCargoType){
        HttpEntity<CargoType> request = new HttpEntity<>(addedCargoType);
        restTemplate.postForObject(URL,request,CargoType.class);
    }

    public List<CargoType> getAll(){
        Optional<CargoType[]> cargoTypesMaybe = Optional.ofNullable(
                restTemplate.getForObject(URL, CargoType[].class));
        List<CargoType> cargoTypes = new ArrayList<>();
        if(cargoTypesMaybe.isPresent()){
            cargoTypes = Arrays.asList(cargoTypesMaybe.get());
        }
        return cargoTypes;
    }

    public CargoType getById(UUID cargoTypeId){
        String workingURL = URL + "/" + cargoTypeId;
        return restTemplate.getForObject(workingURL, CargoType.class);
    }


    public CargoType update(UUID cargoTypeId, CargoType cargoTypeObject){
        String workingURL = URL + "/" + cargoTypeId;
        restTemplate.put(workingURL,cargoTypeObject);
        return getById(cargoTypeId);
    }

}
