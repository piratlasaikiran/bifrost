package org.bhavani.constructions.dao.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bhavani.constructions.dao.entities.subentities.BaseEntity;

import javax.persistence.*;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@NamedQueries(value = {
        @NamedQuery(name = "GetPasswordByName",
                query = "select U from UserEntity U where U.userName = :user_name")
})
@Table(name = "users")
public class UserEntity extends BaseEntity {

    @Id
    @Column(name = "name")
    private String userName;

    @Column(name = "password")
    private String password;

}
