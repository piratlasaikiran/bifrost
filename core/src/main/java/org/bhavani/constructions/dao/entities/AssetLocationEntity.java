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
        @NamedQuery(name = "GetAssetLocationByName",
                query = "select A from AssetLocationEntity A where A.assetName = :asset_name"),
        @NamedQuery(name = "GetAssetLocationByName.Location.Date",
                query = "select A from AssetLocationEntity A where A.assetName = :asset_name and " +
                        "A.location = :asset_location and A.startDate = :start_date"),
        @NamedQuery(name = "GetAllAssets",
                query = "select A from AssetLocationEntity A")
})
@Table(name = "entity_location")
public class AssetLocationEntity extends BaseEntity {
    @Id
    @Column(name = "id")
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "asset_type")
    private String assetType;

    @Column(name = "asset_name")
    private String assetName;

    @Column(name = "cur_location")
    private String location;

    @Column(name = "start_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Column(name = "end_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;
}
