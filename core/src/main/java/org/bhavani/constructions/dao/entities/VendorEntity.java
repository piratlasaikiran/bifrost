package org.bhavani.constructions.dao.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
@TypeDef(name = "json", typeClass = JsonStringType.class)
@NamedQueries(value = {
        @NamedQuery(name = "GetVendorById",
                query = "select V from VendorEntity V where V.vendorId = :vendor_id"),
        @NamedQuery(name = "GetVendorByName",
                query = "select V from VendorEntity V where V.name = :vendor_name"),
        @NamedQuery(name = "GetAllVendors",
                query = "select V from VendorEntity V"),
        @NamedQuery(name = "GetAllVendorsInSite",
                query = "select V from VendorEntity V where V.location = :site_name"),
})
@Table(name = "vendors")
public class VendorEntity extends BaseEntity {

    @Id
    @Column(name = "id")
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "vendor_id")
    private String vendorId;

    @Column(name = "mobile_number")
    private Long mobileNumber;

    @Lob
    @Column(name = "contract_doc")
    private byte[] contractDocument;

    @Column(name = "location")
    private String location;

    @Column(name = "purposes")
    private String purposes;

    @Type(type = "json")
    @Column(name = "commodity_costs")
    private Map<CommodityType, Integer> commodityCosts;

    public List<VendorPurpose> getPurposes() {
        return getPurposesList();
    }

    private List<VendorPurpose> getPurposesList() {
        if (isEmpty(this.purposes)) {
            return new ArrayList<>();
        }
        return stream(this.purposes.split(STRING_JOIN_DELIMITER)).map(VendorPurpose::valueOf).collect(Collectors.toList());
    }

}
