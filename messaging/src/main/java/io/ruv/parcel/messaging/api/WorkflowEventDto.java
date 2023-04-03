package io.ruv.parcel.messaging.api;

import io.ruv.parcel.parcel.api.ParcelStatus;
import io.ruv.parcel.user.api.auth.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowEventDto {

    private long parcelId;
    private String createdBy;
    private int parcelCost;
    private ParcelStatus fromStatus;
    private ParcelStatus toStatus;
    private String fromAssignee;
    private String toAssignee;
    private UserInfo actor;
}
