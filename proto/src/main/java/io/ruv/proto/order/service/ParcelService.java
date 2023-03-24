package io.ruv.proto.order.service;

import io.ruv.proto.order.api.ParcelCreateDto;
import io.ruv.proto.order.api.ParcelDto;
import io.ruv.proto.order.api.ParcelStatus;
import io.ruv.proto.order.api.ParcelUpdateDto;
import jakarta.annotation.Nullable;

import java.util.List;

public interface ParcelService {

    List<ParcelDto> assignedTo(String courierUsername);

    List<ParcelDto> createdBy(String customerUsername);

    ParcelDto createParcel(String customer, ParcelCreateDto parcelCreateDto);

    ParcelDto updateParcel(long id, ParcelUpdateDto parcelUpdateDto);

    ParcelDto updateParcelStatus(long id, ParcelStatus status);

    List<ParcelDto> find(int page, int size,
                         @Nullable ParcelStatus parcelStatus,
                         @Nullable String customerUsername,
                         @Nullable String courierUsername);

    ParcelDto findById(long id);

    ParcelDto assignToCourier(long id, String courierUsername);

    ParcelDto unAssign(long id);
}
