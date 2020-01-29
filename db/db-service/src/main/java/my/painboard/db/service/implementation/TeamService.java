package my.painboard.db.service.implementation;

import lombok.extern.slf4j.Slf4j;
import my.painboard.db.model.Team;
import my.painboard.db.model.Team_;
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
public class TeamService {
    @PersistenceContext
    private EntityManager em;

    public String create(String name) {
        Team team = new Team();
        team.setTeamName(name);
        team.setBorn(new Date());
        em.persist(team);
        return team.getUuid();
    }

    public void update(String uuid, String name) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaUpdate<Team> q = cb.createCriteriaUpdate(Team.class);
        Root<Team> c = q.from(Team.class);
        q.set(c.get(Team_.modified), new Date())
                .set(c.get(Team_.teamName), name)
                .where(cb.equal(c.get(Team_.uuid), uuid));
        em.createQuery(q).executeUpdate();
    }

    public void remove(String uuid) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaUpdate<Team> q = cb.createCriteriaUpdate(Team.class);
        Root<Team> c = q.from(Team.class);
        q.set(c.get(Team_.dead), new Date()).where(cb.equal(c.get(Team_.uuid), uuid));
        em.createQuery(q).executeUpdate();
    }

    public Team getByUuid(String uuid) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Team> q = cb.createQuery(Team.class);
        Root<Team> c = q.from(Team.class);
        q.where(cb.equal(c.get(Team_.uuid), uuid));
        return em.createQuery(q.select(c)).getSingleResult();
    }

    public List<Team> getByUuids(List<String> uuids) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Team> q = cb.createQuery(Team.class);
        Root<Team> c = q.from(Team.class);
        q.where(c.get(Team_.uuid).in(uuids));
        return em.createQuery(q.select(c)).getResultList();
    }

    public List<Team> list() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Team> q = cb.createQuery(Team.class);
        Root<Team> c = q.from(Team.class);
        q.where(cb.isNull(c.get(Team_.dead)));
        return em.createQuery(q.select(c)).getResultList();
    }

    public List<Team> removed() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Team> q = cb.createQuery(Team.class);
        Root<Team> c = q.from(Team.class);
        q.where(cb.isNotNull(c.get(Team_.dead)));
        return em.createQuery(q.select(c)).getResultList();
    }
}
