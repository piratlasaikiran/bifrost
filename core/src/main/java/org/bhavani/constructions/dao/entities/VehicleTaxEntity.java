package org.bhavani.constructions.dao.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.bhavani.constructions.dao.entities.models.VehicleTaxEnum;
import org.bhavani.constructions.dao.entities.subentities.BaseEntity;

import javax.persistence.*;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@NamedQueries(value = {
        @NamedQuery(name = "GetTaxEntryByDetails",
                query = "select T from VehicleTaxEntity T where T.vehicleNumber = :vehicle_num " +
                        "and T.taxType = :tax_type and T.validityStartDate = :start_date")
})
@Table(name = "vehicle_taxes")
public class VehicleTaxEntity extends BaseEntity {

    @Id
    @Column(name = "id")
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vehicle_num")
    private String vehicleNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "tax_type")
    private VehicleTaxEnum taxType;

    @Column(name = "tax_amount")
    private int amount;

    @Lob
    @Column(name = "tax_doc")
    private byte[] taxReceipt;

    @Column(name = "validity_start")
    private LocalDate validityStartDate;

    @Column(name = "validity_end")
    private LocalDate validityEndDate;
}
