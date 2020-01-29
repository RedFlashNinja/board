package my.painboard.web.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UITable {
    private List<UIReportDay> days = new ArrayList<>();
//    private List<Integer> dates = new ArrayList<>();
    private List<UIUserState> uiUsers = new ArrayList<>();
    private List<UIImage> imgs = new ArrayList<>();
}
