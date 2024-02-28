package bytebrewers.bitpod.entity;

import bytebrewers.bitpod.utils.enums.ETransactionType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "t_transactions")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Transaction extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private Double price;
    private Integer lot;
    @Enumerated(EnumType.STRING)
    private ETransactionType transactionType;

    @ManyToOne
    @JoinColumn(name ="stock_id")
    private Stock stock;

    @ManyToOne
    @JoinColumn(name ="portfolio_id")
    @JsonIgnore
    private Portfolio portfolio;

    @ManyToOne
    @JoinColumn(name ="bank_id")
    private Bank bank;


}
