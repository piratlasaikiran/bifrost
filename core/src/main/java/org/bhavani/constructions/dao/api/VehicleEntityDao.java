package org.bhavani.constructions.dao.api;

import org.bhavani.constructions.dao.entities.VehicleEntity;

import java.util.Optional;

public interface VehicleEntityDao {

    void saveVehicleEntity(VehicleEntity vehicleEntity);

    Optional<VehicleEntity> getVehicle(String vehicleNumber);

    void deleteVehicle(VehicleEntity vehicle);
}
