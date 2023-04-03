package io.ruv.parcel.parcelservice.service.remote;

import io.ruv.parcel.parcel.api.ParcelStatus;
import io.ruv.parcel.parcelservice.entity.Parcel;
import io.ruv.parcel.user.api.auth.UserInfo;
import jakarta.annotation.Nullable;

public interface WorkflowEventSender {

    void sendWorkflowEvent(Parcel parcel, @Nullable UserInfo userInfo,
                           @Nullable ParcelStatus previousStatus, @Nullable String previousAssignee);
}
