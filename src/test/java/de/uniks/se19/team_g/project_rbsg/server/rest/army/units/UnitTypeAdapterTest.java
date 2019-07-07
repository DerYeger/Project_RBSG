package de.uniks.se19.team_g.project_rbsg.server.rest.army.units;

import de.uniks.se19.team_g.project_rbsg.model.Unit;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class UnitTypeAdapterTest {

    @Test
    public void map() {
        UnitTypeAdapter sut = new UnitTypeAdapter();
        UnitType externalModel = new UnitType();
        externalModel.hp = 1;
        externalModel.mp = 2;
        externalModel.type = "Soldier";
        externalModel.canAttack = Arrays.asList("Apache", "Keanu", "Mark4");
        externalModel.id = "123";

        final Unit internalModel = sut.map(externalModel);

        Assert.assertEquals(externalModel.type, internalModel.name.get());
        Assert.assertEquals(externalModel.hp, internalModel.health.get());
        Assert.assertEquals(externalModel.mp, internalModel.speed.get());
        Assert.assertEquals(externalModel.canAttack, internalModel.canAttack.get());
        Assert.assertEquals(
                "id: 123\n",
                internalModel.description.get()

        );
    }
}