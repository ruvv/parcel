package io.ruv.parcel.parcelservice.service;

import io.ruv.parcel.parcel.api.ParcelCreateDto;
import io.ruv.parcel.parcel.api.ParcelDto;
import io.ruv.parcel.parcel.api.ParcelStatus;
import io.ruv.parcel.parcel.api.ParcelUpdateDto;
import io.ruv.parcel.user.api.auth.UserInfo;
import jakarta.annotation.Nullable;

import java.util.List;

public interface ParcelService {

    ParcelDto getParcel(long id, UserInfo userInfo);

    List<ParcelDto> findParcels(int page, int size, @Nullable String createdBy, @Nullable String assignedTo);

    ParcelDto createParcel(ParcelCreateDto createDto, UserInfo userInfo);

    ParcelDto updateParcel(long id, ParcelUpdateDto updateDto, UserInfo userInfo);

    ParcelDto updateParcelStatus(long id, ParcelStatus status, @Nullable UserInfo userInfo);

    ParcelDto updateAssignee(long id, String assigneeUsername, UserInfo userInfo);
}
