package com.example.grouperapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.*;
import java.io.Serializable;

@Data @NoArgsConstructor @ToString @AllArgsConstructor
public class RegistrationDTO implements Serializable {
    @NotNull(message = "username can't be null")
    @Size(min = 3, max = 32, message = "username must be between 3 and 32 characters long")
    private String username;
    @NotNull(message = "email can'tbe null")
    @Email(message = "invalid email")
    private String email;
// We would like our password to contain all of the following
//   * At least one digit [0-9]
//   * At least one lowercase character [a-z]
//   * At least one uppercase character [A-Z]
//   * At least one special character [*.!@#$%^&(){}[]:;
//    regex clean = ^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[*.!@$%^&(){}[]:;<>,.?/~_+-=|\]).{8,32}$
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,32}$",
            message = "Password must be between 8 and 32 characters," +
                    "must contain at least 1 capital letter, 1 digit and 1 non-alphanumeric character")
    private String password;
    private String confirmPassword;
    @AssertTrue(message = "user has not agreed to the terms of service")
    Boolean hasAgreed;
}
