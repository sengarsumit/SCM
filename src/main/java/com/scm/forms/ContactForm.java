package com.scm.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContactForm {

    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Email is required")
    @Email(message = "invalid Email Address")
    private String email;
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$",message = "Invalid Phone number")
    private String phoneNumber;
    @NotBlank(message = "Address is required")
    private String address;

    private String description;
    private boolean favorite;
    private String websiteLink;
    private String LinkedinLink;
    //create annotations for size and resolution

    private MultipartFile contactImage;
}
