package my.painboard.web.controller;

import lombok.extern.slf4j.Slf4j;
import my.painboard.db.service.implementation.SettingsService;
import my.painboard.web.dto.ActionResult;
import my.painboard.web.dto.UISetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/settings")
public class SettingsController {
    @Autowired
    private SettingsService settingsService;

    @RequestMapping("/get")
    public
    @ResponseBody
    UISetting get() {
        return new UISetting(settingsService.getSettings());
    }

    @RequestMapping(value = "/switchEditable")
    public @ResponseBody
    ActionResult switchEditable() {
        settingsService.updateEditPreviousDays(!settingsService.getSettings().isEditPreviousDays());
        return new ActionResult(true, "seems that ok");
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    ActionResult save(@RequestBody UISetting setting) {
        settingsService.updateEditPreviousDays(setting.isEditPreviousDays());
        return new ActionResult(true, "seems that ok");
    }
}
