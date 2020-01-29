package my.painboard.web.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UIUserState {
    private UIUser user;
    private List<UIStatusDay> statusday = new ArrayList<>();
}
