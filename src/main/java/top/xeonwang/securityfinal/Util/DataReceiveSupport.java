package top.xeonwang.securityfinal.Util;

import com.google.common.collect.Sets;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author Chen Q.
 */
@Service
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
