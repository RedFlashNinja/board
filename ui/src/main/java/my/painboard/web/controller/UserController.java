package my.painboard.web.controller;

import lombok.extern.slf4j.Slf4j;
import my.painboard.db.model.User;
import my.painboard.db.service.implementation.UserServiceImpl;
import my.painboard.db.service.implementation.UserTeamService;
import my.painboard.web.dto.ActionResult;
import my.painboard.web.dto.UIUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private UserTeamService userTeamService;

    @RequestMapping("/hello")
    public String hellohere() {
        return "Helloworld";
    }

    @RequestMapping("/list")
    @ResponseBody
    public List<UIUser> list() {
        List<UIUser> res = new ArrayList<>();
        for (User item : userServiceImpl.list()) {
            res.add(new UIUser(item, userTeamService.getAllByUser(item.getUuid())));
        }
        return res;
    }

    @RequestMapping("/get/{uuid}")
    @ResponseBody
    public UIUser get(@PathVariable String uuid) {
        System.out.println("User requested {}" + uuid);
        return new UIUser(userServiceImpl.getByUuid(uuid), userTeamService.getAllByUser(uuid));
    }

    @RequestMapping(value = "/modify", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ActionResult addUser(@RequestBody UIUser user) {
        System.out.println("user = " + user.getName());
        System.out.println("uuid = " + user.getUuid());
        System.out.println("team1 = " + user.getTeam());
        if (user.getUuid() == null) {
            userServiceImpl.create(user.getName(), user.getTeamUuids());
        } else {
            userServiceImpl.update(user.getUuid(), user.getName(), user.getTeamUuids());
        }
        return new ActionResult(true, "Seems that ok");
    }


    @RequestMapping("/remove/{uuid}")
    @ResponseBody
    public ActionResult removeUser(@PathVariable String uuid) {
        userServiceImpl.remove(uuid);
        return new ActionResult(true, "Seems that ok");
    }
}
