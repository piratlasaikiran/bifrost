package org.bhavani.constructions.utils;

import org.bhavani.constructions.dto.UploadVehicleTaxRequestDTO;

import java.io.InputStream;
import java.util.List;

public class VehicleHelper {
    public static void assignTaxReceiptsForVehicles(List<UploadVehicleTaxRequestDTO> uploadVehicleTaxRequestDTOS, InputStream puc, InputStream fitness, InputStream permit,
                                              InputStream insurance, InputStream tax, InputStream others){
        uploadVehicleTaxRequestDTOS.forEach(uploadVehicleTaxRequestDTO -> {
            switch (uploadVehicleTaxRequestDTO.getTaxType()){
                case PUC:
                    uploadVehicleTaxRequestDTO.setTaxReceipt(puc);
                    break;
                case TAX:
                    uploadVehicleTaxRequestDTO.setTaxReceipt(tax);
                    break;
                case FITNESS:
                    uploadVehicleTaxRequestDTO.setTaxReceipt(fitness);
                    break;
                case PERMIT:
                    uploadVehicleTaxRequestDTO.setTaxReceipt(permit);
                    break;
                case INSURANCE:
                    uploadVehicleTaxRequestDTO.setTaxReceipt(insurance);
                    break;
                case OTHERS:
                    uploadVehicleTaxRequestDTO.setTaxReceipt(others);
            }
        });
    }
}
