package top.xeonwang.securityfinal.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GraphicsCard;
import oshi.hardware.HardwareAbstractionLayer;
import top.xeonwang.securityfinal.VO.SystemInfoVO;

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

    public String getSystemInfo() {
        Map kvs = redisTemplate.opsForHash().entries("SystemInfo");

        kvs.forEach((key, val) -> {
            log.debug((String) key + " " + (String) val);
        });
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(kvs);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

//    @Scheduled(cron = "10/1 * * * * ?")
    private void RecordHost() {
        SystemInfoVO vo = SystemInfoVO.toSystemInfo();
        ObjectMapper mapper = new ObjectMapper();
        String str = null;
        try {
            str = mapper.writeValueAsString(vo);
            log.debug("localhost: " + vo.toString());
            redisTemplate.opsForHash().put("SystemInfo", "localhost", str);
        } catch (JsonProcessingException e) {
            log.error("Json");
        } catch (Exception e) {
            log.error("redis fail");
        }

    }
}
