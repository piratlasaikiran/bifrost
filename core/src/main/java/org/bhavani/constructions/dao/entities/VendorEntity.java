package org.bhavani.constructions.dao.entities;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bhavani.constructions.dao.entities.models.CommodityType;
import org.bhavani.constructions.dao.entities.models.VendorPurpose;
import org.bhavani.constructions.dao.entities.subentities.BaseEntity;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.Map;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@TypeDef(name = "json", typeClass = JsonStringType.class)
@NamedQueries(value = {
        @NamedQuery(name = "GetVendorById",
                query = "select V from VendorEntity V where V.vendorId = :vendor_id"),
        @NamedQuery(name = "GetAllVendors",
                query = "select V from VendorEntity V")
})
@Table(name = "vendors")
public class VendorEntity extends BaseEntity {

    @Id
    @Column(name = "vendor_id")
    private String vendorId;

    @Column(name = "mobile_number")
    private Long mobileNumber;

    @Lob
    @Column(name = "contract_doc")
    private byte[] contractDocument;

    @Column(name = "location")
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(name = "purpose")
    private VendorPurpose purpose;

    @Type(type = "json")
    @Column(name = "commodity_costs")
    private Map<CommodityType, Integer> commodityCosts;

}
