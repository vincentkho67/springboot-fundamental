package bytebrewers.bitpod.service;

import bytebrewers.bitpod.entity.Stock;
import bytebrewers.bitpod.utils.dto.request.stock.StockDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StockService {
    Page<Stock> getAll(Pageable pageable, StockDTO stockDTO);
    List<StockDTO> fetch();
    Stock create(StockDTO stockDTO);
    Stock getById(Integer id);
    Stock update(Integer id, StockDTO stockDTO);
    void delete(Integer id);
}
