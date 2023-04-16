package org.bhavani.constructions.dao.entities.subentities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * Base entity. Use this <b>only</b> if you need <code>created_at</code> and <code>updated_at</code> fields.
 * <p>
 * This class is used in reflective access. So, don't change path.
 */
@MappedSuperclass
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(name = "json", typeClass = JsonStringType.class)
public abstract class BaseEntity {

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @JsonIgnore
    @Column(name = "created_at", insertable = false, updatable = false)
    protected LocalDateTime createdAt;

    @JsonIgnore
    @Column(name = "updated_at", insertable = false, updatable = false)
    protected LocalDateTime updatedAt;
}
