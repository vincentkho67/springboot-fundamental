package bytebrewers.bitpod.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;


@Configuration
public class BeanConfiguration {
    @Bean
    public PasswordEncoder passwordEncode() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception {
        return auth.getAuthenticationManager();
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    
    @Bean
    public Cloudinary cloudinaryAccount(){
        return new Cloudinary(ObjectUtils.asMap(
            "cloud_name","de0yidcs5",
            "api_key","415953523312214",
            "api_secret","Y5JMq_FNpPplA8Fmtn-zn3sGfnw"
        ));
    }
}
