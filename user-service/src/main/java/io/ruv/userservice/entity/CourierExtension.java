package io.ruv.userservice.entity;


import io.ruv.userservice.api.CourierStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;

import java.util.Objects;
import java.util.UUID;

@Entity(name = "courier_status")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CourierExtension {

    @Id
    @GeneratedValue
    private UUID id;
    @OneToOne(targetEntity = User.class, optional = false, mappedBy = "courierExtension")
    private User user;
    @Enumerated(EnumType.STRING)
    private CourierStatus status;
    private String latitude;
    private String longitude;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CourierExtension that = (CourierExtension) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
