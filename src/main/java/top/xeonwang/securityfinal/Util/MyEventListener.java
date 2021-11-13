package top.xeonwang.securityfinal.Util;


import org.pcap4j.packet.*;
import org.pcap4j.packet.namednumber.IpNumber;
import org.pcap4j.packet.namednumber.TcpPort;
import org.pcap4j.packet.namednumber.UdpPort;
import lombok.extern.slf4j.Slf4j;
import org.pcap4j.packet.namednumber.EtherType;
import org.springframework.stereotype.Component;
import top.xeonwang.securityfinal.Service.RedisAutoSaveService;
import top.xeonwang.securityfinal.Util.Interface.IDataReceiveEventListener;

import java.net.Inet4Address;
import java.net.Inet6Address;

@Slf4j
@Component
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
            //log.info("报文类型: " + type);
        }

        Content content = new Content();
        content.setLength(packet.length());
        content.setProtocol(type);
        if (type.equals(EtherType.IPV4)) {
            IpV4Packet ipV4Packet = packet.get(IpV4Packet.class);
            IpV4Packet.IpV4Header ipV4Header = ipV4Packet.getHeader();
            Inet4Address srcAddr = ipV4Header.getSrcAddr();
            Inet4Address dstAddr = ipV4Header.getDstAddr();
            if(ipV4Header.getProtocol() == IpNumber.TCP){
                TcpPacket tcpPacket = packet.get(TcpPacket.class);
                TcpPacket.TcpHeader tcpHeader = tcpPacket.getHeader();
                TcpPort srcPort = tcpHeader.getSrcPort();
                TcpPort dstPort = tcpHeader.getDstPort();
                content.setSrcAddressPort(srcAddr.getHostAddress()+":"+srcPort.toString());
                content.setDstAddressPort(dstAddr.getHostAddress()+":"+dstPort.toString());

            }
            else if(ipV4Header.getProtocol() == IpNumber.UDP){
                UdpPacket udpPacket = packet.get(UdpPacket.class);
                UdpPacket.UdpHeader udpHeader = udpPacket.getHeader();
                UdpPort srcPort = udpHeader.getSrcPort();
                UdpPort dstPort = udpHeader.getDstPort();
                content.setSrcAddressPort(srcAddr.getHostAddress()+":"+srcPort.toString());
                content.setDstAddressPort(dstAddr.getHostAddress()+":"+dstPort.toString());
            }
            RedisAutoSaveService.vector.add(content);
            int len = ipV4Packet.getHeader().getTotalLengthAsInt();
            log.debug("src: " + content.srcAddressPort + " dst: " + content.dstAddressPort + " len: " + len);
        }
        else if(type.equals(EtherType.IPV6)){
            IpV6Packet ipV6Packet = packet.get(IpV6Packet.class);
            IpV6Packet.IpV6Header ipV6Header = ipV6Packet.getHeader();
            Inet6Address srcAddr = ipV6Header.getSrcAddr();
            Inet6Address dstAddr = ipV6Header.getDstAddr();
            if(ipV6Header.getProtocol() == IpNumber.TCP){
                TcpPacket tcpPacket = packet.get(TcpPacket.class);
                TcpPacket.TcpHeader tcpHeader = tcpPacket.getHeader();
                TcpPort srcPort = tcpHeader.getSrcPort();
                TcpPort dstPort = tcpHeader.getDstPort();
                content.setSrcAddressPort(srcAddr.getHostAddress()+":"+srcPort.toString());
                content.setDstAddressPort(dstAddr.getHostAddress()+":"+dstPort.toString());
                RedisAutoSaveService.vector.add(content);
            }
            else if(ipV6Header.getProtocol() == IpNumber.UDP){
                UdpPacket udpPacket = packet.get(UdpPacket.class);
                UdpPacket.UdpHeader udpHeader = udpPacket.getHeader();
                UdpPort srcPort = udpHeader.getSrcPort();
                UdpPort dstPort = udpHeader.getDstPort();
                content.setSrcAddressPort(srcAddr.getHostAddress()+":"+srcPort.toString());
                content.setDstAddressPort(dstAddr.getHostAddress()+":"+dstPort.toString());
                RedisAutoSaveService.vector.add(content);
            }
        }
    }
}
