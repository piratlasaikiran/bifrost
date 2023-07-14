package org.bhavani.constructions.dao.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
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
        @NamedQuery(name = "GetLatestPendingBalanceForAccountName",
                query = "SELECT P from PendingBalanceEntity P WHERE P.accountName = :account_name ORDER BY P.createdAt DESC"),
        @NamedQuery(name = "GetAllPendingBalanceForAccount",
                query = "SELECT P from PendingBalanceEntity P WHERE P.accountName = :account_name"),
        @NamedQuery(name = "GetLatestPendingBalancesForAllAccounts",
                query = "SELECT P FROM PendingBalanceEntity P " +
                        "WHERE (P.accountName, P.createdAt) IN " +
                        "(SELECT P2.accountName, MAX(P2.createdAt) FROM PendingBalanceEntity P2 " +
                        "GROUP BY P2.accountName)"),
})
@Table(name = "pending_balance")
public class PendingBalanceEntity extends BaseEntity {

    @Id
    @Column(name = "id")
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_name")
    private String accountName;

    @ManyToOne
    @JoinColumn(name = "transaction_id", referencedColumnName = "id")
    private TransactionEntity transactionEntity;

    @Column(name = "pending_balance")
    private Long pendingBalance;

}
