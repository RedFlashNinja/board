package my.painboard.db.service.implementation;

import lombok.extern.slf4j.Slf4j;
import my.painboard.db.model.ReportDay_;
import my.painboard.db.model.ReportedAction;
import my.painboard.db.model.ReportedAction_;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ReportActionService {
    @PersistenceContext
    private EntityManager em;
    @Autowired
    private ReportDayService reportDayService;
    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private ImgService imgService;

    public String save(String userid, String dayId, String imageId) {
        ReportedAction oldAction = getByDayAndUser(userid, dayId);
        if (oldAction != null) {
            remove(oldAction.getUuid());
        }
        ReportedAction action = new ReportedAction();
        action.setUser(userServiceImpl.getByUuid(userid));
        action.setImg(imgService.getByUuid(imageId));
        action.setReportDay(reportDayService.getById(dayId));
        action.setBorn(new Date());
        em.persist(action);
        return action.getUuid();
    }

    public void remove(String uuid) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaUpdate<ReportedAction> q = cb.createCriteriaUpdate(ReportedAction.class);
        Root<ReportedAction> c = q.from(ReportedAction.class);
        q.set(c.get(ReportedAction_.dead), new Date()).where(cb.equal(c.get(ReportedAction_.uuid), uuid));
        em.createQuery(q).executeUpdate();
    }

    public ReportedAction getByDayAndUser(String userid, String dayId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ReportedAction> q = cb.createQuery(ReportedAction.class);
        Root<ReportedAction> c = q.from(ReportedAction.class);
        q.where(cb.and(
                cb.equal(c.get(ReportedAction_.reportDay).get(ReportDay_.uuid), dayId),
//                cb.equal(c.get(ReportedAction_.user).get(User_.uuid), userid),
                cb.isNull(c.get(ReportedAction_.dead))));
        List<ReportedAction> list = em.createQuery(q.select(c)).getResultList();
        return list == null || list.isEmpty() ? null : list.get(0);
    }
}
