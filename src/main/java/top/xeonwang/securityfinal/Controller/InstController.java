package top.xeonwang.securityfinal.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import top.xeonwang.securityfinal.Service.SystemManageService;

/**
 * @author Chen Q.
 */
@RestController
public class InstController {
    @Autowired
    SystemManageService systemManageService;

    @RequestMapping(method = RequestMethod.POST, path = "/system")
    public String getSystemInfo() {
        return systemManageService.getSystemInfo();
    }

//    @RequestMapping(method = RequestMethod.POST, path = "/getCpu")
//    public String getCpu() {
//        String s = systemManageService.getSystemInfo();
//        ObjectMapper mapper = new ObjectMapper();
//        String res = null;
//        try {
//            res = mapper.writeValueAsString(s);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        return res != null ? res : "";
//    }
}
