package my.painboard.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import my.painboard.db.model.Team;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UITeam {
    private String uuid;
    private String name;

    public UITeam(Team team) {
        this.setUuid(team.getUuid());
        this.setName(team.getTeamName());
    }
}
