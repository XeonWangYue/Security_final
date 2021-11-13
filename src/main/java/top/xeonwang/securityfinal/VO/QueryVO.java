package top.xeonwang.securityfinal.VO;

import lombok.Data;
import org.pcap4j.packet.namednumber.EtherType;

@Data
public class QueryVO {
    private String srcIp;
    private String dstIp;
    private String srcPort;
    private String dstPort;
    private String type;
}
