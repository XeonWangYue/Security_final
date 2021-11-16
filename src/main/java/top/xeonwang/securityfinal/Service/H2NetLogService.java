package top.xeonwang.securityfinal.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.pcap4j.packet.namednumber.EtherType;
import org.pcap4j.packet.namednumber.IpNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import top.xeonwang.securityfinal.PO.NetLog;
import top.xeonwang.securityfinal.Repository.NetLogRepo;
import top.xeonwang.securityfinal.Util.BPF;
import top.xeonwang.securityfinal.VO.PackageCountVO;
import top.xeonwang.securityfinal.VO.QueryRetVO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Chen Q.
 */
@Service
@Slf4j
public class H2NetLogService {
    @Autowired
    private NetLogRepo repo;

    private List<String> countProto;

    private Long lastcall;

    public H2NetLogService() {
        countProto = new ArrayList<String>();
        countProto.add(IpNumber.UDP.toString());
        countProto.add(IpNumber.TCP.toString());
        countProto.add(EtherType.ARP.toString());
        countProto.add(IpNumber.ICMPV4.toString());
        lastcall = System.currentTimeMillis();
    }

    public String getPackCount(String hostname) {
        NetLog netLog = new NetLog();
        PackageCountVO vo = new PackageCountVO();
        for (String type : countProto) {

            ExampleMatcher matcher = ExampleMatcher.matching();
            matcher = matcher.withMatcher("SRC_ADDR", ExampleMatcher.GenericPropertyMatcher::contains)
                    .withMatcher("TYPE", ExampleMatcher.GenericPropertyMatcher::contains);
//                .withMatcher("TIME",)
            netLog.setSrcAddr(hostname);
            netLog.setType(type);
            Example<NetLog> example = Example.of(netLog, matcher);
            Long count = 0L;
            try {
                count = repo.count(example);
            } catch (Exception e) {
                log.error("get udp JPA error");
            }
            if (type.equals(IpNumber.UDP.toString())) {
                vo.setUdp(count);
            }
            if (type.equals(IpNumber.TCP.toString())) {
                vo.setTcp(count);
            }
            if (type.equals(IpNumber.ICMPV4.toString())) {
                vo.setIcmp(count);
            }
            if (type.equals(EtherType.ARP.toString())) {
                vo.setArp(count);
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(vo);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String packsOneSecond(String hostname) {
        Long now = System.currentTimeMillis() - 3000;
        List<NetLog> list = repo.findLastStep(hostname, now);
        Long len = 0L;
        for (NetLog one : list) {
            len += one.getLength();
        }
        log.debug("length: " + len);
        QueryRetVO vo = new QueryRetVO();
        vo.setLength(len);
        vo.setIp(hostname);
        vo.setPort("any");
        vo.setCount((long) list.size());
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(vo);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String packGetOnce(String bpf) {
        BPF bpfi = new BPF();
        List<NetLog> list = null;
        if (bpfi.getTuple(bpf) != null) {
            list = repo.findAll(bpfi);
        }
        log.debug("size: "+list.size()+" ");
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
