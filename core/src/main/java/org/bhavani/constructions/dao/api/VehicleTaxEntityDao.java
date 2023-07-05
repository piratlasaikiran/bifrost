package org.bhavani.constructions.dao.api;

import org.bhavani.constructions.dao.entities.VehicleTaxEntity;
import org.bhavani.constructions.dao.entities.models.VehicleTaxEnum;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VehicleTaxEntityDao {

    public void saveVehicleTaxEntity(VehicleTaxEntity vehicleTaxEntity);

    void saveVehicleTaxEntities(List<VehicleTaxEntity> vehicleTaxEntities);

    Optional<VehicleTaxEntity> getVehicleTaxEntity(String vehicleNumber, VehicleTaxEnum taxType, LocalDate validityStartDate);

    void deleteVehicleTaxEntry(VehicleTaxEntity vehicleTaxEntity);

    List<VehicleTaxEntity> getLatestTaxTypesForAllVehicles();
}
