package io.ruv.proto.user.api.courier;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CourierUpdateDto {

    private String email;
    private String password;
    private String repeatPassword;
}
