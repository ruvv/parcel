package io.ruv.parcel.messaging.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ParcelApprovalDto {

    private long parcelId;
    private boolean success;
    private String comment;
}
