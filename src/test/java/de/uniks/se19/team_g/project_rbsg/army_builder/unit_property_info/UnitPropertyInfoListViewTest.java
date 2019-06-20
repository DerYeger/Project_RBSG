package de.uniks.se19.team_g.project_rbsg.army_builder.unit_property_info;

import io.rincl.Rincl;
import io.rincl.resourcebundle.ResourceBundleResourceI18nConcern;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testfx.framework.junit.ApplicationTest;

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
public class UnitPropertyInfoListViewTest extends ApplicationTest {

    @Autowired
    private UnitPropertyInfoListBuilder unitPropertyInfoListBuilder;

    private Node infoView;

    @Override
    public void start(@NonNull final Stage stage) {
        Rincl.setDefaultResourceI18nConcern(new ResourceBundleResourceI18nConcern());
        infoView = unitPropertyInfoListBuilder.buildInfoView();
        Assert.assertNotNull(infoView);
        final Scene scene = new Scene((Parent) infoView, 600, 320);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void testUnitPropertyInfoListView() {
        Button closeButton = lookup("#closeButton").query();
        Assert.assertNotNull(closeButton);
        clickOn("#closeButton");
        Assert.assertFalse(infoView.isVisible());
    }

}
