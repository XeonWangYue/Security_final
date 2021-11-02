package top.xeonwang.securityfinal.Bean.Interface;

public interface IDataExchanger {
    boolean start();
    void close();
    boolean hasNext();
}
