package my.painboard.db.service;

import my.painboard.db.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService extends UserDetailsService {
    String create(String name, List<String> teamUuids);

    void update(String uuid, String name, List<String> teamUuids);

    void remove(String uuid);

    User getByUuid(String uuid);

    List<User> list();

    List<User> removed();

    boolean isUserRegistered(String serName);

    void save(User user);
}
