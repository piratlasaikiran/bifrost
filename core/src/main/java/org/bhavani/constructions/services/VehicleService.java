package org.bhavani.constructions.services;

import org.bhavani.constructions.dao.entities.VehicleEntity;
import org.bhavani.constructions.dao.entities.VehicleTaxEntity;
import org.bhavani.constructions.dao.entities.models.VehicleTaxEnum;
import org.bhavani.constructions.dto.CreateVehicleRequestDTO;
import org.bhavani.constructions.dto.UploadVehicleTaxRequestDTO;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

public interface VehicleService {

    EnumSet<VehicleTaxEnum> getVehicleTaxTypes();

    void addVehicle(CreateVehicleRequestDTO createVehicleRequestDTO, List<UploadVehicleTaxRequestDTO> vehicleTaxRequestDTOS);

    VehicleEntity updateVehicle(CreateVehicleRequestDTO createVehicleRequestDTO, String vehicleNumber);

    VehicleEntity getVehicle(String vehicleNumber);

    void uploadNewTaxDocument(String vehicleNumber, UploadVehicleTaxRequestDTO uploadVehicleTaxRequestDTO);

    VehicleTaxEntity getVehicleTaxEntry(String vehicleNumber, VehicleTaxEnum taxType, LocalDate validityStartDate);

    void updateVehicleTaxEntry(VehicleTaxEntity vehicleTaxEntity, UploadVehicleTaxRequestDTO uploadVehicleTaxRequestDTO);

    void deleteVehicleTaxEntry(String vehicleNumber, VehicleTaxEnum taxType, LocalDate validityStartDate);

    void delete(String vehicleNumber);

    List<CreateVehicleRequestDTO> getVehicles();

    Map<String, List<UploadVehicleTaxRequestDTO>> getLatestTaxTypesByVehicleNumber();

    List<UploadVehicleTaxRequestDTO> getVehicleTaxes(String vehicleNumber);
}
