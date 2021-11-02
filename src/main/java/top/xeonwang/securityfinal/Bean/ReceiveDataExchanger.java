package top.xeonwang.securityfinal.Bean;

import lombok.extern.slf4j.Slf4j;
import org.pcap4j.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.xeonwang.securityfinal.Bean.Interface.IDataExchanger;
import top.xeonwang.securityfinal.Util.DataReceiveSupport;

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

    public static final int TIMEOUT = 50;
    public static final int SNAPLEN = 65535;

    public ReceiveDataExchanger(InetAddress addr, DataReceiveSupport support) throws
            PcapNativeException, NullPointerException, UnknownHostException {
        netCardInf = Pcaps.getDevByAddress(addr);
        this.support = support;
        if (netCardInf == null) {
            throw new NullPointerException("网卡实例为空");
        }
        if (support == null) {
            throw new NullPointerException("support为空");
        }

    }

    @Override
    public boolean start() {
        try {
            handle = netCardInf.openLive(SNAPLEN,
                    PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, TIMEOUT);
            if (null == handle) {
                return false;
            }
            handle.loop(-1, (PacketListener) packet ->
                    support.receiveData(packet));
        } catch (PcapNativeException | InterruptedException | NotOpenException e) {
            return false;
        }
        return true;
    }

    @Override
    public void close() {
        handle.close();
    }

    @Override
    public boolean hasNext() {
        boolean nullFlag = true;
        try {
            nullFlag = handle.getNextPacket() == null;
        } catch (NotOpenException e) {
            e.printStackTrace();
        }
        return !nullFlag;
    }
}
