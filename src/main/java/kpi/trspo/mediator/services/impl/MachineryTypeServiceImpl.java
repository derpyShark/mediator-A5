package kpi.trspo.mediator.services.impl;

import kpi.trspo.mediator.services.interfaces.MachineryTypeService;
import kpi.trspo.mediator.services.model.MachineryType;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@AllArgsConstructor
public class MachineryTypeServiceImpl implements MachineryTypeService {
    final RestTemplate restTemplate = new RestTemplate();

    final String URL = "http://10.98.193.237:80/api/machineryTypes";

    public void delete(UUID id)
    {
        String workingURL = URL + "/" + id;
        restTemplate.delete(workingURL);
    }

    @RabbitListener(queues = "machineryTypeQueue")
    public void create(MachineryType addedMachineryType){
        HttpEntity<MachineryType> request = new HttpEntity<>(addedMachineryType);
        restTemplate.postForObject(URL,request,MachineryType.class);
    }

    public List<MachineryType> getAll(){
        Optional<MachineryType[]> machineryTypesMaybe = Optional.ofNullable(
                restTemplate.getForObject(URL, MachineryType[].class));
        List<MachineryType> machineryTypes = new ArrayList<>();
        if(machineryTypesMaybe.isPresent()){
            machineryTypes = Arrays.asList(machineryTypesMaybe.get());
        }
        return machineryTypes;
    }

    public MachineryType getById(UUID machineryTypeId){
        String workingURL = URL + "/" + machineryTypeId;
        return restTemplate.getForObject(workingURL, MachineryType.class);
    }

    public MachineryType update(UUID machineryTypeId, MachineryType machineryTypeObject){
        String workingURL = URL + "/" + machineryTypeId;
        restTemplate.put(workingURL,machineryTypeObject);
        return getById(machineryTypeId);
    }

}
