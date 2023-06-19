package org.bhavani.constructions.serviceImpls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.api.BankAccountEntityDao;
import org.bhavani.constructions.dao.entities.BankAccountEntity;
import org.bhavani.constructions.dto.CreateBankAccountRequestDTO;
import org.bhavani.constructions.services.BankAccountService;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.bhavani.constructions.utils.EntityBuilder.createBankAccountEntity;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
@Slf4j
public class DefaultBankAccountService implements BankAccountService {

    private final BankAccountEntityDao bankAccountEntityDao;

    @Override
    public BankAccountEntity createBankAccount(CreateBankAccountRequestDTO createBankAccountRequestDTO, String userId) {
        BankAccountEntity bankAccountEntity = createBankAccountEntity(createBankAccountRequestDTO, userId);
        bankAccountEntityDao.saveBankAccount(bankAccountEntity);
        log.info("Account: {} saved successfully", bankAccountEntity.getNickName());
        return bankAccountEntity;
    }

    @Override
    public List<CreateBankAccountRequestDTO> getBankAccounts() {
        List<BankAccountEntity> bankAccountEntities = getBankAccountEntities();
        List<CreateBankAccountRequestDTO> bankAccountRequestDTOS = new ArrayList<>();
        bankAccountEntities.forEach(bankAccountEntity -> bankAccountRequestDTOS.add(
                CreateBankAccountRequestDTO.builder()
                        .nickName(bankAccountEntity.getNickName())
                        .accountHolders(bankAccountEntity.getAccountHolders())
                        .bankName(bankAccountEntity.getBankName())
                        .accountNumber(bankAccountEntity.getAccountNumber())
                        .currentBalance(bankAccountEntity.getCurrentBalance())
                        .atmCard(bankAccountEntity.getAtmCard())
                        .build()
        ));
        return bankAccountRequestDTOS;
    }

    @Override
    public List<Long> getATMCards() {
        List<BankAccountEntity> bankAccountEntities = getBankAccountEntities();
        return bankAccountEntities.stream().map(BankAccountEntity::getAtmCard).collect(Collectors.toList());
    }

    private List<BankAccountEntity> getBankAccountEntities() {
        return bankAccountEntityDao.getBankAccounts();
    }
}
