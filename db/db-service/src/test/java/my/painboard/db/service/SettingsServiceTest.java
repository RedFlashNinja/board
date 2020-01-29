package my.painboard.db.service;

import my.painboard.db.service.implementation.SettingsService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SettingsServiceTest extends BaseTest {
    @Autowired
    private SettingsService settingsService;

    @Test
    public void testSettings(){
        settingsService.create();
        Assert.assertEquals(false, settingsService.getSettings().isEditPreviousDays());
        settingsService.updateEditPreviousDays(true);
        Assert.assertEquals(true, settingsService.getSettings().isEditPreviousDays());
    }
}
