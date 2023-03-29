package io.ruv.parcelservice.repo;

import io.ruv.parcelservice.entity.Parcel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ParcelRepository extends JpaRepository<Parcel, Long> {

    @Query("select p from parcels p where p.id = :id and (p.createdBy = :username or p.assignedTo = :username)")
    Optional<Parcel> findByIdAndCreatedByOrAssignedTo(long id, String username);

    Optional<Parcel> findByIdAndCreatedBy(long id, String createdBy);

    Optional<Parcel> findByIdAndAssignedTo(long id, String assignedTo);
}
