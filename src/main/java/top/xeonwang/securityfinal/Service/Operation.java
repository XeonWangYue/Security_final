package top.xeonwang.securityfinal.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GraphicsCard;
import oshi.hardware.HardwareAbstractionLayer;

/**
 * @author Chen Q.
 */
@Slf4j
@Service
public class Operation {
    public void getInfo() {
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        CentralProcessor cpu = hal.getProcessor();
        GraphicsCard gpu = hal.getGraphicsCards().get(1);
        log.info(cpu.toString());
        log.info(gpu.toString());
    }
}
