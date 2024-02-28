package bytebrewers.bitpod.utils.dto.response.user;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtClaim {
    private String userId;
    private List<String> roles;
}
