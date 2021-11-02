package top.xeonwang.securityfinal.Util;

import java.util.EventObject;

/**
 * @author Chen Q.
 */
public class DataReceiveEvent extends EventObject implements Cloneable {
    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public DataReceiveEvent(Object source) {
        super(source);
    }
}
