package org.bhavani.constructions.dao.entities.subentities;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Embeddable
@MappedSuperclass

public class Employee extends BaseEntity{

    @Column(name = "name")
    private String name;

    @Column(name = "personal_mobile_num")
    private Long personalMobileNumber;

    @Column(name = "bank_ac")
    private String bankAccountNumber;

    @Column(name = "salary")
    private int salary;

    @Column(name = "admin")
    private boolean admin;

    @Lob
    @Column(name = "aadhar")
    private byte[] aadhar;

}
