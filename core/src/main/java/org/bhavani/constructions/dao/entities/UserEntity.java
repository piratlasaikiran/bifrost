package org.bhavani.constructions.dao.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@NamedQueries( value = {
        @NamedQuery(name = "GetUserByName",
                    query = "select U from UserEntity U where U.name = :name")
            })
@Table(name = "users")
public class UserEntity {
    @Id
    @Column(name = "name")
    private String name;

    @Column(name = "mobile_num")
    private String mobileNumber;

    @Column(name = "admin")
    private boolean admin;

}
