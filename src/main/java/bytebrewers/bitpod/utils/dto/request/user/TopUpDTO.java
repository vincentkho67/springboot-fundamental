package bytebrewers.bitpod.utils.dto.request.user;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TopUpDTO {
    @NotBlank(message = "enter amount")
    private BigDecimal amount;
}
