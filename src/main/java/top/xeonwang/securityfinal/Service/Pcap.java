package top.xeonwang.securityfinal.Service;

import lombok.extern.slf4j.Slf4j;
import org.pcap4j.core.*;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.Packet;
import org.springframework.stereotype.Service;

import java.io.EOFException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

/**
 * @author Chen Q.
 */
@Slf4j
//@Service
public class Pcap {
    InetAddress addr;
    PcapNetworkInterface nif;
    PcapHandle handle;
    public Pcap(){
        try {
            addr = InetAddress.getByName("192.168.17.140");
            nif = Pcaps.getDevByAddress(addr);
            int snapLen = 65536;
            PcapNetworkInterface.PromiscuousMode mode = PcapNetworkInterface.PromiscuousMode.PROMISCUOUS;
            int timeout = 1000;
            handle = nif.openLive(snapLen, mode, timeout);
            while(true){
                Packet packet = handle.getNextPacketEx();
                IpV4Packet ipV4Packet = packet.get(IpV4Packet.class);
                Inet4Address srcAddr = ipV4Packet.getHeader().getSrcAddr();
                log.info(srcAddr.toString());
            }
        } catch (PcapNativeException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (NotOpenException e) {
            e.printStackTrace();
        } catch (EOFException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        finally {
            handle.close();
        }
    }
}
