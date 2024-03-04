package bytebrewers.bitpod.utils.dto.request.user;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegisterDTO {

    @Email(message = "Email is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @NotBlank(message = "Email cannot be empty")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    private String password;

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotBlank(message = "username cannot be empty")
    private String username;

    @NotBlank(message = "Adderess cannot be empty")
    private String address;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;

}
