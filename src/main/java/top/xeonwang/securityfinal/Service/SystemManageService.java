package top.xeonwang.securityfinal.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import top.xeonwang.securityfinal.Netty.MsgServerHandler;
import top.xeonwang.securityfinal.VO.SystemInfoVO;

import java.util.ArrayList;
import java.util.List;
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

    @Value(value = "${pcap.ipaddr}")
    String ip;

    @Autowired
    MsgServerHandler handler;

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

    public String sendWaring(String hostname) {
        return handler.sendWarining(hostname);
    }

    public String sendDisable(String hostname) {
        return handler.sendDisable(hostname);
    }

    @Async("thread")
    @Scheduled(cron = "10/1 * * * * ?")
    public void RecordHost() {
        SystemInfoVO vo = SystemInfoVO.toSystemInfo();
        ObjectMapper mapper = new ObjectMapper();
        String str = null;
        try {
            str = mapper.writeValueAsString(vo);
            log.debug(ip + vo.toString());
            redisTemplate.opsForHash().put("SystemInfo", ip, str);
        } catch (JsonProcessingException e) {
            log.error("Json");
        } catch (Exception e) {
            log.error("redis fail");
        }

    }
}
