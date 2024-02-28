package bytebrewers.bitpod.utils.dto.request.bank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BankSearchDTO {
    private String name;
    private String address;
}