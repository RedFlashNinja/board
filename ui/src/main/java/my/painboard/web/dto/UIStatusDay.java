package my.painboard.web.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UIStatusDay {
    private UIStatus status;
    private UIReportDay day;
    private UIImage image;
}
