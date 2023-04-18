package org.bhavani.constructions.dao.api;

import org.bhavani.constructions.dao.entities.DriverEntity;

import java.util.Optional;

public interface DriverEntityDao {

    Optional<DriverEntity> getEmployee(String userName);

    void saveEmployee(DriverEntity driver);

    void updateEmployee(DriverEntity driver);

    void deleteEmployee(DriverEntity driver);
}
