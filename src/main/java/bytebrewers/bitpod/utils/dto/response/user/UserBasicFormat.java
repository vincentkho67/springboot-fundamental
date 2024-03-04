package bytebrewers.bitpod.utils.dto.response.user;

import bytebrewers.bitpod.entity.User;
import lombok.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserBasicFormat {
    private String name;
    private String username;
    private String address;
    private String birthDate;
    private String profilePicture;
    private BigDecimal balance;

    public static UserBasicFormat fromUser(User user) {
        return UserBasicFormat.builder()
                .name(user.getName())
                .username(user.getUsername())
                .address(user.getAddress())
                .birthDate(formatBirthDate(user.getBirthDate()))
                .balance(user.getBalance())
                .profilePicture(user.getProfilePicture())
                .build();
    }

    private static <Date> String formatBirthDate(Date birthDate) {
        if (birthDate != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.format(birthDate);
        }
        return null;
    }
}
