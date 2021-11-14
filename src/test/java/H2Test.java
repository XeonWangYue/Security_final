import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Order;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.xeonwang.securityfinal.PO.NetLog;
import top.xeonwang.securityfinal.Repository.NetLogRepo;
import top.xeonwang.securityfinal.SecurityFinalApplication;

@SpringBootTest(classes = SecurityFinalApplication.class)
@RunWith(SpringRunner.class)
@Slf4j
public class H2Test {
    @Autowired
    private NetLogRepo netLogRepo;

    @Order(1)
    @Test
    public void saveTest() {
        NetLog netLog = new NetLog();
        netLog.setDstPort("9097");
        netLog.setDstAddr("192.168.17.141");
        netLog.setSrcAddr("192.168.17.140");
        netLog.setSrcPort("8586");
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
}
