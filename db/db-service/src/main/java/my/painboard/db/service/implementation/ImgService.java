package my.painboard.db.service.implementation;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import my.painboard.db.model.Img;
import my.painboard.db.model.Img_;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Repository
@Transactional
public class ImgService {
    @PersistenceContext
    private EntityManager em;

    public String create(String path, int level, String desc) {
        Img img = new Img();
        img.setBorn(new Date());
        img.setPath(path);
        img.setLevel(level);
        img.setDesc(desc);
        img.setEnabled(Boolean.TRUE);
        em.persist(img);
        return img.getUuid();
    }

    public void enable(String uuid, boolean isEnabled){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaUpdate<Img> q = cb.createCriteriaUpdate(Img.class);
        Root<Img> c = q.from(Img.class);
        q.set(c.get(Img_.modified), new Date())
                .set(c.get(Img_.enabled), isEnabled)
                .where(cb.equal(c.get(Img_.uuid), uuid));
        em.createQuery(q).executeUpdate();
    }

    public void update(String uuid, String path, int level, String desc) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaUpdate<Img> q = cb.createCriteriaUpdate(Img.class);
        Root<Img> c = q.from(Img.class);
        q.set(c.get(Img_.modified), new Date())
                .set(c.get(Img_.desc), desc)
                .set(c.get(Img_.level), level)
                .set(c.get(Img_.path), path)
                .where(cb.equal(c.get(Img_.uuid), uuid));
        em.createQuery(q).executeUpdate();
    }

    public void remove(String uuid) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaUpdate<Img> q = cb.createCriteriaUpdate(Img.class);
        Root<Img> c = q.from(Img.class);
        q.set(c.get(Img_.dead), new Date()).where(cb.equal(c.get(Img_.uuid), uuid));
        em.createQuery(q).executeUpdate();
    }

    public Img getByUuid(String uuid) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Img> q = cb.createQuery(Img.class);
        Root<Img> c = q.from(Img.class);
        q.where(cb.equal(c.get(Img_.uuid), uuid));
        return em.createQuery(q.select(c)).getSingleResult();
    }

    public List<Img> listEnabled() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Img> q = cb.createQuery(Img.class);
        Root<Img> c = q.from(Img.class);
        q.where(cb.and(cb.isNull(c.get(Img_.dead)),
                cb.equal(c.get(Img_.enabled), true)));
        return em.createQuery(q.select(c)).getResultList();
    }

    public List<Img> list() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Img> q = cb.createQuery(Img.class);
        Root<Img> c = q.from(Img.class);
        q.where(cb.isNull(c.get(Img_.dead)));
        return em.createQuery(q.select(c)).getResultList();
    }

    public List<Img> removed() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Img> q = cb.createQuery(Img.class);
        Root<Img> c = q.from(Img.class);
        q.where(cb.isNotNull(c.get(Img_.dead)));
        return em.createQuery(q.select(c)).getResultList();
    }

    public void updateDescription(String uuid, String newdesc) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaUpdate<Img> q = cb.createCriteriaUpdate(Img.class);
        Root<Img> c = q.from(Img.class);
        q.set(c.get(Img_.modified), new Date())
                .set(c.get(Img_.desc), newdesc)
                .where(cb.equal(c.get(Img_.uuid), uuid));
        em.createQuery(q).executeUpdate();
    }
}
