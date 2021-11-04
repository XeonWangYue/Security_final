package top.xeonwang.securityfinal.Util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.pcap4j.packet.namednumber.EtherType;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RedisContent implements Serializable {
    public EtherType protocol;
    public String srcAddress;
    public String dstAddress;
    public String srcPort;
    public String dstPort;
    public int length;
}
