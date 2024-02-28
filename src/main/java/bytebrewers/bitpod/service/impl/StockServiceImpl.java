package bytebrewers.bitpod.service.impl;
import bytebrewers.bitpod.entity.Stock;
import bytebrewers.bitpod.repository.StockRepository;
import bytebrewers.bitpod.service.StockService;
import bytebrewers.bitpod.utils.dto.request.stock.StockDTO;
import bytebrewers.bitpod.utils.specification.GeneralSpecification;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
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
                return StockDTO.mapFromApiResponse(apiResponse);
            }
        }
        return Collections.emptyList();
    }

    @Override
    public Stock create(StockDTO stockDTO) {
        return null;
    }

    @Override
    public Stock getById(Integer id) {
        return null;
    }

    @Override
    public Stock update(Integer id, StockDTO stockDTO) {
        return null;
    }

    @Override
    public void delete(Integer id) {

    }
}
