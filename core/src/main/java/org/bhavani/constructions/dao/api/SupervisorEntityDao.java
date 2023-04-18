package org.bhavani.constructions.dao.api;

import org.bhavani.constructions.dao.entities.SupervisorEntity;

import java.util.Optional;

public interface SupervisorEntityDao {
    Optional<SupervisorEntity> getSupervisor(String supervisorName);

    void saveSupervisor(SupervisorEntity supervisor);

    void updateSupervisor(SupervisorEntity supervisor);

    void deleteSupervisor(SupervisorEntity supervisor);
}
