package top.xeonwang.securityfinal.Util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.pcap4j.packet.namednumber.EtherType;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Content implements Serializable {
    public EtherType protocol;
    public String srcAddressPort;
    public String dstAddressPort;
    public int length;
}
