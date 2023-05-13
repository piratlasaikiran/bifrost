package org.bhavani.constructions.dao.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bhavani.constructions.dao.entities.subentities.BaseEntity;

import javax.persistence.*;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@NamedQueries(value = {
        @NamedQuery(name = "GetVehicleByNumber",
                query = "select V from VehicleEntity V where V.vehicleNumber = :vehicle_num")
})
@Table(name = "vehicles")
public class VehicleEntity extends BaseEntity {

    @Id
    @Column(name = "vehicle_num")
    private String vehicleNumber;

    @Column(name = "owner")
    private String owner;

    @Column(name = "chassis_num")
    private String chassisNumber;

    @Column(name = "engine_num")
    private String engineNumber;

    @Column(name = "vehicle_class")
    private String vehicleClass;

    @Column(name = "insurance_provider")
    private String insuranceProvider;

    @Column(name = "finance_provider")
    private String financeProvider;
}
