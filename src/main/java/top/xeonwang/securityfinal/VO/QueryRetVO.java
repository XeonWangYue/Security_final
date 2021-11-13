package top.xeonwang.securityfinal.VO;

import lombok.Data;

@Data
public class QueryRetVO {
    String ip;
    String port;
    Long length;
    Long count;
}
