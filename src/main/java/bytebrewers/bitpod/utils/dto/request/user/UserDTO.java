package bytebrewers.bitpod.utils.dto.request.user;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserDTO {
    private String id;
    private String name;
    private String username;
    private String address;
    private MultipartFile image;
    private String birthDate;
}
