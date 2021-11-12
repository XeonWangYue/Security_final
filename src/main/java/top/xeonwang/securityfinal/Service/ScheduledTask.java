package top.xeonwang.securityfinal.Service;

import ch.qos.logback.classic.Logger;
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

@Slf4j
@Component
public class ScheduledTask {
    @Autowired
    RedisTemplate<String, Serializable> redisTemplate;
    public static Vector<RedisContent> vector = new Vector<RedisContent>();

    @Scheduled(cron = "*/1 * * * * ?")
    public void execute() {
        String key = Long.toString(System.currentTimeMillis());
        Vector<RedisContent> copy;
        synchronized (this) {
            copy = (Vector<RedisContent>) vector.clone();
            vector.clear();
        }
        log.info("copy size "+copy.size());
        Map<String, Map<String,PortStatistic>> srcStatistic = new ConcurrentHashMap<>();
        Map<String, Map<String,PortStatistic>> dstStatistic = new ConcurrentHashMap<>();
        for (RedisContent r : copy) {
            if (!srcStatistic.keySet().contains(r.getSrcAddress())) {
                log.info("exist: "+r.getSrcAddress());
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
                log.info("debug size "+dstStatistic.get(r.getDstAddress()).keySet().size()+"");
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
            redisTemplate.opsForValue().set(key, r);
            //log.info(r.getProtocol().toString());
        }

        log.info("src ip num:" + srcStatistic.keySet().size());
        log.info("dst ip num:" + dstStatistic.keySet().size());
        if(!srcStatistic.keySet().isEmpty()) {
            String ipSrcTest = srcStatistic.keySet().iterator().next();
            String ipDstTest = dstStatistic.keySet().iterator().next();
            log.info("src ip ports num:" + srcStatistic.get(ipSrcTest).keySet().size());
            for (String s : srcStatistic.get(ipSrcTest).keySet()) {
                log.info("count: " + srcStatistic.get(ipSrcTest).get(s).getCount()
                        + srcStatistic.get(ipSrcTest).get(s).getLength());
            }
            log.info("dst ip ports num:" + dstStatistic.get(ipDstTest).keySet().size());
            for (String s : dstStatistic.get(ipDstTest).keySet()) {
                log.info("count: " + dstStatistic.get(ipDstTest).get(s).getCount()
                        + dstStatistic.get(ipDstTest).get(s).getLength());
            }
        }

        redisTemplate.ops
        copy.clear();
    }
}
