package main.java.hello;

import org.springframework.mobile.device.Device;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DetectDeviceController {
    @RequestMapping(path="/detect", method=RequestMethod.GET)
    public @ResponseBody String detectDevice(Device device) {

        String returnMessage = "Hello %s browser!";
        if (device.isNormal())
            return String.format(returnMessage, "normal");
        else if (device.isMobile())
            return String.format(returnMessage, "mobile");
        else if (device.isTablet())
            return String.format(returnMessage, "tablet");
        else
            return String.format(returnMessage, "unknown");
    }
}
