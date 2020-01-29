package my.painboard.web.controller;

import lombok.extern.slf4j.Slf4j;
import my.painboard.db.model.Img;
import my.painboard.db.model.ReportDay;
import my.painboard.db.model.ReportedAction;
import my.painboard.db.model.Settings;
import my.painboard.db.model.Team;
import my.painboard.db.model.User;
import my.painboard.db.service.UserService;
import my.painboard.db.service.implementation.HistoryService;
import my.painboard.db.service.implementation.ImgService;
import my.painboard.db.service.implementation.ReportActionService;
import my.painboard.db.service.implementation.ReportDayService;
import my.painboard.db.service.implementation.SettingsService;
import my.painboard.db.service.implementation.TeamService;
import my.painboard.db.service.implementation.UserTeamService;
import my.painboard.web.dto.UIImage;
import my.painboard.web.dto.UIReportDay;
import my.painboard.web.dto.UIStatus;
import my.painboard.web.dto.UIStatusDay;
import my.painboard.web.dto.UITable;
import my.painboard.web.dto.UITeamTable;
import my.painboard.web.dto.UIUser;
import my.painboard.web.dto.UIUserState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/calendar")
public class CalendarController {
    @Autowired
    private ImgService imgService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private UserTeamService userTeamService;
    @Autowired
    private UserService userService;
    @Autowired
    private ReportDayService reportDayService;
    @Autowired
    private ReportActionService reportActionService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private SettingsService settingsService;

    private boolean done = false;

    @RequestMapping("/preload")
    public void doprefill() {
        log.debug("prefill requested");
        List<Settings> list = settingsService.list();
        if (list == null || list.isEmpty()) {
            log.debug("prefill started");
            settingsService.create();
            String teamid = teamService.create("daas reboot");
            userService.create("N", Arrays.asList(teamid));
            userService.create("S", Arrays.asList(teamid));
            userService.create("H", Arrays.asList(teamid));
            imgService.create("0.png", 10, "very happy");
            imgService.create("01.png", 9, "happy");
            imgService.create("02.png", 8, "almost happy");
            imgService.create("03.png", 7, "moderate happy");
            imgService.create("04.png", 6, "slightly happy");
            imgService.create("1.png", 5, "neutral");
            imgService.create("21.png", 4, "slighty upset");
            imgService.create("22.png", 3, "upset");
            imgService.create("22.png", 2, "not happy");
            imgService.create("2.png", 1, "not happy at all");
            LocalDate localDate = LocalDate.now().minusDays(15);
            for (int i = 0; i <= 17; ++i) {
                reportDayService.create(localDate.plusDays(i).getDayOfYear(), LocalDate.now().getYear());
            }
            done = true;
        }
        log.debug("prefill finished images {}", imgService.list().size());
    }

    public void fillDays() {
        List<ReportDay> days = reportDayService.list();
        Collections.sort(days, (o1, o2) -> o1.getYear() != o2.getYear() ? o1.getYear() - o2.getYear() : o1.getDay() - o2.getDay());
        if (days.get(days.size() - 1).getDay() < LocalDate.now().plusDays(30).getDayOfYear()) {
            for (int i = days.get(days.size() - 1).getDay() + 1; i < LocalDate.now().plusDays(30).getDayOfYear(); ++i) {
                reportDayService.create(i, LocalDate.now().getYear());
            }
        }
    }

    @RequestMapping("/all")
    public
    @ResponseBody
    UITable list() {
        fillDays();
        UITable table = new UITable();
        table.setDays(getDays(12, 2));
        List<UIImage> imgs = new ArrayList<>();
        List<Img> imgss = imgService.list();
        Collections.sort(imgss, (o1, o2) -> o2.getLevel() - o1.getLevel());
        for (Img img : imgss) {
            imgs.add(new UIImage(img));
        }
        table.setImgs(imgs);
        table.getUiUsers().addAll(getUserTable(userService.list(), 12, 2));
        return table;
    }

    @RequestMapping("/allByTeams")
    public
    @ResponseBody
    UITeamTable listByTeams() {
        UITeamTable table = new UITeamTable();
        fillDays();
        table.setDays(getDays(12, 2));

        List<UIImage> imgs = new ArrayList<>();
        List<Img> imgss = imgService.list();
        Collections.sort(imgss, (o1, o2) -> o2.getLevel() - o1.getLevel());
        for (Img img : imgss) {
            imgs.add(new UIImage(img));
        }
        table.setImgs(imgs);

        for (Team team : teamService.list()) {
            List<User> users = userTeamService.getAllByTeam(team.getUuid());
            table.getUiUsers().put(team.getTeamName(), getUserTable(users, 12, 2));
        }
        return table;
    }

    @RequestMapping("/getByTeam/{teamUuuid}")
    public
    @ResponseBody
    UITeamTable getByTeams(@PathVariable String teamUuuid) {
        UITeamTable table = new UITeamTable();
        fillDays();
        table.setDays(getDays(12, 2));

        List<UIImage> imgs = new ArrayList<>();
        List<Img> imgss = imgService.list();
        Collections.sort(imgss, (o1, o2) -> o2.getLevel() - o1.getLevel());
        for (Img img : imgss) {
            imgs.add(new UIImage(img));
        }
        table.setImgs(imgs);

        Team team = teamService.getByUuid(teamUuuid);
        List<User> users = userTeamService.getAllByTeam(team.getUuid());
        table.getUiUsers().put(team.getTeamName(), getUserTable(users, 12, 2));
        return table;
    }

    private List<UIReportDay> getDays(int minusDelta, int plusDelta) {
        LocalDate localDate = LocalDate.now().minusDays(minusDelta);
        List<UIReportDay> days = new ArrayList<>();
        for (int i = 0; i <= minusDelta + plusDelta; ++i) {
            ReportDay rd = reportDayService.getDay(localDate.plusDays(i).getDayOfYear(), localDate.getYear());
            LocalDate d1 = LocalDate.ofYearDay(rd.getYear(), rd.getDay());
            days.add(new UIReportDay(rd.getUuid(), d1.getDayOfMonth(), ""));
        }
        return days;
    }

    private List<UIUserState> getUserTable(List<User> users, int minusDelta, int plusDelta) {
        boolean isPreviousDaysEditable = settingsService.getSettings().isEditPreviousDays();
        LocalDate localDate = LocalDate.now().minusDays(minusDelta);
        List<UIUserState> usesrStates = new ArrayList<>();
        for (User user : users) {
            UIUserState uiUserState = new UIUserState();
            uiUserState.setUser(new UIUser(user, userTeamService.getAllByUser(user.getUuid())));
            for (int i = 0; i <= minusDelta + plusDelta; ++i) {
                ReportDay rd = reportDayService.getDay(localDate.plusDays(i).getDayOfYear(), localDate.getYear());
                UIStatusDay statusDay = new UIStatusDay();
                if (localDate.plusDays(i).getDayOfWeek() == DayOfWeek.SATURDAY || localDate.plusDays(i).getDayOfWeek() == DayOfWeek.SUNDAY) {
                    statusDay.setStatus(UIStatus.HOLIDAY);
                } else if (localDate.plusDays(i).getDayOfYear() > LocalDate.now().getDayOfYear()) {
                    statusDay.setStatus(UIStatus.EMPTY);
                } else if (localDate.plusDays(i).getDayOfYear() == LocalDate.now().getDayOfYear()) {
                    ReportedAction action = reportActionService.getByDayAndUser(user.getUuid(), rd.getUuid());
                    if (action != null) {
                        statusDay.setStatus(UIStatus.IMAGE_AND_FILL);
                        statusDay.setImage(new UIImage(action.getImg()));
                    } else {
                        statusDay.setStatus(UIStatus.TOFILL);
                    }
                } else {
                    ReportedAction action = reportActionService.getByDayAndUser(user.getUuid(), rd.getUuid());
                    if (isPreviousDaysEditable) {
                        if (action != null) {
                            statusDay.setStatus(UIStatus.IMAGE_AND_FILL);
                            statusDay.setImage(new UIImage(action.getImg()));
                        } else {
                            statusDay.setStatus(UIStatus.TOFILL);
                        }
                    } else {
                        if (action != null) {
                            statusDay.setStatus(UIStatus.IMAGE);
                            statusDay.setImage(new UIImage(action.getImg()));
                        } else {
                            statusDay.setStatus(UIStatus.EMPTY);
                        }
                    }
                }
                LocalDate d1 = LocalDate.ofYearDay(rd.getYear(), rd.getDay());
                statusDay.setDay(new UIReportDay(rd.getUuid(), d1.getDayOfMonth(), ""));
                uiUserState.getStatusday().add(statusDay);
            }
            usesrStates.add(uiUserState);
        }
        return usesrStates;
    }

    @RequestMapping("register/{userId}/{dayId}/{imgId}")
    public void register(@PathVariable String userId, @PathVariable String dayId, @PathVariable String imgId) {
        log.debug("register request: userId = " + userId + "dayId = " + dayId + "imgId = " + imgId);
        historyService.create("userId = " + userId + "dayId = " + dayId + "imgId = " + imgId);
        reportActionService.save(userId, dayId, imgId);
    }
}
