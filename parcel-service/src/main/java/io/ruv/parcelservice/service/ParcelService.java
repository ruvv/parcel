package io.ruv.parcelservice.service;

import io.ruv.parcelservice.api.ParcelCreateDto;
import io.ruv.parcelservice.api.ParcelDto;
import io.ruv.parcelservice.api.ParcelStatus;
import io.ruv.parcelservice.api.ParcelUpdateDto;
import io.ruv.parcelservice.api.UserInfo;
import jakarta.annotation.Nullable;

import java.util.List;

public interface ParcelService {

    ParcelDto getParcel(long id, UserInfo userInfo);

    List<ParcelDto> findParcels(int page, int size, @Nullable String createdBy, @Nullable String assignedTo);

    ParcelDto createParcel(ParcelCreateDto createDto, UserInfo userInfo);

    ParcelDto updateParcel(long id, ParcelUpdateDto updateDto, UserInfo userInfo);

    ParcelDto updateParcelStatus(long id, ParcelStatus status, UserInfo userInfo);

    ParcelDto updateAssignee(long id, String assigneeUsername, UserInfo userInfo);
}
