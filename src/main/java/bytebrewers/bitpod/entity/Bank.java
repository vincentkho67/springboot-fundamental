package bytebrewers.bitpod.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "m_banks")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Bank extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String address;

    @OneToMany(mappedBy = "bank")
    @JsonIgnore
    private List<Transaction> transactions = new ArrayList<>();
}
