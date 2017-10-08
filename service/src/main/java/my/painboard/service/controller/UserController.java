package my.painboard.service.controller;

import java.util.ArrayList;
import java.util.List;
import my.painboard.db.model.User;
import my.painboard.db.service.UserService;
import my.painboard.service.dto.ActionResult;
import my.painboard.service.dto.UIUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/hello")
    public String hellohere() {
        return "Helloworld";
    }

    @RequestMapping("/list")
    public
    @ResponseBody
    List<UIUser> list() {
        List<UIUser> res = new ArrayList<>();
        for (User item : userService.list()) {
            res.add(new UIUser(item.getUuid(), item.getName(), item.getTeam().getName()));
        }
        return res;
    }

    @RequestMapping(value = "/modify", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    public
    @ResponseBody
    ActionResult addUser(@RequestBody UIUser user) {
        if(user.getUuid() == null) {
            userService.create(user.getName(), user.getTeam());
        }else {
            userService.update(user.getUuid(), user.getName(), user.getTeam());
        }
        return new ActionResult(true, "Seems that ok");
    }


    @RequestMapping("/remove/{uuid}")
    public
    @ResponseBody
    ActionResult removeUser(@PathVariable String uuid) {
        userService.remove(uuid);
        return new ActionResult(true, "Seems that ok");
    }
}
