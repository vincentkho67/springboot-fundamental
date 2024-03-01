package bytebrewers.bitpod.utils.dto.request.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TopUpSnapDTO {

    private String userId;
    private Integer gross_amount;

}
