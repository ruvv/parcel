package io.ruv.parcel.parcelservice.entity;

import io.ruv.parcel.parcel.api.ParcelStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.Objects;

@Entity(name = "parcels")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString
@AllArgsConstructor()
@NoArgsConstructor
@Accessors(chain = true)
public class Parcel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "parcel_id_sequence")
    @SequenceGenerator(name = "parcel_id_sequence", allocationSize = 16)
    private Long id;
    @Enumerated(EnumType.STRING)
    private ParcelStatus status;

    private String source;
    private String destination;
    private String description;

    private Integer cost;

    private String createdBy;
    private String assignedTo;

    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Parcel parcel = (Parcel) o;
        return getId() != null && Objects.equals(getId(), parcel.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
