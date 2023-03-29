package io.ruv.parcelservice.service.converter;

import io.ruv.parcelservice.api.ParcelCreateDto;
import io.ruv.parcelservice.api.ParcelDto;
import io.ruv.parcelservice.api.ParcelUpdateDto;
import io.ruv.parcelservice.entity.Parcel;

public interface ParcelConverter {

    Parcel entity(ParcelCreateDto dto);

    Parcel update(Parcel entity, ParcelUpdateDto dto);

    ParcelDto dto(Parcel entity);
}
