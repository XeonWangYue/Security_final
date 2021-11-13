package top.xeonwang.securityfinal.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author Chen Q.
 */
@Slf4j
@Service
public class HeartBeatService {

    @Scheduled(cron = "*/5 * * * * ?")
    public String clientInfo() {
        return "";
    }
}
