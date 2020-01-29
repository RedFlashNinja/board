package my.painboard.db.service.implementation;

import lombok.extern.slf4j.Slf4j;
import my.painboard.db.model.ERole;
import my.painboard.db.model.PersistentObject;
import my.painboard.db.model.Role;
import my.painboard.db.model.Role_;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Optional;

@Slf4j
@Repository
@Transactional
public class RolesService extends PersistentObject {

    @PersistenceContext
    private EntityManager em;

    public Optional<Role> getRoleName(ERole roleName) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Role> q = cb.createQuery(Role.class);
        Root<Role> c = q.from(Role.class);
        q.where(cb.equal(c.get(Role_.roleName), roleName));
        return Optional.of(em.createQuery(q.select(c)).getSingleResult());
    }
}
