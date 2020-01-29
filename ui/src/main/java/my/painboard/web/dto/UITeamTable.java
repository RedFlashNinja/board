package my.painboard.web.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UITeamTable {
    private List<UIReportDay> days = new ArrayList<>();
    private List<UIImage> imgs = new ArrayList<>();
    private Map<String, List<UIUserState>> uiUsers = new HashMap<>();
}
