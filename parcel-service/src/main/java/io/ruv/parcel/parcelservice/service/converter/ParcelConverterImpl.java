package io.ruv.parcel.parcelservice.service.converter;

import io.ruv.parcel.parcel.api.ParcelCreateDto;
import io.ruv.parcel.parcel.api.ParcelDto;
import io.ruv.parcel.parcel.api.ParcelStatus;
import io.ruv.parcel.parcel.api.ParcelUpdateDto;
import io.ruv.parcel.parcelservice.entity.Parcel;
import org.springframework.stereotype.Service;

@Service
public class ParcelConverterImpl implements ParcelConverter {

    @Override
    public Parcel entity(ParcelCreateDto dto) {

        return new Parcel(null,
                ParcelStatus.CREATED,
                dto.getSource(),
                dto.getDestination(),
                dto.getDescription(),
                1,
                null,
                null,
                null,
                null);
    }

    @Override
    public Parcel update(Parcel entity, ParcelUpdateDto dto) {

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

    @Override
    public ParcelDto dto(Parcel entity) {

        return new ParcelDto(
                entity.getId(),
                entity.getStatus(),
                entity.getSource(),
                entity.getDestination(),
                entity.getDescription(),
                entity.getCost(),
                entity.getCreatedBy(),
                entity.getAssignedTo(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}
