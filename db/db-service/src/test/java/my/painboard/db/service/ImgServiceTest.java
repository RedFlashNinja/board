package my.painboard.db.service;

import java.util.UUID;

import my.painboard.db.service.implementation.ImgService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ImgServiceTest extends BaseTest {

    @Autowired
    private ImgService imgService;

    @Test
    public void getByUuid() throws Exception {
        String randString = UUID.randomUUID().toString();
        String uuid = imgService.create(randString, 2, null);
        Assert.assertNotNull(uuid);
        Assert.assertEquals(randString, imgService.getByUuid(uuid).getPath());
    }

    @Test
    public void testListAll() {
        int size = imgService.list().size();
        String uuid = imgService.create("a", 2, null);
        Assert.assertNotNull(uuid);
        imgService.create("c", 2, null);
        Assert.assertEquals(size + 2, imgService.list().size());
    }

    @Test
    public void testDelete() {
        int size = imgService.list().size();
        int removedsize = imgService.removed().size();
        String uuid = imgService.create("ad", 2, null);
        imgService.create("cd", 2, null);
        Assert.assertEquals(size + 2, imgService.list().size());
        imgService.remove(uuid);
        Assert.assertEquals(size + 1, imgService.list().size());
        Assert.assertEquals(removedsize + 1, imgService.removed().size());
    }
}
