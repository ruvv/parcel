package io.ruv.proto.user.api.customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CustomerRegistrationDto {

    private String username;
    private String email;
    private String password;
    private String repeatPassword;
}
