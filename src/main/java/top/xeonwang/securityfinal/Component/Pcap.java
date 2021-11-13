package top.xeonwang.securityfinal.Component;

import lombok.extern.slf4j.Slf4j;
import org.pcap4j.core.PcapNativeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import top.xeonwang.securityfinal.Component.DataReceiveSupport;
import top.xeonwang.securityfinal.Util.ReceiveDataExchanger;
import top.xeonwang.securityfinal.Util.MyEventListener;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Chen Q.
 */
@Slf4j
@Component
public class Pcap implements ApplicationRunner {
    @Autowired
    MyEventListener eventListener;

    @Autowired
    DataReceiveSupport support;

    @Value(value = "${pcap.ipaddr}")
    private String addr;


    public void start() {
        try {
            log.debug("bind ip: " + addr);
            InetAddress ip = InetAddress.getByName(addr);
            ReceiveDataExchanger exchanger = new ReceiveDataExchanger(ip, support);
            support.addListener(eventListener);
            exchanger.start();
        } catch (PcapNativeException e) {
            log.error("PcapNativeException");
        } catch (UnknownHostException e) {
            log.error("UnknownHostException");
        }
    }
    @Order(2)
    @Async("thread")
    @Override
    public void run(ApplicationArguments args) throws Exception {
//        Thread.sleep(5000);
//        log.info("启动pcap捕获服务");
//        start();
    }
}
