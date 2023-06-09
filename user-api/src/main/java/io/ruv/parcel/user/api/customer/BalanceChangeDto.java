package io.ruv.parcel.user.api.customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class BalanceChangeDto {

    private Integer balanceDelta;
    private Integer lockedDelta;
}
