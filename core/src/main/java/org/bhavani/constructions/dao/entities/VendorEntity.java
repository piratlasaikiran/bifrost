package org.bhavani.constructions.dao.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bhavani.constructions.dao.entities.models.VendorPurposeEnum;
import org.bhavani.constructions.dao.entities.subentities.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class VendorEntity extends BaseEntity {

    @Id
    @Column(name = "vendor_id")
    private String vendorId;

}
