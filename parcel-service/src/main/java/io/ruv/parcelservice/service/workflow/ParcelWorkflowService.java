package io.ruv.parcelservice.service.workflow;

import io.ruv.parcelservice.api.ParcelStatus;
import io.ruv.parcelservice.api.UserInfo;
import io.ruv.parcelservice.entity.Parcel;
import jakarta.annotation.Nullable;

public interface ParcelWorkflowService {

    Parcel processCreatedParcel(Parcel parcel, UserInfo user);

    Parcel processUpdatedParcel(Parcel parcel, UserInfo user);

    Parcel changeParcelStatus(Parcel parcel, ParcelStatus newStatus, UserInfo user, @Nullable String assignee);
}
