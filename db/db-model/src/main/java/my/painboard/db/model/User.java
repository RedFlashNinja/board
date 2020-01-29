package my.painboard.db.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USER")
public class User extends PersistentObject {

    @Column(name = "user_name")
    private String userName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_names", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    @Column(name = "roles")
    private Set<Role> roles = new HashSet<>();

    @Column(name = "password")
    private String password;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
}
