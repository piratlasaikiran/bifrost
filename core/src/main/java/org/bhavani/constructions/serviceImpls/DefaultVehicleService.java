package org.bhavani.constructions.serviceImpls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.bhavani.constructions.dao.api.*;
import org.bhavani.constructions.dao.entities.*;
import org.bhavani.constructions.dao.entities.models.VehicleTaxEnum;
import org.bhavani.constructions.dto.CreateVehicleRequestDTO;
import org.bhavani.constructions.dto.UploadVehicleTaxRequestDTO;
import org.bhavani.constructions.services.VehicleService;

import javax.inject.Inject;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import static org.bhavani.constructions.constants.ErrorConstants.*;
import static org.bhavani.constructions.utils.EntityBuilder.*;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
@Slf4j
public class DefaultVehicleService implements VehicleService {

    private final VehicleEntityDao vehicleEntityDao;
    private final VehicleTaxEntityDao vehicleTaxEntityDao;
    private final AssetLocationEntityDao assetLocationEntityDao;
    private final AssetOwnershipEntityDao assetOwnershipEntityDao;
    private final SiteEntityDao siteEntityDao;
    private final TransactionEntityDao transactionEntityDao;

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
    public VehicleEntity updateVehicle(CreateVehicleRequestDTO updateVehicleRequestDTO, String vehicleNumber) {
        VehicleEntity vehicle = vehicleEntityDao.getVehicle(vehicleNumber)
                .orElseThrow(() -> {
                    log.error("{} doesn't exist. Create vehicle first", vehicleNumber);
                    return new IllegalArgumentException(VEHICLE_NOT_FOUND);
                });
        if(!vehicleNumber.equals(updateVehicleRequestDTO.getVehicleNumber())){
            vehicleEntityDao.getVehicle(updateVehicleRequestDTO.getVehicleNumber())
                    .ifPresent(existingVehicle -> {
                        log.error("{} already exists", updateVehicleRequestDTO.getVehicleNumber());
                        throw  new IllegalArgumentException(VEHICLE_EXISTS);
                    });
            updateDependingEntities(vehicleNumber, updateVehicleRequestDTO.getVehicleNumber());
        }
        updateVehicleData(vehicle, updateVehicleRequestDTO);
        return vehicle;
    }

    private void updateDependingEntities(String oldVehicleNumber, String newVehicleNumber) {
        List<AssetLocationEntity> assetLocationEntities = assetLocationEntityDao.getAssetLocationEntities(oldVehicleNumber);
        assetLocationEntities.forEach(assetLocationEntity -> assetLocationEntity.setAssetName(newVehicleNumber));

        List<AssetOwnershipEntity> assetOwnershipEntities = assetOwnershipEntityDao.getAssetOwnershipEntities(oldVehicleNumber);
        assetOwnershipEntities.forEach(assetOwnershipEntity -> assetOwnershipEntity.setAssetName(newVehicleNumber));

        List<VehicleTaxEntity> vehicleTaxEntities = vehicleTaxEntityDao.getTaxesForVehicle(oldVehicleNumber);
        vehicleTaxEntities.forEach(vehicleTaxEntity -> vehicleTaxEntity.setVehicleNumber(newVehicleNumber));

        List<SiteEntity> siteEntities = siteEntityDao.getSitesWithVehicle(oldVehicleNumber);
        siteEntities.forEach(siteEntity -> {
            List<String> vehicles = siteEntity.getVehicles();
            vehicles.replaceAll(s -> s.equals(oldVehicleNumber) ? newVehicleNumber : s);
            siteEntity.setVehicles(convertListToCommaSeparatedString(vehicles));
        });

        List<TransactionEntity> transactionEntities = transactionEntityDao.getTransactionsByVehicleNumber(oldVehicleNumber);
        transactionEntities.forEach(transactionEntity -> transactionEntity.setVehicleNumber(newVehicleNumber));
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

    @Override
    public List<CreateVehicleRequestDTO> getVehicles() {
        List<VehicleEntity> vehicleEntities = vehicleEntityDao.getVehicles();
        List<CreateVehicleRequestDTO> vehicleRequestDTOS = new ArrayList<>();
        vehicleEntities.forEach(vehicleEntity -> vehicleRequestDTOS.add(getVehicleDTO(vehicleEntity)));
        return vehicleRequestDTOS;
    }

    private static CreateVehicleRequestDTO getVehicleDTO(VehicleEntity vehicleEntity) {
        return CreateVehicleRequestDTO.builder()
                .vehicleNumber(vehicleEntity.getVehicleNumber())
                .owner(vehicleEntity.getOwner())
                .chassisNumber(vehicleEntity.getChassisNumber())
                .engineNumber(vehicleEntity.getEngineNumber())
                .vehicleClass(vehicleEntity.getVehicleClass())
                .insuranceProvider(vehicleEntity.getInsuranceProvider())
                .financeProvider(vehicleEntity.getFinanceProvider())
                .build();
    }

    @Override
    public Map<String, List<UploadVehicleTaxRequestDTO>> getLatestTaxTypesByVehicleNumber() {
        List<VehicleTaxEntity> vehicleTaxEntities = vehicleTaxEntityDao.getLatestTaxTypesForAllVehicles();
        Map<String, List<UploadVehicleTaxRequestDTO>> vehicleLatestTaxes = new HashMap<>();
        vehicleTaxEntities.forEach(vehicleTaxEntity -> {
            UploadVehicleTaxRequestDTO vehicleTaxRequestDTO = getVehicleTaxDTO(vehicleTaxEntity);
            List<UploadVehicleTaxRequestDTO> existingLatestTaxes = vehicleLatestTaxes.get(vehicleTaxEntity.getVehicleNumber());
            if(Objects.isNull(existingLatestTaxes)) {
                existingLatestTaxes = new ArrayList<>();
                vehicleLatestTaxes.put(vehicleTaxEntity.getVehicleNumber(), existingLatestTaxes);
            }
            existingLatestTaxes.add(vehicleTaxRequestDTO);
        });
        return vehicleLatestTaxes;
    }

    @Override
    public List<UploadVehicleTaxRequestDTO> getVehicleTaxes(String vehicleNumber) {
        List<VehicleTaxEntity> vehicleTaxEntities = vehicleTaxEntityDao.getTaxesForVehicle(vehicleNumber);
        List<UploadVehicleTaxRequestDTO> vehicleTaxRequestDTOS = new ArrayList<>();
        vehicleTaxEntities.forEach(vehicleTaxEntity -> {
            vehicleTaxRequestDTOS.add(getVehicleTaxDTO(vehicleTaxEntity));
        });
        return vehicleTaxRequestDTOS;
    }

    private UploadVehicleTaxRequestDTO getVehicleTaxDTO(VehicleTaxEntity vehicleTaxEntity) {
        return UploadVehicleTaxRequestDTO.builder()
                .vehicleNumber(vehicleTaxEntity.getVehicleNumber())
                .taxType(vehicleTaxEntity.getTaxType())
                .amount(vehicleTaxEntity.getAmount())
                .validityStartDate(vehicleTaxEntity.getValidityStartDate())
                .validityEndDate(vehicleTaxEntity.getValidityEndDate())
                .build();
    }

    private void updateVehicleData(VehicleEntity vehicle, CreateVehicleRequestDTO updateVehicleRequestDTO) {
        vehicle.setVehicleNumber(updateVehicleRequestDTO.getVehicleNumber());
        vehicle.setOwner(updateVehicleRequestDTO.getOwner());
        vehicle.setChassisNumber(updateVehicleRequestDTO.getChassisNumber());
        vehicle.setEngineNumber(updateVehicleRequestDTO.getEngineNumber());
        vehicle.setVehicleClass(updateVehicleRequestDTO.getVehicleClass());
        vehicle.setInsuranceProvider(updateVehicleRequestDTO.getInsuranceProvider());
        vehicle.setFinanceProvider(updateVehicleRequestDTO.getFinanceProvider());
    }
}
