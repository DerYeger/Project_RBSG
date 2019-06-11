package de.uniks.se19.team_g.project_rbsg.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Unit {

    public final SimpleStringProperty imageUrl = new SimpleStringProperty();

    public final SimpleIntegerProperty health = new SimpleIntegerProperty();
    public final SimpleIntegerProperty physicalResistance = new SimpleIntegerProperty();
    public final SimpleIntegerProperty magicResistance = new SimpleIntegerProperty();
    public final SimpleIntegerProperty speed = new SimpleIntegerProperty();
    public final SimpleIntegerProperty attack = new SimpleIntegerProperty();
    public final SimpleIntegerProperty spellPower = new SimpleIntegerProperty();

    public final SimpleStringProperty name = new SimpleStringProperty();
}
