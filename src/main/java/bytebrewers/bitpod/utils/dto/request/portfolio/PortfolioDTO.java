package bytebrewers.bitpod.utils.dto.request.portfolio;

import bytebrewers.bitpod.entity.Portfolio;
import bytebrewers.bitpod.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioDTO {
    private BigDecimal avgBuy;
    private String returns;

    public Portfolio toEntity(User user) {
        return Portfolio.builder()
                .avgBuy(avgBuy)
                .returns(returns)
                .user(user)
                .build();
    }
}
