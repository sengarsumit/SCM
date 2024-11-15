package com.scm.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString

public class UserForm {
    @NotBlank(message = "username is required")
    @Size(min = 3, message ="Minimum 3 characters are required")
    private String name;
    @NotBlank(message = "email is required")
    @Email(message = "invalid email")
    private String email;
    @NotBlank(message = "passowrd is requires")
    @Size(min=6,message = "minimum 6 charachters are required")
    private String password;
    @NotBlank(message = "about is required")
    private String about;
    @NotBlank(message = "phone number is required")
    @Size(min=10,max=13,message = "invalid phone number")
    private String phoneNumber;

}
