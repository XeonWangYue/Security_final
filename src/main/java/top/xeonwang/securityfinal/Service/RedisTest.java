package top.xeonwang.securityfinal.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;

@Slf4j
@Service
public class RedisTest {

    @Autowired
    RedisTemplate<String, Serializable> redisTemplate;

    public String getValue() {
//        redisTemplate.opsForValue().set("mykey","mySecondValue");
        return (String) redisTemplate.opsForValue().get("mykey");
    }

    @Scheduled(cron = "*/1 * * * * ?")
    public void excute(){
        log.error("time stamp");
    }
}
