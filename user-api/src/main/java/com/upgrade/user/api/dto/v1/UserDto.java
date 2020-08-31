package com.upgrade.user.api.dto.v1;

import java.io.Serializable;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto implements Serializable {
    private String userId;
    @NotNull
    @Email
    private String email;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
}
