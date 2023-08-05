package org.bhavani.constructions.services;

import org.bhavani.constructions.dao.entities.SupervisorEntity;
import org.bhavani.constructions.dto.CreateSupervisorRequestDTO;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import java.io.InputStream;
import java.util.List;

public interface SupervisorService {
    SupervisorEntity getSupervisor(String supervisorName);

    SupervisorEntity createSupervisor(CreateSupervisorRequestDTO createSupervisorRequestDTO,
                                      InputStream aadhar, FormDataContentDisposition aadharContent, String userId);

    SupervisorEntity updateSupervisor(CreateSupervisorRequestDTO createSupervisorRequestDTO,
                                      InputStream aadhar, FormDataContentDisposition aadharContent, String userId, String supervisorName);

    void deleteSuperVisor(String supervisorName);

    SupervisorEntity createSupervisorResponse(SupervisorEntity supervisor);

    List<CreateSupervisorRequestDTO> getSupervisors();
}
