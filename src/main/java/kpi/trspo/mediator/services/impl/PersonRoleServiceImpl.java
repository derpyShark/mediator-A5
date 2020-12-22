package kpi.trspo.mediator.services.impl;

import kpi.trspo.mediator.services.interfaces.PersonRoleService;
import kpi.trspo.mediator.services.model.PersonRole;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@AllArgsConstructor
public class PersonRoleServiceImpl implements PersonRoleService {

    final RestTemplate restTemplate = new RestTemplate();

    final String URL = "http://10.98.193.236:80/api/personRoles";

    public void delete(UUID id)
    {
        String workingURL = URL + "/" + id;
        restTemplate.delete(workingURL);
    }

    @RabbitListener(queues = "personRoleQueue")
    public void create(PersonRole addedPerson){
        HttpEntity<PersonRole> request = new HttpEntity<>(addedPerson);
        restTemplate.postForObject(URL,request, PersonRole.class);
    }

    public List<PersonRole> getAll(){
        Optional<PersonRole[]> personsMaybe = Optional.ofNullable(
                restTemplate.getForObject(URL, PersonRole[].class));
        List<PersonRole> persons = new ArrayList<>();
        if(personsMaybe.isPresent()){
            persons = Arrays.asList(personsMaybe.get());
        }
        return persons;
    }

    public PersonRole getById(UUID personId){
        String workingURL = URL + "/" + personId;
        return restTemplate.getForObject(workingURL, PersonRole.class);
    }

    public PersonRole update(UUID personId, PersonRole personObject){
        String workingURL = URL + "/" + personId;
        restTemplate.put(workingURL,personObject);
        return getById(personId);
    }
}
