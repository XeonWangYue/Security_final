package top.xeonwang.securityfinal.Util;

import java.util.EventListener;

/**
 * @author Chen Q.
 */
public interface IDataReceiveEventListener extends EventListener {
    void receive(DataReceiveEvent event);
}
