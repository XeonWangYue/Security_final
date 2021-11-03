package top.xeonwang.securityfinal.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import top.xeonwang.securityfinal.Service.Operation;

@RestController
public class InstController {
    @Autowired
    Operation operation;

    @RequestMapping(method = RequestMethod.POST, path = "/system")
    public String getSystemInfo() {
        operation.getInfo();
        return "";
    }
}
