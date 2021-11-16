import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Order;
import org.junit.runner.RunWith;
import org.pcap4j.packet.namednumber.Port;
import org.pcap4j.packet.namednumber.TcpPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;
import top.xeonwang.securityfinal.PO.NetLog;
import top.xeonwang.securityfinal.Repository.NetLogRepo;
import top.xeonwang.securityfinal.SecurityFinalApplication;
import top.xeonwang.securityfinal.Service.H2NetLogService;
import top.xeonwang.securityfinal.Service.SystemManageService;
import top.xeonwang.securityfinal.Util.BPF;
import top.xeonwang.securityfinal.VO.PackageCountVO;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = SecurityFinalApplication.class)
@RunWith(SpringRunner.class)
@Slf4j
public class H2Test {
    @Autowired
    private NetLogRepo netLogRepo;

    @Autowired
    H2NetLogService h2NetLogService;

    @Autowired
    SystemManageService systemManageService;

    @Order(1)
    @Test
    public void saveTest() {
        NetLog netLog = new NetLog();
        netLog.setDstPort(9097);
        netLog.setDstAddr("192.168.17.141");
        netLog.setSrcAddr("192.168.17.140");
        netLog.setSrcPort(8586);
        netLog.setLength(55);
        netLog.setType("HTTP");
        log.info("save test");
        netLogRepo.save(netLog);
        Assert.assertNotNull(netLog.getId());
    }

    @Order(2)
    @Test
    public void findOneTest() throws Exception {
        NetLog netLog;
        netLog = netLogRepo.getById(1l);
        log.info(netLog.toString());
        Assert.assertNotNull(netLog);
        Assert.assertTrue(1l == netLog.getId());
    }

    @Test
    public void likeTest1() throws Exception {
        Long tstart = System.currentTimeMillis(), tend;
        String s = h2NetLogService.getPackCount("192.168.17.141");
        ObjectMapper mapper = new ObjectMapper();
        PackageCountVO vo = mapper.readValue(s, PackageCountVO.class);
        tend = System.currentTimeMillis();
        log.info("耗时时常: " + (tend - tstart) + " 结果: " + "udp: " + vo.getUdp() + " tcp: " + vo.getTcp() + " arp: " + vo.getArp() + " icmp " + vo.getIcmp());
        Assert.assertNotNull(vo);
        Assert.assertTrue(vo.getUdp() > 0);
        Assert.assertTrue(vo.getArp() >= 0);
        Assert.assertTrue(vo.getIcmp() >= 0);
        Assert.assertTrue(vo.getTcp() > 0);
    }

    @Test
    public void hostsTest() throws Exception {
        String s = systemManageService.getHosts();
        ObjectMapper mapper = new ObjectMapper();
        List list = mapper.readValue(s, List.class);
        Assert.assertTrue(list.size() > 0);
        String str = (String) list.get(0);
        Assert.assertTrue(str.charAt(0) != '/');
        Assert.assertTrue(str.equals("localhost"));
    }

    @Test
    public void timestamp() throws Exception {
        String hostname = "192.168.17.141";
        Long time = System.currentTimeMillis() - 10 * 60 * 1000;
        List<NetLog> list = netLogRepo.findLastStep(hostname, time);
        Long end = System.currentTimeMillis() - 10 * 60 * 1000;
        log.info("操作耗时: " + (end - time) + " size: " + list.size());
        Assert.assertTrue(list.size() > 0);
    }

//    @Test
//    public void liketest2() throws Exception {
//        String bpf = "host 35.232.111.17";
//        String bpf = "proto UDP";
//        String bpf = "port 80";
//        Object[] matcher = new BPF().getMatcher(bpf);
//        Example<NetLog> example = Example.of((NetLog) matcher[0], (ExampleMatcher) matcher[1]);
//        NetLog lg = (NetLog) matcher[0];
//
//        log.info(lg.toString());
//        log.info(((ExampleMatcher) matcher[1]).isAnyMatching() + " " + ((ExampleMatcher) matcher[1]).isIgnoreCaseEnabled());
//        List<NetLog> all = netLogRepo.findAll(example);
//        log.info("size: " + all.size());
//        Assert.assertTrue(all.size() > 0);
//    }

//    @Test
//    public void liketest3() throws Exception {
//        ExampleMatcher matcher= ExampleMatcher.matchingAny();
//        matcher.withMatcher("DST_PORT", ExampleMatcher.GenericPropertyMatcher::startsWith);
//        NetLog netLog = new NetLog();
//        String str = TcpPort.HTTP.toString();
//        log.info(TcpPort.HTTP.toString());
//        netLog.setDstPort(80);
//        Example<NetLog> example = Example.of(netLog, matcher);
//        List<NetLog> all = netLogRepo.findAll(example);
//        Assert.assertTrue(all.size() > 0);
//    }

    @Test
    public void liketest4() throws Exception {
        BPF bpf = new BPF();
        List<NetLog> list = null;
        if (bpf.getTuple("src host 36.152.44.96 and port 80") != null) {
            list = netLogRepo.findAll(bpf);
        }
        log.info(list.size()+"");
        Assert.assertTrue(list != null);
        Assert.assertTrue(list.size() > 0);
    }
}
