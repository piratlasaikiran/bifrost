package org.bhavani.constructions.dao.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bhavani.constructions.dao.entities.subentities.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.bhavani.constructions.constants.Constants.STRING_JOIN_DELIMITER;
import static org.jadira.usertype.spi.utils.lang.StringUtils.isEmpty;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@NamedQueries(value = {
        @NamedQuery(name = "GetAllBankAccounts",
                query = "select B from BankAccountEntity B")
})
@Table(name = "bank_accounts")
public class BankAccountEntity extends BaseEntity {

    @Id
    @Column(name = "nick_name")
    private String nickName;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "account_holders")
    private String accountHolders;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "current_balance")
    private Long currentBalance;

    @Column(name = "atm_card")
    private Long atmCard;

    public List<String> getAccountHolders() {
        return getAccountHoldersList();
    }

    private List<String> getAccountHoldersList() {
        if (isEmpty(this.accountHolders)) {
            return new ArrayList<>();
        }
        return stream(this.accountHolders.split(STRING_JOIN_DELIMITER)).collect(Collectors.toList());
    }
}
