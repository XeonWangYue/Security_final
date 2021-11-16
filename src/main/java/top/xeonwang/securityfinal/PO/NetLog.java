package top.xeonwang.securityfinal.PO;

import lombok.Data;
import lombok.ToString;
import top.xeonwang.securityfinal.VO.QueryRetVO;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Chen Q.
 */
@Entity
@Table(name = "netlog")
@Data
@ToString
public class NetLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long time;

    private String srcAddr;
    private String dstAddr;

    private Integer srcPort;
    private Integer dstPort;

    private Integer length;
    private String type;

}
