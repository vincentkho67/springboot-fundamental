package bytebrewers.bitpod.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Value;
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
    @Value("${app.bit-pods.cloudinary-cloud-name}")
    private String cloudName;

    @Value("${app.bit-pods.cloudinary-api-key}")
    private String cloudApiKey;

    @Value("${app.bit-pods.cloudinary-api-secret}")
    private String cloudApiSecret;

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
            "cloud_name",cloudName,
            "api_key",cloudApiKey,
            "api_secret",cloudApiSecret
        ));
    }

    @Bean
    public ExecutorService executorService(){
        return Executors.newFixedThreadPool(5);
    }
}
