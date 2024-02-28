package bytebrewers.bitpod.utils.dto.request.stock;

import bytebrewers.bitpod.entity.Stock;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class StockDTO {
    @NotNull
    private String name;
    @NotNull
    private String company;
    @NotNull
    private Double price;
    @NotNull
    private Integer lot;

    public Stock toEntity() {
        return Stock.builder()
                .name(name)
                .company(company)
                .price(price)
                .lot(lot)
                .build();
    }

    public static List<StockDTO> mapFromApiResponse(JsonNode apiResponse) {
        List<StockDTO> stockDTOList = new ArrayList<>();

        JsonNode dataNode = apiResponse.get("data");
        if (dataNode != null && dataNode.isArray()) {
            for (JsonNode stockNode : dataNode) {
                StockDTO stockDTO = new StockDTO();

                stockDTO.setName(stockNode.get("symbol").asText());
                stockDTO.setCompany(stockNode.get("company").get("name").asText());
                stockDTO.setPrice(stockNode.get("close").asDouble());
                stockDTO.setLot(stockNode.get("volume").asInt());

                stockDTOList.add(stockDTO);
            }
        }

        return stockDTOList;
    }
    public static List<StockDTO> mapFromEntityList(List<Stock> stocks) {
        return stocks.stream()
                .map(StockDTO::mapFromEntity)
                .collect(Collectors.toList());
    }
    public static StockDTO mapFromEntity(Stock stock) {
        StockDTO stockDTO = new StockDTO();
        stockDTO.setName(stock.getName());
        stockDTO.setCompany(stock.getCompany());
        stockDTO.setPrice(stock.getPrice());
        stockDTO.setLot(stock.getLot());
        return stockDTO;
    }

}
