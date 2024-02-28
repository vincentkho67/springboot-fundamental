package bytebrewers.bitpod.utils.dto.request.bank;

import bytebrewers.bitpod.entity.Bank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BankDTO {
    @NotNull
    private String name;
    @NotNull
    private String address;

    public Bank toEntity() {
        return Bank.builder()
                .name(name)
                .address(address)
                .build();
    }
}