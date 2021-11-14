package top.xeonwang.securityfinal.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import top.xeonwang.securityfinal.PO.NetLog;
import top.xeonwang.securityfinal.Repository.NetLogRepo;

import java.util.List;

/**
 * @author Chen Q.
 */
@Service
@Slf4j
public class H2NetLogService {
    @Autowired
    private NetLogRepo repo;

    public String getUdp() {
        List<NetLog> netLogs = repo.selectByProto("17%");
        log.debug("select size = " + netLogs.size());
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.writeValueAsString(netLogs);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getTCP() {
        List<NetLog> netLogs = repo.selectByProto("6%");
        log.debug("select size = " + netLogs.size());
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.writeValueAsString(netLogs);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
