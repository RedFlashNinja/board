package my.painboard.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import my.painboard.db.model.Settings;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UISetting {
    private boolean editPreviousDays;

    public UISetting(Settings settings) {
        this.editPreviousDays = settings.isEditPreviousDays();
    }
}
