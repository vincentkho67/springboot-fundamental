package bytebrewers.bitpod.entity;

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
    private BigDecimal avgBuy;
    private String returns;

    @OneToMany(mappedBy = "portfolio")
    private List<Transaction> transactions = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name ="user_id")
    private User user;
}
