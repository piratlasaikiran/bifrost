package org.bhavani.constructions.dao.api;

import org.bhavani.constructions.dao.entities.DriverEntity;

import java.util.List;
import java.util.Optional;

public interface DriverEntityDao {

    Optional<DriverEntity> getDriver(String driverName);

    void saveDriver(DriverEntity driver);

    void deleteDriver(DriverEntity driver);

    List<DriverEntity> getDrivers();
}
