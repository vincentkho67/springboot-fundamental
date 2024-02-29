package bytebrewers.bitpod.utils.dto.request.user;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    
    private String id;

    private String name;

    private String username;

    private String address;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;

    private String profilePicture;
}
