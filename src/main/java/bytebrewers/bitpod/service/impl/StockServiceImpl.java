package bytebrewers.bitpod.service.impl;
import bytebrewers.bitpod.entity.Auditable;
import bytebrewers.bitpod.entity.Stock;
import bytebrewers.bitpod.entity.Transaction;
import bytebrewers.bitpod.repository.StockRepository;
import bytebrewers.bitpod.service.StockService;
import bytebrewers.bitpod.utils.dto.request.stock.StockDTO;
import bytebrewers.bitpod.utils.dto.response.stock.RecommendationDTO;
import bytebrewers.bitpod.utils.helper.EntityUpdater;
import bytebrewers.bitpod.utils.specification.GeneralSpecification;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
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
    @Scheduled(initialDelay = 0, fixedRate = 300000)
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
        return Auditable.searchById(stockRepository.findById(id), "Stock not found");
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

    @Override
    public Page<RecommendationDTO> recommend(Pageable pageable) {
        List<RecommendationDTO> recommendations = new ArrayList<>();

        // Get all stocks from db
        List<Stock> stocksInDatabase = stockRepository.findAll();

        // Fetch new stock data
        List<StockDTO> fetchedStocks = fetchWithoutUpdating();
        Map<String, Stock> stockMap = stocksInDatabase.stream()
                .collect(Collectors.toMap(Stock::getName, Function.identity()));

        for (StockDTO fetchedStock : fetchedStocks) {
            Stock existingStock = stockMap.get(fetchedStock.getName());

            if (existingStock != null) {
                double percentageChange = calculatePercentageChange(existingStock.getPrice(), fetchedStock.getPrice());
                String formattedPercentageChange = String.format("%.2f", percentageChange);
                RecommendationDTO recommendationDTO = createRecommendationDTO(fetchedStock, formattedPercentageChange);
                recommendations.add(recommendationDTO);
            }
        }

        return wrap(recommendations,pageable) ;
    }

    private double calculatePercentageChange(double oldPrice, double newPrice) {
        return ((newPrice - oldPrice) / oldPrice) * 100;
    }
    private RecommendationDTO createRecommendationDTO(StockDTO fetchedStock, String formattedPercentageChange) {
        RecommendationDTO recommendationDTO = new RecommendationDTO();
        recommendationDTO.setStock(fetchedStock.toEntity());

        // Analyze the percentage change
        String analysis = (formattedPercentageChange.startsWith("-")) ? "Bearish" : "Bullish";
        String recommendation = (formattedPercentageChange.startsWith("-")) ? "Sell" : "Buy";

        recommendationDTO.setAnalysis(analysis + " with " + formattedPercentageChange + "% change");
        recommendationDTO.setRecommendation(recommendation);

        return recommendationDTO;
    }

    private Page<RecommendationDTO> wrap(List<RecommendationDTO> dtoList, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<RecommendationDTO> pageList;

        if (startItem < dtoList.size()) {
            int endItem = Math.min(startItem + pageSize, dtoList.size());
            pageList = dtoList.subList(startItem, endItem);
        } else {
            pageList = Collections.emptyList();
        }

        return new PageImpl<>(pageList, pageable, dtoList.size());
    }

    private List<StockDTO> fetchWithoutUpdating() {
        ResponseEntity<JsonNode> res = restTemplate.getForEntity(mockUrl, JsonNode.class);
        if (res.getStatusCode().is2xxSuccessful()) {
            JsonNode apiResponse = res.getBody();
            if (apiResponse != null) {
                return StockDTO.mapFromApiResponse(apiResponse);
            }
        }
        return Collections.emptyList();
    }
}
