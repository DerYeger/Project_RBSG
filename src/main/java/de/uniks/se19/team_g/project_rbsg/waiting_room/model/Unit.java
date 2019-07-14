package de.uniks.se19.team_g.project_rbsg.waiting_room.model;

import javafx.beans.property.SimpleObjectProperty;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.Collection;

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

        position = new SimpleObjectProperty<>();
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

    public Unit setGame(@Nullable final Game game) {
        if (this.game == game) return this;
        if (this.game != null) this.game.doRemoveUnit(this);
        doSetGame(game);
        if (game != null) game.doAddUnit(this);
        return this;
    }

    Unit doSetGame(@Nullable final Game game) {
        this.game = game;
        return this;
    }

    public Unit setLeader(@Nullable final Player leader) {
        if (this.leader == leader) return this;
        if (this.leader != null) this.leader.doRemoveUnit(this);
        doSetLeader(leader);
        if (leader != null) leader.doAddUnit(this);
        return this;
    }

    void doSetLeader(@Nullable final Player leader) {
        this.leader = leader;
    }

    public Unit setPosition(@Nullable final Cell position) {
        if (this.position.get() == position) return this;
        if (this.position.get() != null) this.position.get().doSetUnit(null);
        if (position != null) position.doSetUnit(this);
        doSetPosition(position);
        return this;
    }

    void doSetPosition(@Nullable final Cell position) {
        this.position.set(position);
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

    public Unit setCanAttack(@NonNull final Collection<UnitType> canAttack) {
        this.canAttack = new ArrayList<>(canAttack);
        return this;
    }

    public void remove() {
        setGame(null);
        setLeader(null);
        setPosition(null);
    }

    @Override
    public String toString() {
        return "(" + unitType + ", mp : " + mp + ", hp: " + hp + ")";
    }
}
