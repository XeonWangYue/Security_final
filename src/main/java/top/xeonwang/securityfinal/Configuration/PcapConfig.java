package top.xeonwang.securityfinal.Configuration;

import lombok.extern.slf4j.Slf4j;
import org.pcap4j.core.PcapNativeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import top.xeonwang.securityfinal.Bean.ReceiveDataExchanger;
import top.xeonwang.securityfinal.Util.DataReceiveSupport;
import top.xeonwang.securityfinal.Util.MyEventListener;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Chen Q.
 */
@Slf4j
@Configuration
public class PcapConfig {
    @Autowired
    DataReceiveSupport support;

    @Value(value = "${pcap.ipaddr}")
    private String addr;

    @Bean
    public ReceiveDataExchanger getDataExchanger() {
        try {
            log.info("bind ip: " + addr);
            InetAddress ip = InetAddress.getByName(addr);
            ReceiveDataExchanger exchanger = new ReceiveDataExchanger(ip, support);
            support.addListener(new MyEventListener());
            exchanger.start();
            return exchanger;
        } catch (PcapNativeException e) {
            log.error("PcapNativeException");
        } catch (UnknownHostException e) {
            log.error("UnknownHostException");
        }
        return null;
    }

    public PcapConfig() {
        log.info("pacp start");
    }
}
