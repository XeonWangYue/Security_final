package top.xeonwang.securityfinal.Util.Interface;

import top.xeonwang.securityfinal.Util.DataReceiveEvent;

import java.util.EventListener;

/**
 * @author Chen Q.
 */
public interface IDataReceiveEventListener extends EventListener {
    void receive(DataReceiveEvent event);
}
