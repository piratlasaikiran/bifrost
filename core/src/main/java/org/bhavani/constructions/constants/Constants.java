package org.bhavani.constructions.constants;

import org.bhavani.constructions.dao.entities.models.CommodityType;

import java.util.HashMap;
import java.util.Map;

import static org.bhavani.constructions.dao.entities.models.CommodityType.*;

public class Constants {
    public static final String NAME = "name";
    public static final String X_USER_ID = "X-User-Id";
    public static final String VEHICLE_NUMBER = "vehicle_num";
    public static final String TAX_TYPE = "tax_type";
    public static final String START_DATE = "start_date";
    public static final String SITE_NAME = "site_name";
    public static final String ASSET_NAME = "asset_name";
    public static final String USER_NAME = "user_name";
    public static final String ASSET_LOCATION = "asset_location";
    public static final String STRING_JOIN_DELIMITER = ",";
    public static final String VENDOR_ID = "vendor_id";

    public static final String VEHICLE = "VEHICLE";
    public static final String EMPLOYEE = "EMPLOYEE";
    public static final String DRIVER = "DRIVER";

    public static final String VEHICLE_ADDED_SUCCESSFULLY = "Vehicle added successfully";
    public static final String VEHICLE_DELETED_SUCCESSFULLY = "Vehicle deleted successfully";
    public static final String TAX_DOC_ADDED_SUCCESSFULLY = "Tax document added successfully";
    public static final String TAX_DOC_DELETED_SUCCESSFULLY = "Tax document deleted successfully";
    public static final String ASSET_LOCATION_ADDED_SUCCESSFULLY = "Asset location added successfully";
    public static final String ASSET_LOCATION_DELETED_SUCCESSFULLY = "Asset location deleted successfully";

    public static final int MOBILE_NUMBER_LENGTH = 10;

    public static final Map<CommodityType, String> COMMODITY_BASE_UNITS = new HashMap<CommodityType, String>()
    {{
            put(MALE_WITHOUT_OT, "Per Day");
            put(MALE_WITH_OT, "Per Day");
            put(FEMALE_WITHOUT_OT, "Per Day");
            put(FEMALE_WITH_OT, "Per Day");
            put(SAND, "Per Ton");
            put(CEMENT, "Per Ton");
            put(IRON, "Per Ton");
            put(CONCRETE_CHIP, "Per Ton");
            put(TAR, "Per Barrel");
    }};

    public static final Map<CommodityType, String> COMMODITY_ATTENDANCE_UNITS = new HashMap<CommodityType, String>()
    {{
        put(MALE_WITHOUT_OT, "No.of Persons");
        put(MALE_WITH_OT, "No.of Persons");
        put(FEMALE_WITHOUT_OT, "No.of Persons");
        put(FEMALE_WITH_OT, "No.of Persons");
        put(SAND, "No.of Tons");
        put(CEMENT, "No.of Tons");
        put(IRON, "No.of Tons");
        put(CONCRETE_CHIP, "No.of Tons");
        put(TAR, "No.of Barrels");
    }};
}
