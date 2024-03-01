package bytebrewers.bitpod.utils.dto.response.user;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TopUpMidtransresponseDTO {
    private Map<String, Object>  requestBody;
    private String clientKey;
    private String token;
}
