package de.uniks.se19.team_g.project_rbsg.waiting_room.model;

import javafx.beans.property.SimpleObjectProperty;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.ArrayList;

/**
 * @author Jan Müller
 */
public class Unit {

    @NonNull
    private final String id;

    private Game game;

    private Player leader;

    private SimpleObjectProperty<Cell> position;

    private UnitType unitType;

    private int mp;
    private int hp;

    private ArrayList<UnitType> canAttack;

    public Unit(@NonNull final String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Game getGame() {
        return game;
    }

    public Player getLeader() {
        return leader;
    }

    public SimpleObjectProperty<Cell> getPosition() {
        return position;
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public int getMp() {
        return mp;
    }

    public int getHp() {
        return hp;
    }

    public ArrayList<UnitType> getCanAttack() {
        return canAttack;
    }

    public Unit setGame(@NonNull final Game game) {
        doSetGame(game);
        game.withUnit(this);
        return this;
    }

    Unit doSetGame(@NonNull final Game game) {
        this.game = game;
        return this;
    }

    public Unit setLeader(@NonNull final Player leader) {
        if (this.leader == leader) return this;
        doSetLeader(leader);
        leader.doAddUnit(this);
        return this;
    }

    void doSetLeader(@NonNull final Player leader) {
        this.leader = leader;
    }

    public Unit setPosition(@Nullable final Cell position) {
        if (this.position.get() == position) return this;
        if (this.position != null) this.position.get().setUnit(null);
        doSetPosition(position);
        if (position != null) position.doSetUnit(this);
        return this;
    }

    void doSetPosition(@Nullable final Cell position) {
        this.position = new SimpleObjectProperty<>(position);
    }

    public Unit setUnitType(@NonNull final UnitType unitType) {
        this.unitType = unitType;
        return this;
    }

    public Unit setMp(@NonNull final int mp) {
        this.mp = mp;
        return this;
    }

    public Unit setHp(@NonNull final int hp) {
        this.hp = hp;
        return this;
    }

    public Unit setCanAttack(@NonNull final ArrayList<UnitType> canAttack) {
        this.canAttack = canAttack;
        return this;
    }

    @Override
    public String toString() {
        return "(" + unitType + ", mp : " + mp + ", hp: " + hp + ")";
    }
}
