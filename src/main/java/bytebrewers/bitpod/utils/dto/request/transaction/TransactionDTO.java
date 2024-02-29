package bytebrewers.bitpod.utils.dto.request.transaction;

import bytebrewers.bitpod.entity.Bank;
import bytebrewers.bitpod.entity.Portfolio;
import bytebrewers.bitpod.entity.Stock;
import bytebrewers.bitpod.entity.Transaction;
import bytebrewers.bitpod.utils.enums.ETransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransactionDTO {
    private Double price;
    private Integer lot;
    private String transactionType;
    private String stockId;
    private Integer bankId;

    public Transaction toEntity(Stock stock, Portfolio port, Bank bank) {
        return Transaction.builder()
                .price(price)
                .lot(lot)
                .transactionType(ETransactionType.valueOf(transactionType))
                .stock(stock)
                .portfolio(port)
                .bank(bank)
                .build();
    }
}
