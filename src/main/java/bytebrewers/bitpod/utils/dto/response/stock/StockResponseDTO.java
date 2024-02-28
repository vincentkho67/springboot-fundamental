package bytebrewers.bitpod.utils.dto.response.stock;

import bytebrewers.bitpod.utils.dto.request.stock.StockDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StockResponseDTO {
    private String status;
    private String message;
    private List<StockDTO> data;
}
