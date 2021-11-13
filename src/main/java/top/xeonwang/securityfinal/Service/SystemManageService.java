package top.xeonwang.securityfinal.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import top.xeonwang.securityfinal.VO.SystemInfoVO;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * @author Chen Q.
 */
@Slf4j
@Service
public class SystemManageService {
    @Autowired
    RedisTemplate redisTemplate;

    public String getSystemInfo(String hostname) {
        Object value = redisTemplate.opsForHash().get("SystemInfo", hostname);
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getHosts() {
        Object[] hosts = redisTemplate.opsForHash().keys("SystemInfo").toArray();
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(hosts);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Scheduled(cron = "10/1 * * * * ?")
    private void RecordHost() {
        SystemInfoVO vo = SystemInfoVO.toSystemInfo();
        ObjectMapper mapper = new ObjectMapper();
        String str = null;
        try {
            str = mapper.writeValueAsString(vo);
            log.debug("/localhost: " + vo.toString());
            redisTemplate.opsForHash().put("SystemInfo", "/localhost", str);
        } catch (JsonProcessingException e) {
            log.error("Json");
        } catch (Exception e) {
            log.error("redis fail");
        }

    }
}
