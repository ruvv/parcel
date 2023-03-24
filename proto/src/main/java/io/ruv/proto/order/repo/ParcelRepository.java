package io.ruv.proto.order.repo;

import io.ruv.proto.order.entity.Parcel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParcelRepository extends JpaRepository<Parcel, Long> {

    List<Parcel> findByCreatedBy(String customerUsername);

    List<Parcel> findByAssignedTo(String courierUsername);
}
