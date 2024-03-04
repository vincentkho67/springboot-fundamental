package bytebrewers.bitpod.controller;

import bytebrewers.bitpod.entity.Stock;
import bytebrewers.bitpod.service.StockService;
import bytebrewers.bitpod.utils.constant.ApiUrl;
import bytebrewers.bitpod.utils.constant.Messages;
import bytebrewers.bitpod.utils.dto.PageResponseWrapper;
import bytebrewers.bitpod.utils.dto.Res;
import bytebrewers.bitpod.utils.dto.request.stock.StockDTO;
import bytebrewers.bitpod.utils.dto.response.stock.RecommendationDTO;
import bytebrewers.bitpod.utils.swagger.stock.SwaggerStockFetch;
import bytebrewers.bitpod.utils.swagger.stock.SwaggerStockIndex;
import bytebrewers.bitpod.utils.swagger.stock.SwaggerStockRecommend;
import bytebrewers.bitpod.utils.swagger.stock.SwaggerStockShow;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping(ApiUrl.BASE_URL + ApiUrl.BASE_STOCK)
@RequiredArgsConstructor
@Tag(name = "Stock", description = "Stock API")
public class StockController {
    private final StockService stockService;

    @SwaggerStockIndex
    @GetMapping
    public ResponseEntity<?> index(
            @PageableDefault(page = 0, size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
            @ModelAttribute StockDTO stockDTO
    ) {
        Page<Stock> result = stockService.getAll(pageable, stockDTO);
        PageResponseWrapper<Stock> responseWrapper = new PageResponseWrapper<>(result);
        return Res.renderJson(responseWrapper, Messages.STOCK_FOUND, HttpStatus.OK);
    }

    @SwaggerStockFetch
    @GetMapping("/fetch")
    public ResponseEntity<?> fetch() {
        return Res.renderJson(stockService.fetch(), Messages.STOCK_UPDATED, HttpStatus.OK);
    }


    @SwaggerStockShow
    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable String id) {
        Stock stock = stockService.getById(id);
        return Res.renderJson(stock, Messages.STOCK_FOUND, HttpStatus.OK);
    }

    @SwaggerStockRecommend
    @GetMapping("/recommendation")
    public ResponseEntity<?> recommend(
            @PageableDefault(page = 0, size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<RecommendationDTO> res = stockService.recommend(pageable);
        PageResponseWrapper<RecommendationDTO> responseWrapper = new PageResponseWrapper<>(res);
        return Res.renderJson(responseWrapper, Messages.STOCK_FOUND, HttpStatus.OK);
    }
}
