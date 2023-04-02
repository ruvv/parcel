package io.ruv.parcel.user.api.courier;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Location {

    @NotEmpty(message = "Latitude is mandatory")
    @NotBlank(message = "Latitude can not be blank")
    private String latitude;
    @NotEmpty(message = "Longitude is mandatory")
    @NotBlank(message = "Longitude can not be blank")
    private String longitude;
}
