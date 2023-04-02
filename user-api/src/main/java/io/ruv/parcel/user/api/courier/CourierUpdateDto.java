package io.ruv.parcel.user.api.courier;

import io.ruv.parcel.user.api.NullOrNotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CourierUpdateDto {

    @NullOrNotEmpty(message = "Email is mandatory")
    @Pattern(regexp = "^(.+)@(\\S+)$", message = "Illegal email format")
    private String email;
    @NullOrNotEmpty(message = "Password is mandatory")
    @Size(min = 3, message = "Password is too short")
    private String password;
}
