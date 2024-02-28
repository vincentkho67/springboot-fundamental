package bytebrewers.bitpod.controller;

import bytebrewers.bitpod.service.StockService;
import bytebrewers.bitpod.utils.constant.ApiUrl;
import bytebrewers.bitpod.utils.dto.Res;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiUrl.BASE_URL + ApiUrl.BASE_STOCK)
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;
    @GetMapping
    public ResponseEntity<?> index() {
        return Res.renderJson(stockService.fetch(), "Stock found", HttpStatus.OK);
    }
}
