package top.xeonwang.securityfinal.VO;

import com.sun.management.OperatingSystemMXBean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

import java.io.Serializable;
import java.lang.management.ManagementFactory;

/**
 * @author Chen Q.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemInfoVO implements Serializable {
    private Double cpuLoadPercentage;
    private String platform;
    private Long uptime;
    private Double memory;

    @Override
    public String toString() {
        return " platform: " + platform + " uptime: " + uptime + " percent: " + cpuLoadPercentage + " Memory: " + memory;
    }

    public static SystemInfoVO toSystemInfo() {
        SystemInfoVO vo = new SystemInfoVO();

        OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        double cpuLoad = osmxb.getSystemCpuLoad();

        Double percentCpuLoad = (cpuLoad * 100);

        SystemInfo si = new SystemInfo();
        OperatingSystem os = si.getOperatingSystem();
        HardwareAbstractionLayer hal = si.getHardware();
        CentralProcessor cpu = hal.getProcessor();

        double totalvirtualMemory = osmxb.getTotalPhysicalMemorySize();
        double freePhysicalMemorySize = osmxb.getFreePhysicalMemorySize();
        double value = freePhysicalMemorySize / totalvirtualMemory;
        Double percentMemoryLoad = ((1 - value) * 100);

        vo.setPlatform(os.getFamily());
        vo.setCpuLoadPercentage(percentCpuLoad);
        vo.setUptime(os.getSystemUptime());
        vo.setMemory(percentMemoryLoad);
        return vo;
    }
}
