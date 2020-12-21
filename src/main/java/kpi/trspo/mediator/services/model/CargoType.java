package kpi.trspo.mediator.services.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CargoType {

    private UUID cargoTypeId;
    private String cargoTypeName;

}
