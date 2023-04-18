package org.bhavani.constructions.dao.api;

import org.bhavani.constructions.dao.entities.SupervisorEntity;
import org.bhavani.constructions.dao.entities.subentities.Employee;

import java.util.Optional;

public interface SupervisorEntityDao {
    Optional<SupervisorEntity> getEmployee(String userName);

    void saveEmployee(SupervisorEntity supervisor);

    void updateEmployee(SupervisorEntity supervisor);

    void deleteEmployee(SupervisorEntity supervisor);
}
