package my.painboard.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UIReportDay {
    private String uuid;
    private int day;
    private String dayOfWeek;
}
