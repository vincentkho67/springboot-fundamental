package bytebrewers.bitpod.controller;

import bytebrewers.bitpod.entity.Stock;
import bytebrewers.bitpod.service.StockService;
import bytebrewers.bitpod.utils.constant.ApiUrl;
import bytebrewers.bitpod.utils.constant.Messages;
import bytebrewers.bitpod.utils.dto.PageResponseWrapper;
import bytebrewers.bitpod.utils.dto.Res;
import bytebrewers.bitpod.utils.dto.request.stock.StockDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping(ApiUrl.BASE_URL + ApiUrl.BASE_STOCK)
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;
    @GetMapping
    public ResponseEntity<?> index(
            @PageableDefault(page = 0, size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
            @ModelAttribute StockDTO stockDTO
    ) {
        Page<Stock> result = stockService.getAll(pageable, stockDTO);
        PageResponseWrapper<Stock> responseWrapper = new PageResponseWrapper<>(result);
        return Res.renderJson(responseWrapper, Messages.STOCK_FOUND, HttpStatus.OK);
    }

    @GetMapping("/fetch")
    public ResponseEntity<?> fetch() {
        return Res.renderJson(stockService.fetch(), Messages.STOCK_UPDATED, HttpStatus.OK);
    }
}
