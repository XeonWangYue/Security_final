package top.xeonwang.securityfinal.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;

@Slf4j
@Service
public class Operation {
    public void getInfo() {
        OperatingSystemMXBean osMxBean = ManagementFactory.getOperatingSystemMXBean();
        double cpu = osMxBean.getSystemLoadAverage();
        int cores = osMxBean.getAvailableProcessors();
        String arch = osMxBean.getArch();
        String name = osMxBean.getName();
        String version = osMxBean.getVersion();
        log.info("System: " + name + " arch: " + arch + " version: " + version + " cpu: " + cpu * 100 + "% cores: " + cores);
    }
}
