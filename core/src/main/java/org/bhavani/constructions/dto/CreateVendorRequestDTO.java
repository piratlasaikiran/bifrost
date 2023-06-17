package org.bhavani.constructions.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bhavani.constructions.dao.entities.models.CommodityType;
import org.bhavani.constructions.dao.entities.models.VendorPurpose;

import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateVendorRequestDTO {

    @JsonProperty("vendor_id")
    private String vendorId;

    @JsonProperty("mobile_number")
    private Long mobileNumber;

    @JsonProperty("purpose")
    private VendorPurpose purpose;

    @JsonProperty("location")
    private String location;

    @JsonProperty("commodity_costs")
    private Map<CommodityType, Integer> commodityCosts;

}
