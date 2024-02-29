package bytebrewers.bitpod.utils.dto.request.user;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TopUpDTO {
    private String id;

    private BigDecimal amount;
}
