package top.xeonwang.securityfinal.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.xeonwang.securityfinal.Component.Redis;
import top.xeonwang.securityfinal.Service.H2NetLogService;
import top.xeonwang.securityfinal.Service.PcapOpsService;
import top.xeonwang.securityfinal.VO.QueryVO;

/**
 * @author Chen Q.
 */
@Slf4j
@RestController
public class DataController {
    @Autowired
    Redis redisTest;

    @Autowired
    PcapOpsService service;

    @Autowired
    H2NetLogService h2NetLogService;

    ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping(method = RequestMethod.GET, path = "/test")
    public String test() {
        ObjectMapper mapper = new ObjectMapper();
        String s = "yes";
        String ret = null;
        try {
            ret = mapper.writeValueAsString(s);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return ret;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/test2")
    public String test2() {
        return "";
    }

    @RequestMapping(method = RequestMethod.POST, path = "/test3")
    public String test3() {
        return "";
    }

    @RequestMapping(method = RequestMethod.POST, path = "/packs")
    public String packscount(@RequestParam String hostname) {
        return h2NetLogService.getPackCount(hostname);
    }

    @RequestMapping(method = RequestMethod.POST, path ="/speed")
    public String speedCount(@RequestParam String hostname){
        return h2NetLogService.packsOneSecond(hostname);
    }

    @RequestMapping(method = RequestMethod.POST, path ="/search")
    public String searchPack(@RequestParam String bpf){
        return h2NetLogService.packGetOnce(bpf);
    }


    @RequestMapping(method = RequestMethod.POST, path = "/searchBySrcIp")
    public String searchBySrcIp(@RequestBody QueryVO vo) {
        return service.searchBySrcIp(vo);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/searchBySrcIpPort")
    public String searchBySrcIpPort(@RequestBody QueryVO vo) {
        return service.searchBySrcIpPort(vo);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/searchByDstIp")
    public String searchByDstIp(@RequestBody QueryVO vo) {
        return service.searchByDstIp(vo);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/searchByDstIpPort")
    public String searchByDstIpPort(@RequestBody QueryVO vo) {
        return service.searchByDstIpPort(vo);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/searchByProtocol")
    public String searchByProtocol(@RequestBody QueryVO vo) {
        return service.searchByProtocol(vo);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/searchBySrcIpProtocol")
    public String searchBySrcIpProtocol(@RequestBody QueryVO vo) {
        return service.searchBySrcIpProtocol(vo);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/searchByDstIpProtocol")
    public String searchByDstIpProtocol(@RequestBody QueryVO vo) {
        return service.searchByDstIpProtocol(vo);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/searchBySrcIpPortProtocol")
    public String searchBySrcIpPortProtocol(@RequestBody QueryVO vo) {
        return service.searchBySrcIpPortProtocol(vo);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/searchByDstIpPortProtocol")
    public String searchByDstIpPortProtocol(@RequestBody QueryVO vo) {
        return service.searchByDstIpPortProtocol(vo);
    }
}
