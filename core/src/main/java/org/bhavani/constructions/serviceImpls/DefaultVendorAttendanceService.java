package org.bhavani.constructions.serviceImpls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.api.*;
import org.bhavani.constructions.dao.entities.*;
import org.bhavani.constructions.dao.entities.models.CommodityType;
import org.bhavani.constructions.dto.CreateVendorAttendanceRequestDTO;
import org.bhavani.constructions.services.VendorAttendanceService;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static org.bhavani.constructions.constants.ErrorConstants.TRANSACTION_ERROR;
import static org.bhavani.constructions.utils.EntityBuilder.createVendorAttendanceEntity;
import static org.bhavani.constructions.utils.EntityBuilder.getTransactionEntityForAttendance;
import static org.bhavani.constructions.utils.PassBookHelper.createPassBookEntities;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
@Slf4j
public class DefaultVendorAttendanceService implements VendorAttendanceService {

    private final VendorAttendanceEntityDao vendorAttendanceEntityDao;
    private final VendorEntityDao vendorEntityDao;
    private final TransactionEntityDao transactionEntityDao;
    private final PassBookEntityDao passBookEntityDao;
    private final SupervisorEntityDao supervisorEntityDao;
    private final DriverEntityDao driverEntityDao;

    @Override
    public VendorAttendanceEntity enterAttendance(CreateVendorAttendanceRequestDTO createVendorAttendanceRequestDTO, String userId) {

        //Marking the attendance
        VendorAttendanceEntity vendorAttendanceEntity = createVendorAttendanceEntity(createVendorAttendanceRequestDTO, userId);
        vendorAttendanceEntityDao.enterAttendance(vendorAttendanceEntity);
        log.info("Attendance for: {} entered successfully by: {}", vendorAttendanceEntity.getAttendanceDate(), vendorAttendanceEntity.getEnteredBy());
        if(createVendorAttendanceRequestDTO.isMakeTransaction()){
            VendorEntity vendorEntity = vendorEntityDao.getVendor(createVendorAttendanceRequestDTO.getVendorId()).orElseThrow(() -> {
                log.error("Error while making transaction based on attendance");
                return new RuntimeException(TRANSACTION_ERROR);
            });
            Map<CommodityType, Integer> vendorCommodityCosts = vendorEntity.getCommodityCosts();
            Map<CommodityType, Integer> vendorCommodityAttendance = createVendorAttendanceRequestDTO.getCommodityAttendance();
            AtomicLong vendorPaymentAmount = new AtomicLong(0);

            //Calculating all the commodity costs entered as a part of attendance.
            vendorCommodityAttendance.forEach((commodity, units) -> vendorPaymentAmount.addAndGet((long) vendorCommodityCosts.get(commodity) * units));

            //Making a transaction now.
            TransactionEntity transactionEntity = getTransactionEntityForAttendance(createVendorAttendanceRequestDTO.getEnteredBy(),
                    createVendorAttendanceRequestDTO.getVendorId(), vendorPaymentAmount.get(),
                    createVendorAttendanceRequestDTO.getAttendanceDate(), createVendorAttendanceRequestDTO.getBankAccount(), userId);
            transactionEntityDao.saveTransaction(transactionEntity);
        }
        return vendorAttendanceEntity;
    }

    @Override
    public List<CreateVendorAttendanceRequestDTO> getAllVendorAttendance() {
        List<VendorAttendanceEntity> vendorAttendanceEntities = vendorAttendanceEntityDao.getAllVendorAttendance();
        List<CreateVendorAttendanceRequestDTO> vendorAttendanceRequestDTOS = new ArrayList<>();
        vendorAttendanceEntities.forEach(vendorAttendanceEntity -> {
            vendorAttendanceRequestDTOS.add(CreateVendorAttendanceRequestDTO.builder()
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
}
