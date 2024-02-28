package bytebrewers.bitpod.controller;

import bytebrewers.bitpod.utils.constant.ApiUrl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiUrl.BASE_URL + ApiUrl.BASE_BANK)
@RequiredArgsConstructor
public class BankController {
}
