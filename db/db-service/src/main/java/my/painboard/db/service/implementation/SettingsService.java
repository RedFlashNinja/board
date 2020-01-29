package my.painboard.db.service.implementation;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import my.painboard.db.model.Settings;
import my.painboard.db.model.Settings_;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class SettingsService {
    @PersistenceContext
    private EntityManager em;

    public String create() {
        List<Settings> list = list();
        if (list == null || list.isEmpty()) {
            Settings settings = new Settings();
            settings.setBorn(new Date());
            settings.setEditPreviousDays(false);
            em.persist(settings);
            return settings.getUuid();
        }
        return list.get(0).getUuid();
    }

    public void updateEditPreviousDays(boolean editPreviousDays) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaUpdate<Settings> q = cb.createCriteriaUpdate(Settings.class);
        Root<Settings> c = q.from(Settings.class);
        q.set(c.get(Settings_.editPreviousDays), editPreviousDays);
        em.createQuery(q).executeUpdate();
    }

    public Settings getSettings() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Settings> q = cb.createQuery(Settings.class);
        Root<Settings> c = q.from(Settings.class);
        return em.createQuery(q.select(c)).getSingleResult();
    }

    public List<Settings> list() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Settings> q = cb.createQuery(Settings.class);
        Root<Settings> c = q.from(Settings.class);
        return em.createQuery(q.select(c)).getResultList();
    }
}
