package top.xeonwang.securityfinal.PO;

import lombok.Data;
import top.xeonwang.securityfinal.VO.QueryRetVO;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Chen Q.
 */
@Entity
@Table(name = "netlog")
@Data
public class NetLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long time;

    private String srcAddr;
    private String dstAddr;

    private String srcPort;
    private String dstPort;

    private Integer length;
    private String type;
}
