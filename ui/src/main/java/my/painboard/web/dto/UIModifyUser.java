package my.painboard.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UIModifyUser {
    private String name;
    private String uuid;
    private String currentTeam;
    private String currentTeamUuid;
    private List<UITeam> allTeams = new ArrayList<>();
}
