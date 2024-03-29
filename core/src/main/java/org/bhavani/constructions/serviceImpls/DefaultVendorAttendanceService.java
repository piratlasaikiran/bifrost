package org.bhavani.constructions.serviceImpls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.api.*;
import org.bhavani.constructions.dao.entities.PendingBalanceEntity;
import org.bhavani.constructions.dao.entities.TransactionEntity;
import org.bhavani.constructions.dao.entities.VendorAttendanceEntity;
import org.bhavani.constructions.dao.entities.VendorEntity;
import org.bhavani.constructions.dao.entities.models.CommodityType;
import org.bhavani.constructions.dto.CreateVendorAttendanceRequestDTO;
import org.bhavani.constructions.services.VendorAttendanceService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static org.bhavani.constructions.constants.ErrorConstants.ATTENDANCE_ENTRY_NOT_FOUND;
import static org.bhavani.constructions.constants.ErrorConstants.TRANSACTION_ERROR;
import static org.bhavani.constructions.utils.EntityBuilder.*;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
@Slf4j
public class DefaultVendorAttendanceService implements VendorAttendanceService {

    private final VendorAttendanceEntityDao vendorAttendanceEntityDao;
    private final VendorEntityDao vendorEntityDao;
    private final TransactionEntityDao transactionEntityDao;
    private final PendingBalanceEntityDao pendingBalanceEntityDao;

    @Override
    public VendorAttendanceEntity enterAttendance(CreateVendorAttendanceRequestDTO createVendorAttendanceRequestDTO, String userId) {

        //Marking the attendance
        VendorAttendanceEntity vendorAttendanceEntity = createVendorAttendanceEntity(createVendorAttendanceRequestDTO, userId);
        vendorAttendanceEntityDao.enterAttendance(vendorAttendanceEntity);
        log.info("Attendance for: {} entered successfully by: {}", vendorAttendanceEntity.getAttendanceDate(), vendorAttendanceEntity.getEnteredBy());
        if(createVendorAttendanceRequestDTO.isMakeTransaction()){
            VendorEntity vendorEntity = vendorEntityDao.getVendorById(createVendorAttendanceRequestDTO.getVendorId()).orElseThrow(() -> {
                log.error("Error while making transaction based on attendance");
                return new RuntimeException(TRANSACTION_ERROR);
            });
            Map<CommodityType, Integer> vendorCommodityCosts = vendorEntity.getCommodityCosts();
            Map<CommodityType, Integer> vendorCommodityAttendance = createVendorAttendanceRequestDTO.getCommodityAttendance();
            AtomicLong vendorPaymentAmount = new AtomicLong(0);

            //Calculating all the commodity costs entered as a part of attendance.
            vendorCommodityAttendance.forEach((commodity, units) -> vendorPaymentAmount.addAndGet((long) vendorCommodityCosts.get(commodity) * units));

            //Making a transaction now.
            TransactionEntity transactionEntity = getTransactionEntityForAttendance(
                    createVendorAttendanceRequestDTO.getVendorId(), vendorPaymentAmount.get(),
                    createVendorAttendanceRequestDTO.getAttendanceDate(), userId);
            checkAndUpdatePendingBalanceEntity(transactionEntity, userId, vendorPaymentAmount.get());
            transactionEntityDao.saveTransaction(transactionEntity);
        }
        return vendorAttendanceEntity;
    }

    private void checkAndUpdatePendingBalanceEntity(TransactionEntity transactionEntity, String userId, long dayAmount) {
        Optional<PendingBalanceEntity> pendingBalanceEntity = pendingBalanceEntityDao.getLatestPendingBalanceEntity(transactionEntity.getDestination());
        Long pendingAmount = dayAmount;
        if(pendingBalanceEntity.isPresent()){
            log.info("Fetching previous pending balance for user: {}", transactionEntity.getDestination());
            pendingAmount += pendingBalanceEntity.get().getPendingBalance();

        }
        PendingBalanceEntity latestPendingBalanceEntity = createPendingBalanceEntity(transactionEntity.getDestination(), transactionEntity, pendingAmount, userId);
        pendingBalanceEntityDao.savePendingBalanceEntity(latestPendingBalanceEntity);
    }

    @Override
    public List<CreateVendorAttendanceRequestDTO> getAllVendorAttendance() {
        List<VendorAttendanceEntity> vendorAttendanceEntities = vendorAttendanceEntityDao.getAllVendorAttendance();
        List<CreateVendorAttendanceRequestDTO> vendorAttendanceRequestDTOS = new ArrayList<>();
        vendorAttendanceEntities.forEach(vendorAttendanceEntity -> {
            vendorAttendanceRequestDTOS.add(CreateVendorAttendanceRequestDTO.builder()
                            .id(vendorAttendanceEntity.getId())
                            .vendorId(vendorAttendanceEntity.getVendorId())
                            .site(vendorAttendanceEntity.getSite())
                            .attendanceDate(vendorAttendanceEntity.getAttendanceDate())
                            .commodityAttendance(vendorAttendanceEntity.getCommodityAttendance())
                            .enteredBy(vendorAttendanceEntity.getEnteredBy())
                            .makeTransaction(vendorAttendanceEntity.isMakeTransaction())
                            .bankAccount(vendorAttendanceEntity.getBankAccount())
                            .build()
            );
        });
        return vendorAttendanceRequestDTOS;
    }

    @Override
    public VendorAttendanceEntity updateAttendance(CreateVendorAttendanceRequestDTO createVendorAttendanceRequestDTO, String userId, Long existingVendorAttendanceId) {
        VendorAttendanceEntity existingVendorAttendanceEntity = vendorAttendanceEntityDao.getVendorAttendance(existingVendorAttendanceId).orElseThrow(() -> {
            log.error("Error while updating attendance");
            return new RuntimeException(ATTENDANCE_ENTRY_NOT_FOUND);
        });
        existingVendorAttendanceEntity.setSite(createVendorAttendanceRequestDTO.getSite());
        existingVendorAttendanceEntity.setVendorId(createVendorAttendanceRequestDTO.getVendorId());
        existingVendorAttendanceEntity.setEnteredBy(createVendorAttendanceRequestDTO.getEnteredBy());
        existingVendorAttendanceEntity.setAttendanceDate(createVendorAttendanceRequestDTO.getAttendanceDate());
        existingVendorAttendanceEntity.setCommodityAttendance(createVendorAttendanceRequestDTO.getCommodityAttendance());
        existingVendorAttendanceEntity.setMakeTransaction(createVendorAttendanceRequestDTO.isMakeTransaction());
        existingVendorAttendanceEntity.setBankAccount(createVendorAttendanceRequestDTO.getBankAccount());

        //ToDo: make changes in existing transaction entity and reflect balances accordingly.

        return existingVendorAttendanceEntity;
    }
}
