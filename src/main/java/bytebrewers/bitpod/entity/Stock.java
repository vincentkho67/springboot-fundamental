package bytebrewers.bitpod.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "m_stocks")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Stock extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    private String company;
    private Double price;
    private Integer lot;

    @OneToMany(mappedBy = "stock")
    @JsonIgnore
    private List<Transaction> transactions = new ArrayList<>();
}
