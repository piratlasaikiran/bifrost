package org.bhavani.constructions.dao.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bhavani.constructions.dao.entities.subentities.BaseEntity;

import javax.persistence.*;
import java.time.LocalDate;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@NamedQueries(value = {
        @NamedQuery(name = "GetAssetOwnershipsByName",
                query = "select A from AssetOwnershipEntity A where A.assetName = :asset_name"),
        @NamedQuery(name = "GetAllAssetOwnerships",
                query = "select A from AssetOwnershipEntity A"),
        @NamedQuery(name = "GetAssetOwnershipsByOwner",
                query = "select A from AssetOwnershipEntity A where A.currentOwner = :employee_name"),
})
@Table(name = "entity_ownership")
public class AssetOwnershipEntity extends BaseEntity {

    @Id
    @Column(name = "id")
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "asset_type")
    private String assetType;

    @Column(name = "asset_name")
    private String assetName;

    @Column(name = "cur_owner")
    private String currentOwner;

    @Column(name = "start_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Column(name = "end_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;
}
