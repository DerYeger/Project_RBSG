package de.uniks.se19.team_g.project_rbsg.army_builder.unit_property_info;

import de.uniks.se19.team_g.project_rbsg.ViewComponent;
import de.uniks.se19.team_g.project_rbsg.army_builder.ArmyBuilderController;
import de.uniks.se19.team_g.project_rbsg.army_builder.ArmyBuilderState;
import de.uniks.se19.team_g.project_rbsg.army_builder.edit_army.EditArmyController;
import de.uniks.se19.team_g.project_rbsg.army_builder.unit_selection.UnitListCellFactory;
import de.uniks.se19.team_g.project_rbsg.configuration.ApplicationState;
import de.uniks.se19.team_g.project_rbsg.configuration.army.DefaultArmyGenerator;
import de.uniks.se19.team_g.project_rbsg.model.Unit;
import de.uniks.se19.team_g.project_rbsg.overlay.alert.AlertBuilder;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.GetArmiesService;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.deletion.DeleteArmyService;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.persistance.PersistentArmyManager;
import io.rincl.Rincl;
import io.rincl.resourcebundle.ResourceBundleResourceI18nConcern;
import javafx.beans.InvalidationListener;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import junit.framework.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.testfx.framework.junit.ApplicationTest;

import java.util.Locale;

public class ArmyBuilderLogicTest extends ApplicationTest {

    @MockBean
    EditArmyController editArmyController;

    @Test
    public void createArmyTest(){

        Rincl.setDefaultResourceI18nConcern(new ResourceBundleResourceI18nConcern());

        ApplicationState applicationState = new ApplicationState();
        applicationState.unitDefinitions.add(new Unit());

        Property<Locale> selectedLocale = new Property<Locale>() {
            @Override
            public void bind(ObservableValue<? extends Locale> observable) {

            }

            @Override
            public void unbind() {

            }

            @Override
            public boolean isBound() {
                return false;
            }

            @Override
            public void bindBidirectional(Property<Locale> other) {

            }

            @Override
            public void unbindBidirectional(Property<Locale> other) {

            }

            @Override
            public Object getBean() {
                return null;
            }

            @Override
            public String getName() {
                return null;
            }

            @Override
            public void addListener(ChangeListener<? super Locale> listener) {

            }

            @Override
            public void removeListener(ChangeListener<? super Locale> listener) {

            }

            @Override
            public Locale getValue() {
                return null;
            }

            @Override
            public void addListener(InvalidationListener listener) {

            }

            @Override
            public void removeListener(InvalidationListener listener) {

            }

            @Override
            public void setValue(Locale value) {

            }
        };

        DefaultArmyGenerator defaultArmyGenerator = new DefaultArmyGenerator(applicationState);

        ArmyBuilderController armyBuilderController = new ArmyBuilderController(
                applicationState,
                Mockito.mock(ArmyBuilderState.class),
                Mockito.mock(UnitListCellFactory.class),
                Mockito.mock(ViewComponent.class),
                null,
                null,
                null,
                null,
                null,
                Mockito.mock(PersistentArmyManager.class),
                Mockito.mock(DeleteArmyService.class),
                defaultArmyGenerator,
                selectedLocale,
                Mockito.mock(AlertBuilder.class),
                Mockito.mock(GetArmiesService.class)
        ){
            @Override
            public void editArmy() {

            }
        };

        Assert.assertTrue(applicationState.armies.isEmpty());
        armyBuilderController.createArmy();
        Assert.assertEquals(1, applicationState.armies.size());
    }
}
