package io.ruv.userservice.api.courier;

import io.ruv.userservice.api.CourierStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CourierDto {
    private String username;
    private String email;
    private CourierStatus courierStatus;
    private String latitude;
    private String longitude;
}
