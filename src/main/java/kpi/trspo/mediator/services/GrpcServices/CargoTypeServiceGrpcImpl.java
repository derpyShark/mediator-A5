package kpi.trspo.mediator.services.GrpcServices;

import com.example.demo.*;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import kpi.trspo.mediator.services.model.CargoType;
import kpi.trspo.mediator.services.model.MachineryType;
import kpi.trspo.mediator.services.model.PersonRole;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CargoTypeServiceGrpcImpl extends CargoTypeServiceGrpc.CargoTypeServiceImplBase {
    final RestTemplate restTemplate = new RestTemplate();
    final String URL = "http://10.98.193.235:80/api/cargoTypes";

    @Override
    public void getCargoTypeById(CargoTypeByIdRequest request, StreamObserver<CargoTypeResponse> responseObserver) {
        String workingURL = URL + "/" + request.getId();
        CargoType cargoType = restTemplate.getForObject(workingURL, CargoType.class);
        responseObserver.onNext(CargoTypeResponse.newBuilder()
                .setId(request.getId())
                .setCargoTypeName(cargoType.getCargoTypeName())
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void getCargoTypes(Empty request, StreamObserver<CargoTypeListResponse> responseObserver) {
        Optional<CargoType[]> cargoTypesMaybe = Optional.ofNullable(
                restTemplate.getForObject(URL, CargoType[].class));
        List<CargoType> cargoTypes = new ArrayList<>();
        if(cargoTypesMaybe.isPresent()){
            cargoTypes = Arrays.asList(cargoTypesMaybe.get());
        }
        List<CargoTypeResponse> cargoTypeResponses = new ArrayList<>();
        for (CargoType cargoType : cargoTypes){
            cargoTypeResponses.add(CargoTypeResponse.newBuilder()
                    .setId(cargoType.getCargoTypeId().toString())
                    .setCargoTypeName(cargoType.getCargoTypeName())
                    .build());
        }
        CargoTypeListResponse cargoTypeList = CargoTypeListResponse.newBuilder()
                .addAllCargoTypeList(cargoTypeResponses)
                .build();
        responseObserver.onNext(cargoTypeList);
        responseObserver.onCompleted();;
    }

    @Override
    public void changeCargoTypeById(CargoTypeChangeRequest request, StreamObserver<CargoTypeResponse> responseObserver) {
        String workingURL = URL + "/" + request.getId();
        restTemplate.put(workingURL,CargoType.builder()
                .cargoTypeName(request.getCargoTypeName())
                .build());
        CargoType cargoType = restTemplate.getForObject(workingURL, CargoType.class);
        responseObserver.onNext(CargoTypeResponse.newBuilder()
                .setId(request.getId())
                .setCargoTypeName(cargoType.getCargoTypeName())
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void deleteCargoTypeById(CargoTypeByIdRequest request, StreamObserver<Empty> responseObserver) {
        String workingURL = URL + "/" + request.getId();
        restTemplate.delete(workingURL);
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void addCargoType(CargoTypeAddRequest request, StreamObserver<CargoTypeResponse> responseObserver) {
        HttpEntity<CargoType> restRequest = new HttpEntity<CargoType>(CargoType.builder()
                .cargoTypeName(request.getCargoTypeName()).build());
        CargoType cargoType = restTemplate.postForObject(URL,restRequest, CargoType.class);
        responseObserver.onNext(CargoTypeResponse.newBuilder()
                .setId(cargoType.getCargoTypeId().toString())
                .setCargoTypeName(cargoType.getCargoTypeName())
                .build());
        responseObserver.onCompleted();
    }
}
