package top.xeonwang.securityfinal.Component;

import com.google.common.collect.Sets;
import org.springframework.stereotype.Component;
import top.xeonwang.securityfinal.Util.DataReceiveEvent;
import top.xeonwang.securityfinal.Util.Interface.IDataReceiveEventListener;

import java.util.Set;

/**
 * @author Chen Q.
 */
@Component
public final class DataReceiveSupport {
    private Set<IDataReceiveEventListener> listeners;

    public DataReceiveSupport() {
        listeners = Sets.newConcurrentHashSet();
    }

    public void addListener(IDataReceiveEventListener eventListener) {
        listeners.add(eventListener);
    }

    public void receiveData(Object source) {
        listeners.forEach(listener -> {
            listener.receive(new DataReceiveEvent(source));
        });
    }
}
