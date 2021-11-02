package top.xeonwang.securityfinal.Controller;

import com.alibaba.fastjson.JSON;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Chen Q.
 */
@RestController
public class DataController {
    @RequestMapping(method = RequestMethod.GET, path = "/test")
    public String test() {
        return JSON.toJSONString("yes");
    }

    @RequestMapping(method = RequestMethod.GET, path = "/test2")
    public String test2() {
        return JSON.toJSONString("no");
    }
}
