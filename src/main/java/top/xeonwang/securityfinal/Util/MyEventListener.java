package top.xeonwang.securityfinal.Util;

import lombok.extern.slf4j.Slf4j;
import org.pcap4j.packet.ArpPacket;
import org.pcap4j.packet.EthernetPacket;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.namednumber.EtherType;
import org.pcap4j.packet.namednumber.IpV4OptionType;

import java.net.Inet4Address;

@Slf4j
public class MyEventListener implements IDataReceiveEventListener {
    @Override
    public void receive(DataReceiveEvent event) {
        Packet packet = (Packet) event.getSource();
        EtherType type;
        EthernetPacket ethernetPacket = packet.get(EthernetPacket.class);
        if (ethernetPacket == null) {
            log.error("不是以太网数据包");
            return;
        } else {
            type = ethernetPacket.getHeader().getType();
            log.info("报文类型: " + type);
        }

        if (type.equals(EtherType.IPV4)) {
            IpV4Packet ipV4Packet = packet.get(IpV4Packet.class);
            Inet4Address srcAddr = ipV4Packet.getHeader().getSrcAddr();
            Inet4Address dstAddr = ipV4Packet.getHeader().getDstAddr();
            int len = ipV4Packet.getHeader().getTotalLengthAsInt();
            log.info("src: " + srcAddr + " dst: " + dstAddr + " len: " + len);
        }
    }
}
