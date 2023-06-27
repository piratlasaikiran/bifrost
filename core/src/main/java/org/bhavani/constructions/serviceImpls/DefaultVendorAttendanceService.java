package org.bhavani.constructions.serviceImpls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.api.VendorAttendanceEntityDao;
import org.bhavani.constructions.dao.api.VendorEntityDao;
import org.bhavani.constructions.dao.entities.TransactionEntity;
import org.bhavani.constructions.dao.entities.VendorAttendanceEntity;
import org.bhavani.constructions.dao.entities.VendorEntity;
import org.bhavani.constructions.dao.entities.models.CommodityType;
import org.bhavani.constructions.dao.entities.models.TransactionMode;
import org.bhavani.constructions.dao.entities.models.TransactionPurpose;
import org.bhavani.constructions.dao.entities.models.TransactionStatus;
import org.bhavani.constructions.dto.CreateVendorAttendanceRequestDTO;
import org.bhavani.constructions.services.VendorAttendanceService;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static org.bhavani.constructions.constants.ErrorConstants.TRANSACTION_ERROR;
import static org.bhavani.constructions.utils.EntityBuilder.createVendorAttendanceEntity;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
@Slf4j
public class DefaultVendorAttendanceService implements VendorAttendanceService {

    private final VendorAttendanceEntityDao vendorAttendanceEntityDao;
    private final VendorEntityDao vendorEntityDao;

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
            TransactionEntity transactionEntity = TransactionEntity.builder()
                    .source(createVendorAttendanceRequestDTO.getEnteredBy())
                    .destination(createVendorAttendanceRequestDTO.getVendorId())
                    .amount(vendorPaymentAmount.get())
                    .purpose(TransactionPurpose.ATTENDANCE)
                    .remarks("Making automated payment by attendance marking")
                    .transactionDate(createVendorAttendanceRequestDTO.getAttendanceDate())
                    .status(TransactionStatus.SUBMITTED)
                    .mode(TransactionMode.CASH)
                    .build();

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
