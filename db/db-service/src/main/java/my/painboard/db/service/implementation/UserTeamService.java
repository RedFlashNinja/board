package my.painboard.db.service.implementation;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import my.painboard.db.model.Team;
import my.painboard.db.model.Team_;
import my.painboard.db.model.User;
import my.painboard.db.model.User_;
import my.painboard.db.model.UserTeam;
import my.painboard.db.model.UserTeam_;
import org.springframework.stereotype.Repository;

@Repository
public class UserTeamService {
    @PersistenceContext
    private EntityManager em;

    public String create(Team team, User user) {
        UserTeam ut = new UserTeam();
        ut.setTeam(team);
        ut.setUser(user);
        ut.setBorn(new Date());
        em.persist(ut);
        return ut.getUuid();
    }

    public List<User> getAllByTeam(String teamUuid) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<UserTeam> q = cb.createQuery(UserTeam.class);
        Root<UserTeam> c = q.from(UserTeam.class);
        q.where(cb.and(
                cb.isNull(c.get(UserTeam_.dead)),
                cb.equal(c.get(UserTeam_.team).get(Team_.uuid), teamUuid)));
        return em.createQuery(q.select(c)).getResultList().stream().map(UserTeam::getUser).collect(Collectors.toList());
    }


    public List<Team> getAllByUser(String userUuid) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<UserTeam> q = cb.createQuery(UserTeam.class);
        Root<UserTeam> c = q.from(UserTeam.class);
        q.where(cb.and(
                cb.isNull(c.get(UserTeam_.dead)),
                cb.equal(c.get(UserTeam_.user).get(User_.uuid), userUuid)));
        return em.createQuery(q.select(c)).getResultList().stream().map(UserTeam::getTeam).collect(Collectors.toList());
    }

    public List<User> getAllByTeams(List<Team> teams) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<UserTeam> q = cb.createQuery(UserTeam.class);
        Root<UserTeam> c = q.from(UserTeam.class);
        q.where(cb.and(
                cb.isNull(c.get(UserTeam_.dead)),
                c.get(UserTeam_.team).in(teams)));
        return em.createQuery(q.select(c)).getResultList().stream().map(UserTeam::getUser).collect(Collectors.toList());
    }

    public void addToUser(User user, List<Team> teams) {
        for (Team team : teams) {
            create(team, user);
        }
    }

    public void removeUser(String userId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaUpdate<UserTeam> q = cb.createCriteriaUpdate(UserTeam.class);
        Root<UserTeam> c = q.from(UserTeam.class);
        q.set(c.get(UserTeam_.dead), new Date())
                .where(cb.and(cb.isNull(c.get(UserTeam_.dead)),
                        cb.equal(c.get(UserTeam_.user).get(User_.uuid), userId)));
//                c.get(UserTeam_.team).in(teamService.getByUuids(teamUuids))));
        em.createQuery(q).executeUpdate();
    }
}
