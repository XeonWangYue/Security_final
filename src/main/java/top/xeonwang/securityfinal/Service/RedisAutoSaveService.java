package top.xeonwang.securityfinal.Service;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.xeonwang.securityfinal.Util.Interface.PortStatistic;
import top.xeonwang.securityfinal.Util.RedisContent;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Song Y.
 */
@Slf4j
@Component
public class RedisAutoSaveService {
    @Autowired
    RedisTemplate<String, Serializable> redisTemplate;
    public static Vector<RedisContent> vector = new Vector<RedisContent>();

//    @Scheduled(cron = "10/1 * * * * ?")
    public void execute() {
        String key = Long.toString(System.currentTimeMillis());
        Vector<RedisContent> copy;
        synchronized (this) {
            copy = (Vector<RedisContent>) vector.clone();
            vector.clear();
        }
        log.debug("copy size " + copy.size());
        Map<String, Map<String, PortStatistic>> srcStatistic = new ConcurrentHashMap<>();
        Map<String, Map<String, PortStatistic>> dstStatistic = new ConcurrentHashMap<>();
        for (RedisContent r : copy) {
            if (!srcStatistic.keySet().contains(r.getSrcAddress())) {
                log.debug("exist: " + r.getSrcAddress());
                Map<String, PortStatistic> portStatisticMap = new HashMap<>();
                PortStatistic portStatistic = new PortStatistic();
                portStatistic.setCount(1);
                portStatistic.setLength(r.getLength());
                portStatisticMap.put(r.getSrcPort(), portStatistic);
                srcStatistic.put(r.getSrcAddress(), portStatisticMap);
            } else {
                if (srcStatistic.get(r.getSrcAddress()).keySet().contains(r.getSrcPort())) {
                    srcStatistic.get(r.getSrcAddress()).get(r.getSrcPort()).count += 1;
                    srcStatistic.get(r.getSrcAddress()).get(r.getSrcPort()).length += r.getLength();
                } else {
                    PortStatistic portStatistic = new PortStatistic();
                    portStatistic.setCount(1);
                    portStatistic.setLength(r.getLength());
                    srcStatistic.get(r.getSrcAddress()).put(r.getSrcPort(), portStatistic);
                }
            }
            if (!dstStatistic.keySet().contains(r.getDstAddress())) {
                Map<String, PortStatistic> portStatisticMap = new HashMap<>();
                PortStatistic portStatistic = new PortStatistic();
                portStatistic.setCount(1);
                portStatistic.setLength(r.getLength());
                portStatisticMap.put(r.getDstPort(), portStatistic);
                dstStatistic.put(r.getDstAddress(), portStatisticMap);
            } else {
                log.debug("debug size " + dstStatistic.get(r.getDstAddress()).keySet().size() + "");
                if (dstStatistic.get(r.getDstAddress()).keySet().contains(r.getDstPort())) {
                    dstStatistic.get(r.getDstAddress()).get(r.getDstPort()).count += 1;
                    dstStatistic.get(r.getDstAddress()).get(r.getDstPort()).length += r.getLength();
                } else {
                    PortStatistic portStatistic = new PortStatistic();
                    portStatistic.setCount(1);
                    portStatistic.setLength(r.getLength());
                    dstStatistic.get(r.getDstAddress()).put(r.getDstPort(), portStatistic);
                }
            }
//            redisTemplate.opsForValue().set(key, r);
            log.debug(r.getProtocol().toString());
        }

        log.debug("src ip num:" + srcStatistic.keySet().size());
        log.debug("dst ip num:" + dstStatistic.keySet().size());
        if (!srcStatistic.keySet().isEmpty()) {
            String ipSrcTest = srcStatistic.keySet().iterator().next();
            String ipDstTest = dstStatistic.keySet().iterator().next();
            log.debug("src ip ports num:" + srcStatistic.get(ipSrcTest).keySet().size());
            for (String s : srcStatistic.get(ipSrcTest).keySet()) {
                log.debug("count: " + srcStatistic.get(ipSrcTest).get(s).getCount()
                        + srcStatistic.get(ipSrcTest).get(s).getLength());
            }
            log.debug("dst ip ports num:" + dstStatistic.get(ipDstTest).keySet().size());
            for (String s : dstStatistic.get(ipDstTest).keySet()) {
                log.debug("count: " + dstStatistic.get(ipDstTest).get(s).getCount()
                        + dstStatistic.get(ipDstTest).get(s).getLength());
            }
            ObjectMapper mapper = new ObjectMapper();
            try {
                String srcString = mapper.writeValueAsString(srcStatistic);
                String dstString = mapper.writeValueAsString(dstStatistic);
                redisTemplate.boundListOps("srcStatistic").rightPush(dstString);
//                redisTemplate.boundHashOps("dstStatistic").put(key, dstString);

                String ret = (String) redisTemplate.boundHashOps("srcStatistic").get(key);
                Map retMap = mapper.readValue(ret, Map.class);
                log.debug(retMap.size() + "");
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        copy.clear();
    }
}
