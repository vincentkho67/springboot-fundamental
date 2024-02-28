package bytebrewers.bitpod.service.impl;
import bytebrewers.bitpod.entity.Stock;
import bytebrewers.bitpod.repository.StockRepository;
import bytebrewers.bitpod.service.StockService;
import bytebrewers.bitpod.utils.dto.request.stock.StockDTO;
import bytebrewers.bitpod.utils.helper.EntityUpdater;
import bytebrewers.bitpod.utils.specification.GeneralSpecification;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {
    private final StockRepository stockRepository;
    private final RestTemplate restTemplate;
    @Value("${app.bit-pods.mock-url-stock}")
    private String mockUrl;
    @Override
    public Page<Stock> getAll(Pageable pageable, StockDTO stockDTO) {
        Specification<Stock> specification = GeneralSpecification.getSpecification(stockDTO);
        return stockRepository.findAll(specification, pageable);
    }

    @Override
    public List<StockDTO> fetch() {
        ResponseEntity<JsonNode> res = restTemplate.getForEntity(mockUrl, JsonNode.class);
        if (res.getStatusCode().is2xxSuccessful()) {
            JsonNode apiResponse = res.getBody();
            if (apiResponse != null) {
                List<StockDTO> checkStocks = StockDTO.mapFromApiResponse(apiResponse);

                List<Stock> stocks = new ArrayList<>();
                for(StockDTO s : checkStocks) {
                    Stock existStock = stockRepository.findByName(s.getName());
                    if(existStock != null) {
                        Stock updateStock = update(existStock.getId(), s);
                        stocks.add(updateStock);
                    } else {
                        Stock newStock = create(s);
                        stocks.add(newStock);
                    }
                }
                return StockDTO.mapFromEntityList(stocks);
            }
        }
        return Collections.emptyList();
    }

    @Override
    public Stock create(StockDTO stockDTO) {
        return stockRepository.save(stockDTO.toEntity());
    }

    @Override
    public Stock getById(String id) {
        return stockRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Stock not found"));
    }

    @Override
    public Stock update(String id, StockDTO stockDTO) {
        Stock existingStock = stockRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Stock not found"));
        EntityUpdater.updateEntity(existingStock, stockDTO.toEntity());
        return stockRepository.save(existingStock);
    }

    @Override
    public void delete(String id) {
        stockRepository.deleteById(id);
    }
}
