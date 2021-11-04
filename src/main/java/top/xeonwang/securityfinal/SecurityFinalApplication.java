package top.xeonwang.securityfinal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Chen Q.
 */
@EnableAsync
@EnableScheduling
@SpringBootApplication
public class SecurityFinalApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityFinalApplication.class, args);
    }

}
