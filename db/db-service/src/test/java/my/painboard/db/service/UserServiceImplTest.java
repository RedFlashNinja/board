package my.painboard.db.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import my.painboard.db.model.User;
import my.painboard.db.model.User_;
import my.painboard.db.service.implementation.TeamService;
import my.painboard.db.service.implementation.UserTeamService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class UserServiceImplTest extends BaseTest {

    @Autowired
    private UserService userService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private UserTeamService userTeamService;
    @PersistenceContext
    private EntityManager em;
    String userName = "testUser";

    @Test
    public void testGetByUuid() throws Exception {
        String teamuuid = teamService.create("dfs");
        String name = UUID.randomUUID().toString();
        String uuid = userService.create(name, Arrays.asList(teamuuid));
        Assert.assertNotNull(uuid);
        Assert.assertEquals(name, userService.getByUuid(uuid).getUserName());
    }
    @Test
    public void testUpdate() throws Exception {
        String teamuuid = teamService.create("dfs");
        String name = UUID.randomUUID().toString();
        String uuid = userService.create(name, Arrays.asList(teamuuid));
        Assert.assertNotNull(uuid);
        Assert.assertEquals(name, userService.getByUuid(uuid).getUserName());

        String newTeamuuid = teamService.create("aaa");
        String newName = UUID.randomUUID().toString();
        userService.update(uuid, newName, Collections.singletonList(newTeamuuid));
        Assert.assertEquals(newName, userService.getByUuid(uuid).getUserName());
        Assert.assertEquals(newTeamuuid, userTeamService.getAllByUser(uuid).get(0).getUuid());
    }
    @Test
    public void testListAll() {
        String teamuuid = teamService.create("dfs");
        int size = userService.list().size();
        String uuid = userService.create("a", Arrays.asList(teamuuid));
        Assert.assertNotNull(uuid);
        userService.create("c", Arrays.asList(teamuuid));
        Assert.assertEquals(size + 2, userService.list().size());
    }

    @Test
    public void testDelete() {
        String teamuuid = teamService.create("dfs");
        int size = userService.list().size();
        int removedsize = userService.removed().size();
        String uuid = userService.create("ad", Arrays.asList(teamuuid));
        userService.create("cd", Arrays.asList(teamuuid));
        Assert.assertEquals(size + 2, userService.list().size());
        userService.remove(uuid);
        Assert.assertEquals(size + 1, userService.list().size());
        Assert.assertEquals(removedsize + 1, userService.removed().size());
    }

    @Test(expected = NoResultException.class)
    public void testGetByUserNameOptionalException() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> q = cb.createQuery(User.class);
        Root<User> c = q.from(User.class);
        q.where(cb.equal(c.get(User_.userName), userName));
        Assert.assertNotNull(Optional.ofNullable(em.createQuery(q.select(c)).getSingleResult()));
    }

    @Test()
    public void testGetByUserNameOptional() {
        String teamuuid = teamService.create("dfs");
        userService.create(userName, Arrays.asList(teamuuid));

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> q = cb.createQuery(User.class);
        Root<User> c = q.from(User.class);
        q.where(cb.equal(c.get(User_.userName), userName));
        User user = em.createQuery(q.select(c)).getSingleResult();
        Assert.assertNotNull(Optional.ofNullable(em.createQuery(q.select(c)).getSingleResult()));
        Assert.assertEquals(userName, user.getUserName());
    }
}
