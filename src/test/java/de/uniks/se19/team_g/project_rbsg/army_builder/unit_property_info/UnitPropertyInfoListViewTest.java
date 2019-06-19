package de.uniks.se19.team_g.project_rbsg.army_builder.unit_property_info;

import io.rincl.Rincl;
import io.rincl.resourcebundle.ResourceBundleResourceI18nConcern;
import javafx.application.Platform;
import javafx.scene.Node;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author  Keanu Stückrad
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        UnitPropertyInfoListBuilder.class,
        UnitPropertyInfoListBuilder.class,
        UnitPropertyInfoListController.class,
        UnitPropertyInfoCellController.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UnitPropertyInfoListViewTest {

    @Autowired
    private ApplicationContext context;

    @Test
    public void testUnitPropertyInfoListView() throws Exception {
        Rincl.setDefaultResourceI18nConcern(new ResourceBundleResourceI18nConcern());
        Platform.startup(() -> {
            Node infoView = infoView = context.getBean(UnitPropertyInfoListBuilder.class).buildInfoView();
            Assert.assertNotNull(infoView);
        });
    }

}
