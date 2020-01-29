package my.painboard.web.controller;

import lombok.extern.slf4j.Slf4j;
import my.painboard.db.model.Team;
import my.painboard.db.service.implementation.TeamService;
import my.painboard.web.dto.ActionResult;
import my.painboard.web.dto.UITeam;
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
@RequestMapping("/team")
public class TeamContoller {
    @Autowired
    private TeamService teamService;

    @RequestMapping("/list")
    public
    @ResponseBody
    List<UITeam> list() {
        log.debug("teamService list requested ");
        List<UITeam> res = new ArrayList<>();
        for (Team item : teamService.list()) {
            res.add(new UITeam(item.getUuid(), item.getTeamName()));
        }
        log.debug("found " + teamService.list().size() + " teams");
        return res;
    }

    @RequestMapping("/get/{uuid}")
    public
    @ResponseBody
    UITeam get(@PathVariable String uuid) {
        log.debug("Team requested {}", uuid);
        return new UITeam(teamService.getByUuid(uuid));
    }

    @RequestMapping(value = "/modify", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    ActionResult addTeam(@RequestBody UITeam team) {
        if (team.getUuid() == null) {
            teamService.create(team.getName());
        } else {
            teamService.update(team.getUuid(), team.getName());
        }
        log.debug("seems ok");
        return new ActionResult(true, "Seems that ok");
    }


    @RequestMapping("/remove/{uuid}")
    public void removeTeam(@PathVariable String uuid) {
        log.debug("removing team ", uuid);
        teamService.remove(uuid);
    }
}
