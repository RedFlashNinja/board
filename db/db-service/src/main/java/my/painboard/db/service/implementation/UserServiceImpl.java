package my.painboard.db.service.implementation;

import lombok.extern.slf4j.Slf4j;
import my.painboard.db.model.Team;
import my.painboard.db.model.User;
import my.painboard.db.model.User_;
import my.painboard.db.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

@Slf4j
@Repository
@Transactional
public class UserServiceImpl implements UserService {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserTeamService userTeamService;
    @Autowired
    private TeamService teamService;

    @Override
    public String create(String userName, List<String> teamUuids) {
        User user = new User();
        user.setUserName(userName);
        user.setBorn(new Date());
        em.persist(user);
        for (Team t : teamService.getByUuids(teamUuids)) {
            userTeamService.create(t, user);
        }
        return user.getUuid();
    }

    @Override
    public void update(String uuid, String name, List<String> teamUuids) {
        userTeamService.removeUser(uuid);
        userTeamService.addToUser(getByUuid(uuid), teamService.getByUuids(teamUuids));
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaUpdate<User> q = cb.createCriteriaUpdate(User.class);
        Root<User> c = q.from(User.class);
        q.set(c.get(User_.modified), new Date())
                .set(c.get(User_.userName), name)
                .where(cb.equal(c.get(User_.uuid), uuid));
        em.createQuery(q).executeUpdate();
    }

    @Override
    public void remove(String uuid) {
        userTeamService.removeUser(uuid);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaUpdate<User> q = cb.createCriteriaUpdate(User.class);
        Root<User> c = q.from(User.class);
        q.set(c.get(User_.dead), new Date()).where(cb.equal(c.get(User_.uuid), uuid));
        em.createQuery(q).executeUpdate();
    }

    @Override
    public User getByUuid(String uuid) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> q = cb.createQuery(User.class);
        Root<User> c = q.from(User.class);
        q.where(cb.equal(c.get(User_.uuid), uuid));
        return em.createQuery(q.select(c)).getSingleResult();
    }

    @Override
    public List<User> list() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> q = cb.createQuery(User.class);
        Root<User> c = q.from(User.class);
        q.where(cb.isNull(c.get(User_.dead)));
        return em.createQuery(q.select(c)).getResultList();
    }

    @Override
    public List<User> removed() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> q = cb.createQuery(User.class);
        Root<User> c = q.from(User.class);
        q.where(cb.isNotNull(c.get(User_.dead)));
        return em.createQuery(q.select(c)).getResultList();
    }

    @Override
    public boolean isUserRegistered(String userName) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> q = cb.createQuery(User.class);
        Root<User> c = q.from(User.class);
        q.where(cb.equal(c.get(User_.userName), userName));

        try {
            if (em.createQuery(q.select(c)).getSingleResult().getUserName().isEmpty()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> q = cb.createQuery(User.class);
        Root<User> c = q.from(User.class);
        q.where(cb.equal(c.get(User_.userName), userName));
        try {
            User user = em.createQuery(q.select(c)).getSingleResult();
            return UserDetailsImpl.build(user);
        } catch (UsernameNotFoundException ex) {
            throw new UsernameNotFoundException("User Not Found with username: " + userName);
        }
    }

    @Override
    public void save(User user) {
        user.setBorn(new Date());
        em.persist(user);
    }
}
