package top.xeonwang.securityfinal.Util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.data.jpa.domain.Specification;
import top.xeonwang.securityfinal.PO.NetLog;

import javax.persistence.criteria.*;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Chen Q.
 */
@Slf4j
public class BPF implements Specification<NetLog> {
    List<String> bpftuple;

    public List<String> getTuple(String bpf) {
        String[] ops = bpf.split(" ").clone();
        /**
         * state 0 = t/f
         * state 1 = s/d/b
         * state 2 = proto/host/port
         * state 3 = >  < == >= <= !=
         * state 4 = ips/ps/tcp/udp/ip/icmp/arp
         * state 5 = and/or
         * state -1 = error
         */
        List<String> tuple = new ArrayList<>();
        int i = 0;
        int state = 0;
        while (i < ops.length) {
            String s = ops[i];
            switch (state) {
                case 0: {
                    char c = s.charAt(0);
                    if (c == '!') {
                        tuple.add("false");
                        ops[i] = s.substring(1);
                    } else if (s.equals("not")) {
                        tuple.add("false");
                        i++;
                    } else {
                        tuple.add("true");
                    }
                    state = 1;
                    break;
                }
                case 1: {
                    if (s.equalsIgnoreCase("host")
                            || s.equalsIgnoreCase("port")
                            || s.equalsIgnoreCase("proto")) {
                        tuple.add("both");
                    } else if (s.equalsIgnoreCase("src")
                            || s.equalsIgnoreCase("dst")) {
                        tuple.add(s);
                        i++;
                    } else {
                        log.error("local 2");
                        log.info(s + " " + i);
                        state = -1;
                        break;
                    }
                    state = 2;
                    break;
                }
                case 2: {
                    if (s.equalsIgnoreCase("host")
                            || s.equalsIgnoreCase("port")
                            || s.equalsIgnoreCase("proto")) {
                        tuple.add(s);
                        i++;
                    } else {
                        log.error("type error");
                        state = -1;
                        break;
                    }
                    state = 3;
                    break;
                }
                case 3: {
                    if (tuple.get(tuple.size() - 1).equalsIgnoreCase("port")) {
                        if (s.equalsIgnoreCase("*") || s.equalsIgnoreCase("any")) {
                            tuple.add(s);
                            i++;
                        } else if (s.contains(">") || s.contains("<") || s.contains("=")) {
                            tuple.add(s);
                            i++;
                        } else if (Integer.valueOf(s) > 0 && Integer.valueOf(s) < 65536) {
                            tuple.add(s);
                            i++;
                        } else {
                            log.error("port error");
                            state = -1;
                            break;
                        }
                    } else if (tuple.get(tuple.size() - 1).equalsIgnoreCase("proto")) {
                        if (s.equalsIgnoreCase("tcp")
                                || s.equalsIgnoreCase("ip")
                                || s.equalsIgnoreCase("arp")
                                || s.equalsIgnoreCase("icmp")
                                || s.equalsIgnoreCase("udp")) {
                            tuple.add(s);
                            i++;
                        } else {
                            state = -1;
                            log.error("proto not support");
                            break;
                        }
                    } else if (tuple.get(tuple.size() - 1).equalsIgnoreCase("host")) {
                        try {
                            InetSocketAddress addr = new InetSocketAddress(s, 8090);
                            tuple.add(s);
                            i++;
                        } catch (Exception e) {
                            log.error("ip not legal");
                            state = -1;
                            break;
                        }
                    }
                    state = 4;
                    break;
                }
                case 4: {
                    if (s.equalsIgnoreCase("and")
                            || s.equalsIgnoreCase("&&")) {
                        tuple.add("and");
                        i++;
                    } else if (s.equalsIgnoreCase("or")
                            || s.equalsIgnoreCase("||")) {
                        tuple.add("or");
                        i++;
                    } else {
                        state = -1;
                        break;
                    }
                    state = 0;
                    break;
                }
                default: {
                    log.error("bpf匹配失败");
                    return null;
                }
            }
        }
        bpftuple = tuple;
        return tuple;
    }

    public static List<String> getPort(String port) {
        String oper = null, pot = null;
        try {
            Integer p = Integer.valueOf(port);
            oper = "=";
            pot = port;
        } catch (NumberFormatException e) {
            for (int i = 0; i < port.length(); i++) {
                if (Character.isDigit(port.charAt(i))) {
                    oper = port.substring(0, i);
                    pot = port.substring(i);
                    break;
                }
            }
        }
        if (oper == null || pot == null) {
            log.error("port不合法");
            return null;
        }
        System.out.println(oper + " " + pot);
        List<String> ret = new ArrayList<>();
        ret.add(oper);
        ret.add(pot);
        return ret;
    }

    @Override
    public Predicate toPredicate(Root<NetLog> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        if (this.bpftuple == null) {
            return null;
        }
        Path<String> psrcAddr = root.get("srcAddr");
        Path<String> pdstAddr = root.get("dstAddr");
        Path<String> psrcPort = root.get("srcPort");
        Path<String> pdstPort = root.get("srcPort");
        Path<String> ptime = root.get("time");
        Path<String> ptype = root.get("type");
        Path<String> plength = root.get("length");


        for (String o : bpftuple) {
            if (o.equalsIgnoreCase("and") || o.equalsIgnoreCase("or")) {
                System.out.println("");
                System.out.println(o);
            } else {
                System.out.print(o + " ");
            }
        }
        int concat = -1;
        Predicate pmain = null;
        for (int i = 0; i < bpftuple.size(); i += 4) {
            Predicate res = null, likesrc = null, likedst = null;
            if (bpftuple.get(i).equalsIgnoreCase("and")) {
                concat = 1;
                i++;
            } else if (bpftuple.get(i).equalsIgnoreCase("or")) {
                concat = 0;
                i++;
            } else {
                concat = -1;
            }
            boolean bool = bpftuple.get(i).equalsIgnoreCase("true") ? true : false;
            boolean src = true;
            boolean dst = true;
            if (bpftuple.get(i + 1).equalsIgnoreCase("src")) {
                dst = false;
            } else if (bpftuple.get(i + 1).equalsIgnoreCase("dst")) {
                src = false;
            }
            String todo = bpftuple.get(i + 2);
            String obj = bpftuple.get(i + 3);
            String st = "=";
            if (todo.equalsIgnoreCase("port")) {
                List<String> stobj = getPort(bpftuple.get(i + 3));
                obj = stobj.get(1);
                st = stobj.get(0);
            }
            if (todo.equalsIgnoreCase("host")) {
                likesrc = cb.like(psrcAddr, "%" + obj + "%");
                likedst = cb.like(pdstAddr, "%" + obj + "%");
                if (dst && src) {
                    log.debug("src&dst host match " + obj);
                    res = cb.or(likedst, likesrc);
                } else if (dst) {
                    log.debug("dst host match " + obj);
                    res = likedst;
                } else if (src) {
                    log.debug("src host match " + obj);
                    res = likesrc;
                }
            } else if (todo.equalsIgnoreCase("port")) {
                if (st.equalsIgnoreCase("=")) {
                    likesrc = cb.equal(psrcPort.as(Integer.class), Integer.valueOf(obj));
                    likedst = cb.equal(psrcPort.as(Integer.class), Integer.valueOf(obj));
                } else if (st.equalsIgnoreCase(">")) {
                    likesrc = cb.gt(psrcPort.as(Integer.class), Integer.valueOf(obj));
                    likedst = cb.gt(psrcPort.as(Integer.class), Integer.valueOf(obj));
                } else if (st.equalsIgnoreCase("<")) {
                    likesrc = cb.lt(psrcPort.as(Integer.class), Integer.valueOf(obj));
                    likedst = cb.lt(psrcPort.as(Integer.class), Integer.valueOf(obj));
                } else if (st.equalsIgnoreCase(">=")) {
                    likesrc = cb.ge(psrcPort.as(Integer.class), Integer.valueOf(obj));
                    likedst = cb.ge(psrcPort.as(Integer.class), Integer.valueOf(obj));
                } else if (st.equalsIgnoreCase("<=")) {
                    likesrc = cb.le(psrcPort.as(Integer.class), Integer.valueOf(obj));
                    likedst = cb.le(psrcPort.as(Integer.class), Integer.valueOf(obj));
                } else if (st.equalsIgnoreCase("!=")) {
                    likesrc = cb.notEqual(psrcPort.as(Integer.class), Integer.valueOf(obj));
                    likedst = cb.notEqual(psrcPort.as(Integer.class), Integer.valueOf(obj));
                }
                if (dst && src) {
                    log.debug("src&dst port match " + obj);
                    res = cb.or(likedst, likesrc);
                } else if (dst) {
                    log.debug("dst port match " + obj);
                    res = likedst;
                } else if (src) {
                    log.debug("src port match " + obj);
                    res = likesrc;
                }
            } else if (todo.equalsIgnoreCase("proto")) {
                res = cb.like(ptype, "%" + obj.toUpperCase() + "%");
                log.debug("proto match " + obj);
            }
            if (!bool) {
                res = cb.not(res);
            }
            if (concat == 1) {
                pmain = cb.and(pmain, res);
            } else if (concat == 0) {
                pmain = cb.or(pmain, res);
            } else {
                pmain = res;
            }
        }
        return pmain;
    }
}
