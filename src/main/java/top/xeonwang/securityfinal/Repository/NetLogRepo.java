package top.xeonwang.securityfinal.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import top.xeonwang.securityfinal.PO.NetLog;

import java.util.List;

/**
 * @author Chen Q.
 */
@Repository
public interface NetLogRepo extends JpaRepository<NetLog, Long> {
    @Query("SELECT e FROM NetLog e WHERE e.type like %?1")
    public List<NetLog> selectByProto(String pro);
}
