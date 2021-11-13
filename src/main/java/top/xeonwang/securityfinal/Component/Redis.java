package top.xeonwang.securityfinal.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import top.xeonwang.securityfinal.VO.SystemInfoVO;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class Redis implements ApplicationRunner {

    @Autowired
    RedisTemplate<String, Serializable> redisTemplate;

    public void getValue() {
        String s = null;
        try {
            s = (String) redisTemplate.opsForValue().get("mykey");
            log.info(s);
            if (redisTemplate.hasKey("SystemInfo")) {
                redisTemplate.delete("SystemInfo");
                log.info("清空原数据");
            }
            SystemInfoVO vo = SystemInfoVO.toSystemInfo();
            ObjectMapper mapper = new ObjectMapper();
            String str = mapper.writeValueAsString(vo);
            redisTemplate.opsForHash().put("SystemInfo", "localhost", str);
            log.info("redis测试成功！");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("redis连接失败...");
        } finally {
            log.info("测试redis连接完毕");
        }
    }

    @Order(0)
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("测试redis连接...");
        getValue();
    }
}
