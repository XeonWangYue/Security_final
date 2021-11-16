package top.xeonwang.securityfinal.Util;


import org.pcap4j.packet.*;
import org.pcap4j.packet.namednumber.*;
import lombok.extern.slf4j.Slf4j;
import org.pcap4j.util.MacAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import top.xeonwang.securityfinal.PO.NetLog;
import top.xeonwang.securityfinal.Repository.NetLogRepo;
import top.xeonwang.securityfinal.Service.PcapOpsService;
import top.xeonwang.securityfinal.Util.Interface.IDataReceiveEventListener;

import java.net.Inet4Address;
import java.net.Inet6Address;

/**
 * @author Song Y.
 * @Edit and change to SQL Chen Q.
 */
@Slf4j
@Service
public class MyEventListener implements IDataReceiveEventListener {
    @Autowired
    private NetLogRepo repo;

    @Override
    public void receive(DataReceiveEvent event) {
        Long tstart = System.currentTimeMillis(), tend;
        Packet packet = (Packet) event.getSource();
        EthernetPacket ethernetPacket = packet.get(EthernetPacket.class);
        NetLog netLog = new NetLog();
        if (ethernetPacket == null) {
            log.debug("不是以太网数据包");
            return;
        }
        EtherType type = ethernetPacket.getHeader().getType();
        int length = ethernetPacket.length();

        String srcAddr = ethernetPacket.getHeader().getSrcAddr().toString();
        String dstAddr = ethernetPacket.getHeader().getDstAddr().toString();

        IpNumber iptype = null;

        Integer dstPort = null;
        Integer srcPort = null;

        TransportPacket trans = null;
        IpPacket ipPacket = null;
        if (type == EtherType.IPV4 || type == EtherType.IPV6) {
            ipPacket = ethernetPacket.get(IpPacket.class);
            srcAddr = ipPacket.getHeader().getSrcAddr().getHostAddress();
            dstAddr = ipPacket.getHeader().getDstAddr().getHostAddress();
            trans = ipPacket.getPayload().get(TransportPacket.class);
            iptype = ipPacket.getHeader().getProtocol();
            if (type == EtherType.IPV4) {
                length = ((IpV4Packet) ipPacket).getHeader().getTotalLengthAsInt();
            }
            if (type == EtherType.IPV6) {
                length = ((IpV6Packet) ipPacket).getHeader().getPayloadLengthAsInt();
            }
        }
        if (trans != null && (iptype == IpNumber.TCP || iptype == IpNumber.UDP)) {
            dstPort = trans.getHeader().getDstPort().valueAsInt();
            srcPort = trans.getHeader().getSrcPort().valueAsInt();
        }

        netLog.setTime(System.currentTimeMillis());
        netLog.setType(iptype == null ? type.toString() : iptype.toString());
        netLog.setLength(length);
        netLog.setSrcAddr(srcAddr);
        netLog.setDstAddr(dstAddr);
        netLog.setSrcPort(srcPort);
        netLog.setDstPort(dstPort);

        try {
            repo.save(netLog);
            log.debug("insert id: " + netLog.getId() + " proto: " + netLog.getType());
        } catch (Exception e) {
            log.error("h2 database fail");
        }
        tend = System.currentTimeMillis();
        log.debug("入库耗时：" + (tend - tstart));
    }
}
