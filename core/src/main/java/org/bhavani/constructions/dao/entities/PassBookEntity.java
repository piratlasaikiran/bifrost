package org.bhavani.constructions.dao.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bhavani.constructions.dao.entities.models.AccountType;
import org.bhavani.constructions.dao.entities.models.TransactionType;
import org.bhavani.constructions.dao.entities.subentities.BaseEntity;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@TypeDef(name = "json", typeClass = JsonStringType.class)
@NamedQueries(value = {
        @NamedQuery(name = "GetAccountPassBook",
                query = "SELECT P from PassBookEntity P WHERE P.accountName = :account_name ORDER BY P.updatedAt DESC"),
        @NamedQuery(name = "GetLatestPassBookEntryForAllAccounts",
                query = "SELECT P FROM PassBookEntity P " +
                        "WHERE (P.accountName, P.updatedAt) IN " +
                        "(SELECT P2.accountName, MAX(P2.updatedAt) FROM PassBookEntity P2 " +
                        "GROUP BY P2.accountName)")
})
@Table(name = "passbook")
public class PassBookEntity extends BaseEntity {

    @Id
    @Column(name = "id")
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_name")
    private String accountName;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type")
    private AccountType accountType;

    @ManyToOne
    @JoinColumn(name = "transaction_id", referencedColumnName = "id")
    private TransactionEntity transactionEntity;

    @Column(name = "current_balance")
    private Long currentBalance;

    @Column(name = "transaction_amount")
    private Long transactionAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TransactionType transactionType;
}
