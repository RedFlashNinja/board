package my.painboard.db.service.implementation;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import my.painboard.db.model.ReportDay;
import my.painboard.db.model.ReportDay_;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Repository
@Transactional
public class ReportDayService {
    @PersistenceContext
    private EntityManager em;

    public String create(int day, int year) {
        ReportDay reportDay = new ReportDay();
        reportDay.setDay(day);
        reportDay.setYear(year);
        reportDay.setBorn(new Date());
        em.persist(reportDay);
        return reportDay.getUuid();
    }

    public ReportDay getById(String uuid) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ReportDay> q = cb.createQuery(ReportDay.class);
        Root<ReportDay> c = q.from(ReportDay.class);
        q.where(cb.equal(c.get(ReportDay_.uuid), uuid));
        return em.createQuery(q.select(c)).getSingleResult();
    }

    public ReportDay getDay(int day, int year) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ReportDay> q = cb.createQuery(ReportDay.class);
        Root<ReportDay> c = q.from(ReportDay.class);
        q.where(cb.and(
                cb.equal(c.get(ReportDay_.day), day),
                cb.equal(c.get(ReportDay_.year), year)));
        return em.createQuery(q.select(c)).getSingleResult();
    }

    public List<ReportDay> list() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ReportDay> q = cb.createQuery(ReportDay.class);
        Root<ReportDay> c = q.from(ReportDay.class);
        return em.createQuery(q.select(c)).getResultList();
    }
}
