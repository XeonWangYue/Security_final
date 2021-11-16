package top.xeonwang.securityfinal.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import top.xeonwang.securityfinal.PO.NetLog;

import java.util.List;

/**
 * @author Chen Q.
 */
@Repository
public interface NetLogRepo extends JpaRepository<NetLog, Long>, JpaSpecificationExecutor<NetLog> {
    @Query(value = "select netlog from NetLog netlog where netlog.time>?2 and (netlog.srcAddr like ?1% or netlog.dstAddr like ?1%)")
    public List<NetLog> findLastStep(String hostname, Long start);
}
