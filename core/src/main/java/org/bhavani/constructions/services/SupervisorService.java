package org.bhavani.constructions.services;

import org.bhavani.constructions.dao.entities.SupervisorEntity;
import org.bhavani.constructions.dto.CreateSupervisorRequestDTO;

public interface SupervisorService {
    SupervisorEntity getEmployee(String employeeName);

    SupervisorEntity createEmployee(CreateSupervisorRequestDTO createSupervisorRequestDTO,
                                    String userId);

    SupervisorEntity updateEmployee(CreateSupervisorRequestDTO createSupervisorRequestDTO,
                                    String userId);

    void deleteEmployee(String employeeName);
}
