package org.bhavani.constructions.dao.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bhavani.constructions.dao.entities.models.TransactionMode;
import org.bhavani.constructions.dao.entities.models.TransactionStatus;
import org.bhavani.constructions.dao.entities.models.TransactionPurpose;
import org.bhavani.constructions.dao.entities.subentities.BaseEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@NamedQueries(value = {
        @NamedQuery(name = "GetAllTransactions",
                query = "select T from TransactionEntity T"),
        @NamedQuery(name = "GetTransactionsBySource",
                query = "select T from TransactionEntity T where T.source = :source"),
        @NamedQuery(name = "GetTransactionsByDestination",
                query = "select T from TransactionEntity T where T.destination = :destination")
})
@Table(name = "transactions")
public class TransactionEntity extends BaseEntity {

    @Id
    @Column(name = "id")
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source")
    private String source;

    @Column(name = "destination")
    private String destination;

    @Column(name = "amount")
    private Long amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "purpose")
    private TransactionPurpose purpose;

    @Column(name = "remarks")
    private String remarks;

    @Lob
    @Column(name = "bill")
    private byte[] bill;

    @Column(name = "transaction_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate transactionDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TransactionStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "mode")
    private TransactionMode mode;

    @Column(name = "bank_ac")
    private String bankAccount;

    @OneToMany(mappedBy = "transactionEntity", cascade = CascadeType.ALL)
    private List<PassBookEntity> passBookEntities;

    @OneToMany(mappedBy = "transactionEntity", cascade = CascadeType.ALL)
    private List<PendingBalanceEntity> pendingBalanceEntities;

}
