package org.bhavani.constructions.services;

import org.bhavani.constructions.dao.entities.DriverEntity;
import org.bhavani.constructions.dto.CreateDriverRequestDTO;
import org.glassfish.jersey.media.multipart.FormDataParam;

import java.io.InputStream;

public interface DriverService {
    DriverEntity getEmployee(String employeeName);

    DriverEntity createEmployee(CreateDriverRequestDTO createDriverRequestDTO,
                                @FormDataParam("file") InputStream license, @FormDataParam("file") InputStream aadhar,
                                String userId);

    DriverEntity updateEmployee(CreateDriverRequestDTO createDriverRequestDTO,
                                    String userId);

    void deleteEmployee(String employeeName);

    DriverEntity createDriverResponse(DriverEntity driver);
}
