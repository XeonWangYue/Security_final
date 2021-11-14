package top.xeonwang.securityfinal.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.pcap4j.packet.namednumber.EtherType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import top.xeonwang.securityfinal.Util.Interface.PortStatistic;
import top.xeonwang.securityfinal.Util.Content;
import top.xeonwang.securityfinal.VO.QueryRetVO;
import top.xeonwang.securityfinal.VO.QueryVO;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Song Y.
 */
@Slf4j
@Component
public class PcapOpsService {
    @Autowired
    RedisTemplate<String, Serializable> redisTemplate;
    public static Vector<Content> vector = new Vector<Content>();

    ObjectMapper objectMapper = new ObjectMapper();


    public String searchBySrcIp(QueryVO queryVO) {
        Vector<Content> copy;
        copy = (Vector<Content>) vector.clone();
        QueryRetVO res = new QueryRetVO();
        res.setLength(0L);
        res.setIp(queryVO.getSrcIp());
        res.setPort(queryVO.getSrcPort());
        for(Content c : copy){

            if(c.srcAddressPort.contains(queryVO.getSrcIp())){
                res.setLength(res.getLength()+c.getLength());
                res.setCount(res.getCount()+1);
            }
        }
        String r = null;
        try {
            r = objectMapper.writeValueAsString(res);
        }
        catch(JsonProcessingException j) {

        }
        return r;
    }
    public String searchBySrcIpPort(QueryVO queryVO) {
        Vector<Content> copy;
        copy = (Vector<Content>) vector.clone();

        QueryRetVO res = new QueryRetVO();
        res.setCount(0L);
        res.setLength(0L);
        res.setIp(queryVO.getSrcIp());
        res.setPort(queryVO.getSrcPort());
        for(Content c : copy){
            if(c.srcAddressPort.contains(queryVO.getSrcIp()+":"+queryVO.getSrcPort())){
                res.setLength(res.getLength()+c.getLength());
                res.setCount(res.getCount()+1);
            }
        }
        String r = null;
        try {
            r = objectMapper.writeValueAsString(res);
        }
        catch(JsonProcessingException j) {

        }
        return r;
    }
    public String searchByDstIp(QueryVO queryVO) {
        Vector<Content> copy;
        synchronized (this) {
            copy = (Vector<Content>) vector.clone();

        }
        QueryRetVO res = new QueryRetVO();
        res.setCount(0L);
        res.setLength(0L);
        res.setIp(queryVO.getSrcIp());
        res.setPort(queryVO.getSrcPort());
        for(Content c : copy){
            if(c.dstAddressPort.contains(queryVO.getDstIp())){
                res.setLength(res.getLength()+c.getLength());
                res.setCount(res.getCount()+1);
            }
        }
        String r = null;
        try {
            r = objectMapper.writeValueAsString(res);
        }
        catch(JsonProcessingException j) {

        }
        return r;
    }
    public String searchByDstIpPort(QueryVO queryVO) {
        Vector<Content> copy;
        synchronized (this) {
            copy = (Vector<Content>) vector.clone();
        }
        QueryRetVO res = new QueryRetVO();
        res.setCount(0L);
        res.setLength(0L);
        res.setIp(queryVO.getSrcIp());
        res.setPort(queryVO.getSrcPort());
        for(Content c : copy){
            if(c.dstAddressPort.contains(queryVO.getDstIp()+":"+queryVO.getDstPort())){
                res.setLength(res.getLength()+c.getLength());
                res.setCount(res.getCount()+1);
            }
        }
        String r = null;
        try {
            r = objectMapper.writeValueAsString(res);
        }
        catch(JsonProcessingException j) {

        }
        return r;
    }
    public String searchByProtocol(QueryVO queryVO){
        Vector<Content> copy;
        synchronized (this) {
            copy = (Vector<Content>) vector.clone();
        }
        QueryRetVO res = new QueryRetVO();
        res.setCount(0L);
        res.setLength(0L);
        res.setIp(queryVO.getSrcIp());
        res.setPort(queryVO.getSrcPort());
        for(Content c : copy){
            log.info("protocol: "+ c.getProtocol().toString());
            if(c.getProtocol().toString().contains(queryVO.getType())){
                res.setLength(res.getLength()+c.getLength());
                res.setCount(res.getCount()+1);
            }
        }
        String r = null;
        try {
            r = objectMapper.writeValueAsString(res);
        }
        catch(JsonProcessingException j) {

        }
        return r;
    }
    public String searchBySrcIpProtocol(QueryVO queryVO){
        Vector<Content> copy;
        synchronized (this) {
            copy = (Vector<Content>) vector.clone();
        }
        QueryRetVO res = new QueryRetVO();
        res.setCount(0L);
        res.setLength(0L);
        res.setIp(queryVO.getSrcIp());
        res.setPort(queryVO.getSrcPort());
        for(Content c : copy){
            if(c.getProtocol().toString().contains(queryVO.getType())){
                if(c.getSrcAddressPort().contains(queryVO.getSrcIp())){
                    res.setLength(res.getLength()+c.getLength());
                    res.setCount(res.getCount()+1);
                }
            }
        }
        String r = null;
        try {
            r = objectMapper.writeValueAsString(res);
        }
        catch(JsonProcessingException j) {

        }
        return r;
    }
    public String searchByDstIpProtocol(QueryVO queryVO) {
        Vector<Content> copy;
        synchronized (this) {
            copy = (Vector<Content>) vector.clone();
        }
        QueryRetVO res = new QueryRetVO();
        res.setCount(0L);
        res.setLength(0L);
        res.setIp(queryVO.getSrcIp());
        res.setPort(queryVO.getSrcPort());
        for(Content c : copy){
            if(c.getProtocol().toString().contains(queryVO.getType())){
                if(c.getDstAddressPort().contains(queryVO.getDstIp())){
                    res.setLength(res.getLength()+c.getLength());
                    res.setCount(res.getCount()+1);
                }
            }
        }
        String r = null;
        try {
            r = objectMapper.writeValueAsString(res);
        }
        catch(JsonProcessingException j) {

        }
        return r;
    }
    public String searchBySrcIpPortProtocol(QueryVO queryVO){
        Vector<Content> copy;
        synchronized (this) {
            copy = (Vector<Content>) vector.clone();
        }
        QueryRetVO res = new QueryRetVO();
        res.setCount(0L);
        res.setLength(0L);
        res.setIp(queryVO.getSrcIp());
        res.setPort(queryVO.getSrcPort());
        for(Content c : copy){
            if(c.getProtocol().toString().contains(queryVO.getType())){
                if(c.getSrcAddressPort().contains(queryVO.getSrcIp()+":"+queryVO.getSrcPort())){
                    res.setLength(res.getLength()+c.getLength());
                    res.setCount(res.getCount()+1);
                }
            }
        }
        String r = null;
        try {
            r = objectMapper.writeValueAsString(res);
        }
        catch(JsonProcessingException j) {

        }
        return r;
    }
    public String searchByDstIpPortProtocol(QueryVO queryVO){
        Vector<Content> copy;
        synchronized (this) {
            copy = (Vector<Content>) vector.clone();
        }
        QueryRetVO res = new QueryRetVO();
        res.setCount(0L);
        res.setLength(0L);
        res.setIp(queryVO.getSrcIp());
        res.setPort(queryVO.getSrcPort());
        for(Content c : copy){
            if(c.getProtocol().toString().contains(queryVO.getType())){
                if(c.getDstAddressPort().contains(queryVO.getDstIp()+":"+queryVO.getDstPort())){
                    res.setLength(res.getLength()+c.getLength());
                    res.setCount(res.getCount()+1);
                }
            }
        }
        String r = null;
        try {
            r = objectMapper.writeValueAsString(res);
        }
        catch(JsonProcessingException j) {

        }
        return r;
    }

}
