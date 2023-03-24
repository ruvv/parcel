package io.ruv.proto.user.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.ruv.proto.user.api.courier.CourierStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserDto {

    private String username;
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int balance;
    private CourierStatus courierStatus;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<String> roles;
    private boolean enabled;
}
