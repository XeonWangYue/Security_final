package top.xeonwang.securityfinal.Util;

import lombok.extern.slf4j.Slf4j;
import org.pcap4j.core.*;
import top.xeonwang.securityfinal.Util.Interface.IDataExchanger;
import top.xeonwang.securityfinal.Component.DataReceiveSupport;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Chen Q.
 */
@Slf4j
public class ReceiveDataExchanger implements IDataExchanger {

    private PcapNetworkInterface netCardInf;
    private PcapHandle handle;
    private DataReceiveSupport support;

    private static final int TIMEOUT = 50;
    private static final int SNAPLEN = 65535;

    private static String nothost = "!host ";
    private static String and = " and ";
    private static String ether = "!ether host ";
    private static String filter = null;

    public ReceiveDataExchanger(InetAddress addr, DataReceiveSupport support) throws
            PcapNativeException, NullPointerException, UnknownHostException {
        netCardInf = Pcaps.getDevByAddress(addr);
        String localv6 = netCardInf.getAddresses().get(0).getAddress().getHostAddress();
        String localv4 = addr.getHostAddress();
        filter = nothost + localv6 + and + nothost + localv4;
        log.info("bpf过滤器 " + filter);
        this.support = support;
        if (netCardInf == null) {
            throw new NullPointerException("网卡实例为空");
        }
        if (support == null) {
            throw new NullPointerException("support为空");
        }

    }

    @Override
    public void start() {
        try {
            handle = netCardInf.openLive(SNAPLEN,
                    PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, TIMEOUT);
            if (null == handle) {
                return;
            }
            if (filter.length() != 0) {
                handle.setFilter(filter, BpfProgram.BpfCompileMode.OPTIMIZE);
            }
            handle.loop(-1, (PacketListener) packet ->
                    support.receiveData(packet));
        } catch (PcapNativeException| NotOpenException e) {
            log.error("Pcap not open exception in start");
        }
        catch (InterruptedException e){
            log.info("Pcap has been interrupted");
        }
        return;
    }

    @Override
    public void close() {
        try {
            handle.breakLoop();
        } catch (NotOpenException e) {
            log.error("Pcap not open exception in close");
        }
    }

    @Override
    public boolean hasNext() {
        boolean nullFlag = true;
        try {
            nullFlag = handle.getNextPacket() == null;
        } catch (NotOpenException e) {
            log.error("handler not open");
        }
        return !nullFlag;
    }
}
