package bytebrewers.bitpod.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "m_users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
}
