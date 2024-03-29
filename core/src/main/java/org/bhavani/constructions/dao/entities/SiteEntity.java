package org.bhavani.constructions.dao.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bhavani.constructions.dao.entities.models.SiteStatus;
import org.bhavani.constructions.dao.entities.subentities.BaseEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.bhavani.constructions.constants.Constants.STRING_JOIN_DELIMITER;
import static org.jadira.usertype.spi.utils.lang.StringUtils.isEmpty;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@NamedQueries(value = {
        @NamedQuery(name = "GetSiteByName",
                query = "select S from SiteEntity S where S.siteName = :site_name"),
        @NamedQuery(name = "GetAllSites",
                query = "select S from SiteEntity S"),
        @NamedQuery(name = "GetSitesUnderSupervisor",
                query = "SELECT S FROM SiteEntity S WHERE S.supervisors LIKE CONCAT('%', :employee_name, '%')"),
        @NamedQuery(name = "GetSitesWithVehicle",
                query = "SELECT S FROM SiteEntity S WHERE S.vehicles LIKE CONCAT('%', :vehicle_num, '%')")
})
@Table(name = "sites")
public class SiteEntity extends BaseEntity {

    @Id
    @Column(name = "id")
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "site_name")
    private String siteName;

    @Column(name = "address")
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "current_status")
    private SiteStatus currentStatus;

    @Column(name = "supervisors")
    private String supervisors;

    @Column(name = "vehicles")
    private String vehicles;

    @Column(name = "work_start")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate workStartDate;

    @Column(name = "work_end")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate workEndDate;

    public List<String> getSupervisors() {
        return getSupervisorsList();
    }

    public List<String> getVehicles() {
        return getVehiclesList();
    }

    private List<String> getSupervisorsList() {
        if (isEmpty(this.supervisors)) {
            return new ArrayList<>();
        }
        return stream(this.supervisors.split(STRING_JOIN_DELIMITER)).collect(Collectors.toList());
    }

    private List<String> getVehiclesList() {
        if (isEmpty(this.vehicles)) {
            return new ArrayList<>();
        }
        return stream(this.vehicles.split(STRING_JOIN_DELIMITER)).collect(Collectors.toList());
    }
}
