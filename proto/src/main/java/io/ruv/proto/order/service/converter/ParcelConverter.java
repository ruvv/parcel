package io.ruv.proto.order.service.converter;

import io.ruv.proto.order.api.ParcelCreateDto;
import io.ruv.proto.order.api.ParcelDto;
import io.ruv.proto.order.api.ParcelStatus;
import io.ruv.proto.order.api.ParcelUpdateDto;
import io.ruv.proto.order.entity.Parcel;
import org.springframework.stereotype.Service;

@Service
public class ParcelConverter {

    public Parcel toEntity(ParcelCreateDto dto) {

        return new Parcel(null,
                ParcelStatus.CREATED,
                dto.getSource(),
                dto.getDestination(),
                dto.getDescription(),
                null,
                null,
                null,
                null);
    }

    public Parcel updateEntity(Parcel entity, ParcelUpdateDto dto) {

        if (dto.getSource() != null) {
            entity.setSource(dto.getSource());
        }
        if (dto.getDestination() != null) {
            entity.setDestination(dto.getDestination());
        }
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }

        return entity;
    }

    public ParcelDto toDto(Parcel entity) {

        return new ParcelDto(
                entity.getId(),
                entity.getStatus(),
                entity.getSource(),
                entity.getDestination(),
                entity.getDescription(),
                entity.getCreatedBy(),
                entity.getAssignedTo(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}
