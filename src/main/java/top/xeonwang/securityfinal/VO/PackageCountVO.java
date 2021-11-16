package top.xeonwang.securityfinal.VO;

import lombok.Data;

/**
 * @author Chen Q.
 */
@Data
public class PackageCountVO {
    private Long udp;
    private Long arp;
    private Long icmp;
    private Long tcp;
}
