package io.ruv.userservice.api.customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceChangeDto {

    private Integer balanceDelta;
    private Integer lockedDelta;
}
