package top.xeonwang.securityfinal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author Chen Q.
 */
@EnableAsync
@SpringBootApplication
public class SecurityFinalApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityFinalApplication.class, args);
    }

}
