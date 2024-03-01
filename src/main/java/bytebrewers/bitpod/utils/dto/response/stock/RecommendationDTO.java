package bytebrewers.bitpod.utils.dto.response.stock;

import bytebrewers.bitpod.entity.Stock;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecommendationDTO {
    private String analysis;
    private String recommendation;
    private Stock stock;
}
