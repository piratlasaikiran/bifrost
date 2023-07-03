package org.bhavani.constructions.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.entities.PassBookEntity;
import org.bhavani.constructions.dao.entities.TransactionEntity;
import org.bhavani.constructions.dao.entities.models.AccountType;
import org.bhavani.constructions.dao.entities.models.TransactionType;

import javax.inject.Inject;
import java.util.*;

import static org.bhavani.constructions.constants.Constants.MY_ACCOUNT;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
@Slf4j
public class PassBookHelper {

    public static List<PassBookEntity> createPassBookEntities(TransactionEntity transactionEntity, Set<String> employees,
                                                       Long sourcePreviousBalance, Long destinationPreviousBalance) {
        List<PassBookEntity> passBookEntities = new ArrayList<>();
        Optional<PassBookEntity> destinationPassBookEntity = getDestinationPassBookEntity(transactionEntity, employees, destinationPreviousBalance);
        Optional<PassBookEntity> sourcePassBookEntity = getSourcePassBookEntity(transactionEntity, sourcePreviousBalance);

        destinationPassBookEntity.ifPresent(passBookEntities::add);
        sourcePassBookEntity.ifPresent(passBookEntities::add);
        return passBookEntities;
    }

    private static Optional<PassBookEntity> getDestinationPassBookEntity(TransactionEntity transactionEntity, Set<String> employees, Long previousBalance) {
        String destination = transactionEntity.getDestination();
        if(!employees.contains(destination)){
            return Optional.empty();
        }
        return Optional.ofNullable(PassBookEntity.builder()
                        .accountName(destination)
                        .accountType(AccountType.PERSONAL)
                        .transactionEntity(transactionEntity)
                        .currentBalance(previousBalance + transactionEntity.getAmount())
                        .transactionAmount(transactionEntity.getAmount())
                        .transactionType(TransactionType.CREDIT)
                        .createdBy(transactionEntity.getCreatedBy())
                        .updatedBy(transactionEntity.getUpdatedBy())
                        .build());
    }

    private static Optional<PassBookEntity> getSourcePassBookEntity(TransactionEntity transactionEntity, Long previousBalance) {
        if(Objects.isNull(transactionEntity.getBankAccount()) || transactionEntity.getBankAccount().isEmpty()){
            return Optional.empty();
        }
        PassBookEntity sourcePassBookEntity = PassBookEntity.builder()
                .transactionEntity(transactionEntity)
                .transactionAmount(transactionEntity.getAmount())
                .currentBalance(previousBalance - transactionEntity.getAmount())
                .transactionType(TransactionType.DEBIT)
                .createdBy(transactionEntity.getCreatedBy())
                .updatedBy(transactionEntity.getUpdatedBy())
                .build();
        if(transactionEntity.getBankAccount().equals(MY_ACCOUNT)){
            sourcePassBookEntity.setAccountName(transactionEntity.getSource());
            sourcePassBookEntity.setAccountType(AccountType.PERSONAL);
        }
        else{
            sourcePassBookEntity.setAccountName(transactionEntity.getBankAccount());
            sourcePassBookEntity.setAccountType(AccountType.ORGANIZATION);
        }
        return Optional.of(sourcePassBookEntity);
    }
}
