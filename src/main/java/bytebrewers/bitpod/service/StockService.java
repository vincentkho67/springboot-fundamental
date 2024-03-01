package bytebrewers.bitpod.service;

import bytebrewers.bitpod.entity.Stock;
import bytebrewers.bitpod.utils.dto.request.stock.StockDTO;
import bytebrewers.bitpod.utils.dto.response.stock.RecommendationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StockService {
    Page<Stock> getAll(Pageable pageable, StockDTO stockDTO);
    List<StockDTO> fetch(); // for getting stocks data form external API
    Stock create(StockDTO stockDTO);
    Stock getById(String id);
    Stock update(String id, StockDTO stockDTO);
    void delete(String id);
    Page<RecommendationDTO> recommend(Pageable pageable);
}
