package org.bhavani.constructions.services;

import org.bhavani.constructions.dao.entities.DriverEntity;
import org.bhavani.constructions.dto.CreateDriverRequestDTO;

import java.io.InputStream;

public interface DriverService {
    DriverEntity getDriver(String driverName);

    DriverEntity createDriver(CreateDriverRequestDTO createDriverRequestDTO, InputStream license, InputStream aadhar,
                              String userId);


    void deleteDriver(String driverName);

    DriverEntity createDriverResponse(DriverEntity driverEntity);

    DriverEntity updateDriver(CreateDriverRequestDTO createDriverRequestDTO, InputStream license, InputStream aadhar, String userId);
}
