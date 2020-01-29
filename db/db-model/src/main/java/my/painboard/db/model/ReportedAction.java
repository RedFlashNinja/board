package my.painboard.db.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "REPORTED_ACTION")
public class ReportedAction extends PersistentObject {
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "img_id", nullable = false)
    private Img img;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "reportday_id", nullable = false)
    private ReportDay reportDay;
    @Column(name = "year")
    private String description;
}
