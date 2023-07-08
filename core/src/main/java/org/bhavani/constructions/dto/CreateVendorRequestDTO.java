package org.bhavani.constructions.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bhavani.constructions.dao.entities.models.CommodityType;
import org.bhavani.constructions.dao.entities.models.VendorPurpose;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateVendorRequestDTO {

    @JsonProperty("vendor_id")
    private String vendorId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("mobile_number")
    private Long mobileNumber;

    @JsonProperty("purposes")
    private List<VendorPurpose> purposes;

    @JsonProperty("location")
    private String location;

    @JsonProperty("commodity_costs")
    private Map<CommodityType, Integer> commodityCosts;

}
