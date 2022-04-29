package com.example.grouperapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Data @NoArgsConstructor @ToString @AllArgsConstructor
public class RegistrationDTO {
    private String username;
    @Email
    private String email;
// We would like our password to contain all of the following
//   * At least one digit [0-9]
//   * At least one lowercase character [a-z]
//   * At least one uppercase character [A-Z]
//   * At least one special character [*.!@#$%^&(){}[]:;
//    regex clean = ^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[*.!@$%^&(){}[]:;<>,.?/~_+-=|\]).{8,32}$
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,32}$")
    private String password;
}
