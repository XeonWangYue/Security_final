package top.xeonwang.securityfinal.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import top.xeonwang.securityfinal.Component.Redis;

/**
 * @author Chen Q.
 */
@Slf4j
@RestController
public class DataController {
    @Autowired
    Redis redisTest;

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
        ObjectMapper mapper = new ObjectMapper();
        String s = "hello";
        String ret = null;
        try {
            ret = mapper.writeValueAsString(s);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return ret;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/test3")
    public String test3() {
        return "";
    }
}
