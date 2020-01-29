package my.painboard.db.service;

import my.painboard.db.service.implementation.HistoryService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class HistoryServiceTest extends BaseTest {
    @Autowired
    private HistoryService historyService;

    @Test
    public void testGetAll() {
        int size = historyService.list().size();
        Assert.assertNotNull(historyService.create("blaaa"));
        Assert.assertEquals(size + 1, historyService.list().size());
    }
}
