package io.ruv.userservice.api.courier;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
public class CourierCreateDto {

    @NotEmpty(message = "Username is mandatory")
    @NotBlank(message = "Username can not be blank")
    private String username;
    @NotEmpty(message = "Email is mandatory")
    @Pattern(regexp = "^(.+)@(\\S+)$", message = "Illegal email format")
    private String email;
    @NotEmpty(message = "Password is mandatory")
    @Size(min = 3, message = "Password is too short")
    private String password;
}
