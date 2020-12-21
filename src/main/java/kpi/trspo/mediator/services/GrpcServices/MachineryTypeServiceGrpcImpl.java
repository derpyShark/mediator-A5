package kpi.trspo.mediator.services.GrpcServices;

import com.example.demo.*;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import kpi.trspo.mediator.services.model.MachineryType;
import kpi.trspo.mediator.services.model.PersonRole;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MachineryTypeServiceGrpcImpl extends MachineryTypeServiceGrpc.MachineryTypeServiceImplBase {
    final RestTemplate restTemplate = new RestTemplate();
    final String URL = "http://10.98.193.237:80/api/machineryTypes";

    @Override
    public void getMachineryTypeById(MachineryTypeByIdRequest request, StreamObserver<MachineryTypeResponse> responseObserver) {
        String workingURL = URL + "/" + request.getId();
        MachineryType machineryType = restTemplate.getForObject(workingURL, MachineryType.class);
        responseObserver.onNext(MachineryTypeResponse.newBuilder()
                .setId(request.getId())
                .setMachineryTypeName(machineryType.getMachineryTypeName())
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void getMachineryTypes(Empty request, StreamObserver<MachineryTypeListResponse> responseObserver) {
        Optional<MachineryType[]> personsMaybe = Optional.ofNullable(
                restTemplate.getForObject(URL, MachineryType[].class));
        List<MachineryType> persons = new ArrayList<>();
        if(personsMaybe.isPresent()){
            persons = Arrays.asList(personsMaybe.get());
        }
        List<MachineryTypeResponse> machineryTypeResponses = new ArrayList<>();
        for (MachineryType pr : persons){
            machineryTypeResponses.add(MachineryTypeResponse.newBuilder()
                    .setId(pr.getTypeId().toString())
                    .setMachineryTypeName(pr.getMachineryTypeName())
                    .build());
        }
        MachineryTypeListResponse machineryTypeList = MachineryTypeListResponse.newBuilder()
                .addAllMachineryTypeList(machineryTypeResponses)
                .build();
        responseObserver.onNext(machineryTypeList);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteMachineryTypeById(MachineryTypeByIdRequest request, StreamObserver<Empty> responseObserver) {
        String workingURL = URL + "/" + request.getId();
        restTemplate.delete(workingURL);
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void changeMachineryTypeById(MachineryTypeChangeRequest request, StreamObserver<MachineryTypeResponse> responseObserver) {
        String workingURL = URL + "/" + request.getId();
        restTemplate.put(workingURL,MachineryType.builder()
                .machineryTypeName(request.getMachineryTypeName())
                .build());
        MachineryType machineryType = restTemplate.getForObject(workingURL, MachineryType.class);
        responseObserver.onNext(MachineryTypeResponse.newBuilder()
                .setId(request.getId())
                .setMachineryTypeName(machineryType.getMachineryTypeName())
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void addMachineryType(MachineryTypeAddRequest request, StreamObserver<MachineryTypeResponse> responseObserver) {
        HttpEntity<MachineryType> restRequest = new HttpEntity<MachineryType>(MachineryType.builder()
                .machineryTypeName(request.getMachineryTypeName()).build());
        MachineryType machineryType = restTemplate.postForObject(URL,restRequest, MachineryType.class);
        responseObserver.onNext(MachineryTypeResponse.newBuilder()
                .setId(machineryType.getTypeId().toString())
                .setMachineryTypeName(machineryType.getMachineryTypeName())
                .build());
        responseObserver.onCompleted();
    }
}