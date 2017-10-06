package my.painboard.db.service;

import java.util.Date;
import java.util.List;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import my.painboard.db.model.Img;
import my.painboard.db.model.Img;
import my.painboard.db.model.Img_;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Repository
@Transactional
public class ImgService {
    @PersistenceContext
    private Session em;

    public String create(String path, int level, String desc){
        Img img = new Img();
        img.setBorn(new Date());
        img.setPath(path);
        img.setLevel(level);
        img.setDesc(desc);
        em.saveOrUpdate(img);
        return img.getUuid();
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
}
