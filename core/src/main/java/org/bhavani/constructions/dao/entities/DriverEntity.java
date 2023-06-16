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
        @NamedQuery(name = "GetDriverByName",
                    query = "select e from DriverEntity e where e.name = :name"),
        @NamedQuery(name = "GetAllDrivers",
                    query = "select e from DriverEntity e")
})
@Table(name = "drivers")
public class DriverEntity extends Employee {

    @Id
    @Column(name = "id")
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ot_pay_day")
    private int otPayDay;

    @Column(name = "ot_pay_day_night")
    private int otPayDayNight;

    @Lob
    @Column(name = "license")
    private byte[] license;
}
