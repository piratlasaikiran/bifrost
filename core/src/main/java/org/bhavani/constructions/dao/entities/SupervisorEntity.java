package org.bhavani.constructions.dao.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bhavani.constructions.dao.entities.subentities.Employee;

import javax.persistence.*;


@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@NamedQueries(value = {
        @NamedQuery(name = "GetSupervisorByName",
                query = "select e from SupervisorEntity e where e.name = :name")
})
@Table(name = "supervisors")
public class SupervisorEntity extends Employee {

    @Id
    @Column(name = "id")
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_mob_num")
    private Long companyMobileNumber;

    @Column(name = "atm_card")
    private Long atmCardNumber;

    @Column(name = "vehicle_num")
    private String vehicleNumber;

    @Column(name = "ot_pay")
    private int otPay;
}
