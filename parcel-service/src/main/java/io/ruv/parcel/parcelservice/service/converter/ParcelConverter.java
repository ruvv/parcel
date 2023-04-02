package io.ruv.parcel.parcelservice.service.converter;

import io.ruv.parcel.parcel.api.ParcelCreateDto;
import io.ruv.parcel.parcel.api.ParcelDto;
import io.ruv.parcel.parcel.api.ParcelUpdateDto;
import io.ruv.parcel.parcelservice.entity.Parcel;

public interface ParcelConverter {

    Parcel entity(ParcelCreateDto dto);

    Parcel update(Parcel entity, ParcelUpdateDto dto);

    ParcelDto dto(Parcel entity);
}
