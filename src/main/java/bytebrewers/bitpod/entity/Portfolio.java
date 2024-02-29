package bytebrewers.bitpod.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "t_portfolios")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Portfolio extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "avg_buy")
    private BigDecimal avgBuy;
    private String returns;

    @OneToMany(mappedBy = "portfolio")
    private List<Transaction> transactions = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    private User user;
}
