package my.painboard.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import my.painboard.db.model.Img;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UIImage {
    private String uuid;
    private String description;
    private Integer level;
    private String name;
    private boolean enabled;

    public UIImage(Img img) {
        this.uuid = img.getUuid();
        this.description = img.getDesc();
        this.level = img.getLevel();
        this.name = img.getPath();
        this.enabled = img.getEnabled();
    }
}
