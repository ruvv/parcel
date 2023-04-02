package io.ruv.parcel.parcelservice.service.workflow;

import io.ruv.parcel.parcel.api.ParcelStatus;
import io.ruv.parcel.parcelservice.entity.Parcel;
import io.ruv.parcel.user.api.auth.UserInfo;
import jakarta.annotation.Nullable;

public interface ParcelWorkflowService {

    Parcel processCreatedParcel(Parcel parcel, UserInfo user);

    Parcel processUpdatedParcel(Parcel parcel, UserInfo user);

    Parcel changeParcelStatus(Parcel parcel, ParcelStatus newStatus,
                              @Nullable UserInfo user, @Nullable String assignee);
}
