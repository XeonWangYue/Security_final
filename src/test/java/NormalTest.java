import top.xeonwang.securityfinal.Util.BPF;

import java.util.List;

public class NormalTest {
    public static void main(String[] args) {
        String str = "not src host 192.168.17.141 && !port >=144 and !src port <5555";
        List<String> tuple = new BPF().getTuple(str);
        for (String o : tuple) {
            if (o.equalsIgnoreCase("and") || o.equalsIgnoreCase("or")) {
                System.out.println("");
                System.out.println(o);
            } else {
                System.out.print(o + " ");
            }
        }
        System.out.println("");
        try {
            List<String> port = BPF.getPort(">9090");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
