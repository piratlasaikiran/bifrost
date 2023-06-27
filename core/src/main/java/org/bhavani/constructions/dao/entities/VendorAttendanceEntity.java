package org.bhavani.constructions.dao.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bhavani.constructions.dao.entities.models.CommodityType;
import org.bhavani.constructions.dao.entities.subentities.BaseEntity;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Map;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@TypeDef(name = "json", typeClass = JsonStringType.class)
@NamedQueries(value = {
        @NamedQuery(name = "GetAllVendorAttendances",
                query = "select V from VendorAttendanceEntity V")
})
@Table(name = "vendor_attendance")
public class VendorAttendanceEntity extends BaseEntity {

    @Id
    @Column(name = "id")
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "site")
    private String site;

    @Column(name = "vendor_id")
    private String vendorId;

    @Column(name = "entered_by")
    private String enteredBy;

    @Column(name = "attendance_date")
    private LocalDate attendanceDate;

    @Type(type = "json")
    @Column(name = "commodity_attendance")
    private Map<CommodityType, Integer> commodityAttendance;

    @Column(name = "make_transaction")
    private boolean makeTransaction;

    @Column(name = "bank_account")
    private String bankAccount;
}
