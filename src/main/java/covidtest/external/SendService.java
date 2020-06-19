
package covidtest.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

@FeignClient(name="kitSend", url="http://kitSend:8080")
public interface SendService {

    @RequestMapping(method= RequestMethod.POST, path="/sends")
    public void send(@RequestBody Send send);

}