package top.xeonwang.securityfinal.VO;

import lombok.Data;

/**
 * @author Song Y.
 */
@Data
public class QueryRetVO {
    String ip;
    String port;
    Long length;
    Long count;
}
