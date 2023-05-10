package org.bhavani.constructions.serviceImpls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.bhavani.constructions.dao.api.VehicleEntityDao;
import org.bhavani.constructions.dao.api.VehicleTaxEntityDao;
import org.bhavani.constructions.dao.entities.VehicleEntity;
import org.bhavani.constructions.dao.entities.VehicleTaxEntity;
import org.bhavani.constructions.dao.entities.models.VehicleTaxEnum;
import org.bhavani.constructions.dto.CreateVehicleRequestDTO;
import org.bhavani.constructions.dto.UploadVehicleTaxRequestDTO;
import org.bhavani.constructions.services.VehicleService;
import org.bhavani.constructions.utils.VehicleHelper;

import javax.inject.Inject;
import java.io.IOException;
import java.time.LocalDate;
import java.util.EnumSet;
import java.util.List;

import static org.bhavani.constructions.constants.ErrorConstants.USER_NOT_FOUND;
import static org.bhavani.constructions.utils.EntityBuilder.*;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
@Slf4j
public class DefaultVehicleService implements VehicleService {

    private final VehicleEntityDao vehicleEntityDao;
    private final VehicleTaxEntityDao vehicleTaxEntityDao;

    public EnumSet<VehicleTaxEnum> getVehicleTaxTypes(){
        return EnumSet.allOf(VehicleTaxEnum.class);
    }

    @Override
    public void addVehicle(CreateVehicleRequestDTO createVehicleRequestDTO, List<UploadVehicleTaxRequestDTO> vehicleTaxRequestDTOS) {
        VehicleEntity vehicle = createVehicle(createVehicleRequestDTO);
        List<VehicleTaxEntity> vehicleTaxEntities = createVehicleTaxEntities(vehicleTaxRequestDTOS, vehicle.getVehicleNumber());
        vehicleEntityDao.saveVehicleEntity(vehicle);
        vehicleTaxEntityDao.saveVehicleTaxEntities(vehicleTaxEntities);
    }

    @Override
    public VehicleEntity updateVehicle(CreateVehicleRequestDTO updateVehicleRequestDTO) {
        VehicleEntity vehicle = vehicleEntityDao.getVehicle(updateVehicleRequestDTO.getVehicleNumber())
                .orElseThrow(() -> {
                    log.error("{} doesn't exist. Create vehicle first", updateVehicleRequestDTO.getVehicleNumber());
                    return new IllegalArgumentException(USER_NOT_FOUND);
                });
        updateVehicleData(vehicle, updateVehicleRequestDTO);
        return vehicle;
    }

    @Override
    public VehicleEntity getVehicle(String vehicleNumber) {
        return vehicleEntityDao.getVehicle(vehicleNumber)
                .orElseThrow(() -> {
                    log.error("{} doesn't exist.", vehicleNumber);
                    return new IllegalArgumentException(USER_NOT_FOUND);
                });
    }

    @Override
    public void uploadNewTaxDocument(String vehicleNumber, UploadVehicleTaxRequestDTO uploadVehicleTaxRequestDTO) {
        try{
            VehicleTaxEntity vehicleTaxEntity = createVehicleTaxEntity(uploadVehicleTaxRequestDTO, vehicleNumber);
            vehicleTaxEntityDao.saveVehicleTaxEntity(vehicleTaxEntity);
        }catch (IOException e){
            throw new RuntimeException();
        }
    }

    @Override
    public VehicleTaxEntity getVehicleTaxEntry(String vehicleNumber, VehicleTaxEnum taxType, LocalDate validityStartDate) {
        return vehicleTaxEntityDao.getVehicleTaxEntity(vehicleNumber, taxType, validityStartDate)
                .orElseThrow(RuntimeException::new);
    }

    @Override
    public void updateVehicleTaxEntry(VehicleTaxEntity vehicleTaxEntity, UploadVehicleTaxRequestDTO uploadVehicleTaxRequestDTO) {
        try {
            vehicleTaxEntity.setTaxType(uploadVehicleTaxRequestDTO.getTaxType());
            vehicleTaxEntity.setAmount(uploadVehicleTaxRequestDTO.getAmount());
            vehicleTaxEntity.setTaxReceipt(IOUtils.toByteArray(uploadVehicleTaxRequestDTO.getTaxReceipt()));
            vehicleTaxEntity.setValidityStartDate(uploadVehicleTaxRequestDTO.getValidityStartDate());
            vehicleTaxEntity.setValidityEndDate(uploadVehicleTaxRequestDTO.getValidityEndDate());
        }catch (IOException e){
            log.error("Error processing the tax receipt");
            throw new RuntimeException();
        }
    }

    @Override
    public void deleteVehicleTaxEntry(String vehicleNumber, VehicleTaxEnum taxType, LocalDate validityStartDate) {
        VehicleTaxEntity vehicleTaxEntity = vehicleTaxEntityDao.getVehicleTaxEntity(vehicleNumber, taxType, validityStartDate)
                .orElseThrow(RuntimeException::new);
        vehicleTaxEntityDao.deleteVehicleTaxEntry(vehicleTaxEntity);
    }

    @Override
    public void delete(String vehicleNumber) {
        VehicleEntity vehicle = vehicleEntityDao.getVehicle(vehicleNumber)
                .orElseThrow(() -> {
                    log.error("{} doesn't exist.", vehicleNumber);
                    return new IllegalArgumentException(USER_NOT_FOUND);
                });
        vehicleEntityDao.deleteVehicle(vehicle);
    }

    private void updateVehicleData(VehicleEntity vehicle, CreateVehicleRequestDTO updateVehicleRequestDTO) {
        vehicle.setOwner(updateVehicleRequestDTO.getOwner());
        vehicle.setChassisNumber(updateVehicleRequestDTO.getChassisNumber());
        vehicle.setEngineNumber(updateVehicleRequestDTO.getEngineNumber());
        vehicle.setVehicleClass(updateVehicleRequestDTO.getVehicleClass());
        vehicle.setInsuranceProvider(updateVehicleRequestDTO.getInsuranceProvider());
        vehicle.setFinanceProvider(updateVehicleRequestDTO.getFinanceProvider());
    }
}
