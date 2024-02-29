package bytebrewers.bitpod.utils.dto.request.user;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegisterDTO {
    private String email;

    private String password;

    private String name;

    private String username;

    private String address;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;

}
