package kpi.trspo.mediator.services.GrpcServices;

import com.example.demo.*;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import kpi.trspo.mediator.services.impl.PersonRoleServiceImpl;
import kpi.trspo.mediator.services.interfaces.PersonRoleService;
import kpi.trspo.mediator.services.model.CargoType;
import kpi.trspo.mediator.services.model.PersonRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;

public class PersonRoleServiceGrpcImpl extends PersonRoleServiceGrpc.PersonRoleServiceImplBase {
    final RestTemplate restTemplate = new RestTemplate();
    final String URL = "http://10.98.193.236:80/api/personRoles";

    @Override
    public void getPersonRoleById(PersonRoleByIdRequest request, StreamObserver<PersonRoleResponse> responseObserver) {
        String workingURL = URL + "/" + request.getId();
        PersonRole personRole = restTemplate.getForObject(workingURL, PersonRole.class);
        responseObserver.onNext(PersonRoleResponse.newBuilder()
                .setId(request.getId())
                .setRoleName(personRole.getRoleName())
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void getPersonRoles(Empty request, StreamObserver<PersonRoleListResponse> responseObserver) {
        Optional<PersonRole[]> personsMaybe = Optional.ofNullable(
                restTemplate.getForObject(URL, PersonRole[].class));
        List<PersonRole> persons = new ArrayList<>();
        if(personsMaybe.isPresent()){
            persons = Arrays.asList(personsMaybe.get());
        }
        List<PersonRoleResponse> personRoleResponses = new ArrayList<>();
        for (PersonRole pr : persons){
            personRoleResponses.add(PersonRoleResponse.newBuilder()
                    .setId(pr.getRoleId().toString())
                    .setRoleName(pr.getRoleName())
                    .build());
        }
        PersonRoleListResponse personRoleList = PersonRoleListResponse.newBuilder()
                .addAllRoles(personRoleResponses)
                .build();
        responseObserver.onNext(personRoleList);
        responseObserver.onCompleted();
    }

    @Override
    public void changePersonRoleById(PersonRoleChangeRequest request, StreamObserver<PersonRoleResponse> responseObserver) {
        String workingURL = URL + "/" + request.getId();
        restTemplate.put(workingURL,PersonRole.builder()
                                                .roleName(request.getRoleName())
                                                .build());
        PersonRole personRole = restTemplate.getForObject(workingURL, PersonRole.class);
        responseObserver.onNext(PersonRoleResponse.newBuilder()
                .setId(request.getId())
                .setRoleName(personRole.getRoleName())
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void deletePersonRoleById(PersonRoleByIdRequest request, StreamObserver<Empty> responseObserver) {
        String workingURL = URL + "/" + request.getId();
        restTemplate.delete(workingURL);
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void addPersonRole(PersonRoleAddRequest request, StreamObserver<PersonRoleResponse> responseObserver) {
        HttpEntity<PersonRole> restRequest = new HttpEntity<PersonRole>(PersonRole.builder()
                .roleName(request.getRoleName()).build());
        PersonRole personRole = restTemplate.postForObject(URL,restRequest, PersonRole.class);
        responseObserver.onNext(PersonRoleResponse.newBuilder()
                .setId(personRole.getRoleId().toString())
                .setRoleName(personRole.getRoleName())
                .build());
        responseObserver.onCompleted();
    }
}
