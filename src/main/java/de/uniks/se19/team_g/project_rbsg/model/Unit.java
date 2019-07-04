package de.uniks.se19.team_g.project_rbsg.model;

import de.uniks.se19.team_g.project_rbsg.configuration.flavor.UnitTypeInfo;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;

import javax.annotation.Nonnull;

public class Unit implements Cloneable {

    public static final String UNKNOWN = "UNKNOWN";

    public final SimpleStringProperty type = new SimpleStringProperty();
    public final SimpleStringProperty id = new SimpleStringProperty();

    public final SimpleStringProperty name = new SimpleStringProperty();
    public final SimpleStringProperty imageUrl = new SimpleStringProperty();
    public final SimpleStringProperty iconUrl = new SimpleStringProperty();
    public final SimpleStringProperty description = new SimpleStringProperty();

    public final SimpleIntegerProperty health = new SimpleIntegerProperty();
    public final SimpleIntegerProperty physicalResistance = new SimpleIntegerProperty();
    public final SimpleIntegerProperty magicResistance = new SimpleIntegerProperty();
    public final SimpleIntegerProperty speed = new SimpleIntegerProperty();
    public final SimpleIntegerProperty attack = new SimpleIntegerProperty();
    public final SimpleIntegerProperty spellPower = new SimpleIntegerProperty();

    private UnitTypeInfo typeInfo = UnitTypeInfo.UNKNOWN;

    public final SimpleListProperty<String> canAttack = new SimpleListProperty<>(FXCollections.observableArrayList());

    public static Unit unknownType(String id) {
        final Unit unit = new Unit();
        unit.health.set(-1);
        unit.physicalResistance.set(0);
        unit.magicResistance.set(0);
        unit.speed.set(0);
        unit.attack.set(0);
        unit.spellPower.set(0);
        unit.name.set(UNKNOWN);
        unit.description.set(UNKNOWN);
        unit.id.set(id);

        UnitTypeInfo metaData = UnitTypeInfo.UNKNOWN;

        unit.iconUrl.set(
            metaData.getIcon().toString()
        );
        unit.imageUrl.set(
            metaData.getImage().toString()
        );

        return unit;
    }

    @Override
    @Nonnull
    public Unit clone() {
        try {
            return (Unit) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Unit
                && id.get() != null
                && id.get().equals(((Unit) obj).id.get())
        ;
    }

    public UnitTypeInfo getTypeInfo() {
        return typeInfo;
    }

    public void setTypeInfo(UnitTypeInfo typeInfo) {
        this.typeInfo = typeInfo;
    }

    public String getNameKey() {
        return typeInfo.getNameKey();
    }

    public String getDescriptionKey() {
        return typeInfo.getDescriptionKey();
    }
}
