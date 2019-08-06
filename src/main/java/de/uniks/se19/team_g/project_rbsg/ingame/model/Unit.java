package de.uniks.se19.team_g.project_rbsg.ingame.model;

import de.uniks.se19.team_g.project_rbsg.configuration.flavor.UnitTypeInfo;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableIntegerValue;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Jan Müller
 */
public class Unit implements Selectable, Hoverable {

    @NonNull
    private final String id;

    private Game game;

    private Player leader;

    private SimpleObjectProperty<Cell> position = new SimpleObjectProperty<>();

    private final ObjectProperty<Game> selected = new SimpleObjectProperty<>();
    private final BooleanBinding isSelected = selected.isNotNull();

    private final ObjectProperty<Game> hoveredIn = new SimpleObjectProperty<>();
    private final BooleanBinding isHovered = hoveredIn.isNotNull();

    private BooleanProperty attackReady = new SimpleBooleanProperty(true);

    private UnitTypeInfo unitType;

    final private SimpleIntegerProperty remainingMovePoints = new SimpleIntegerProperty(0);

    private int mp;
    private SimpleIntegerProperty hp = new SimpleIntegerProperty();
    private int maxHp;

    private ArrayList<UnitTypeInfo> canAttack = new ArrayList<>();

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

    public ReadOnlyObjectProperty<Cell> positionProperty() {
        return position;
    }

    public Cell getPosition() {
        return position.get();
    }

    public UnitTypeInfo getUnitType() {
        return unitType;
    }

    public int getMp() {
        return mp;
    }

    public int getHp() {
        return hp.get();
    }

    public SimpleIntegerProperty hpProperty() {
        return hp;
    }

    public ArrayList<UnitTypeInfo> getCanAttack() {
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

    public Unit setUnitType(@NonNull final UnitTypeInfo unitType) {
        this.unitType = unitType;
        return this;
    }

    public Unit setMp(@NonNull final int mp) {
        this.mp = mp;
        return this;
    }

    public Unit setHp(@NonNull final int hp) {
        this.hp.set(hp);
        return this;
    }

    public Unit setCanAttack(@NonNull final Collection<UnitTypeInfo> canAttack) {
        this.canAttack.clear();
        this.canAttack.addAll(canAttack);
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

    public void setSelected(boolean selected) {
        // keep it to not break compiling for now
    }


    public int getRemainingMovePoints() {
        return remainingMovePoints.get();
    }

    public ReadOnlyIntegerProperty remainingMovePointsProperty() {
        return remainingMovePoints;
    }

    public Unit setRemainingMovePoints(int remainingMovePoints) {
        this.remainingMovePoints.set(remainingMovePoints);
        return this;
    }

    public boolean isSelected() {
        return isSelected.get();
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void setSelectedIn(@Nullable Game game) {
        // note that this should be just a toggle between null and a game, not between two games
        Game lastState = selected.get();

        if (lastState == game) {
            return;
        }

        selected.set(game);

        if (lastState != null) {
            lastState.setSelected(null);
        }
        if (game != null) {
            game.setSelected(this);
        }
    }

    @Override
    public ObservableBooleanValue selectedProperty() {
        return isSelected;
    }

    @Override
    public boolean isHovered() {
        return isHovered.get();
    }

    @Override
    public ObservableBooleanValue hoveredProperty() {
        return isHovered;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void setHoveredIn(@Nullable Game game) {
        Game lastHoveredIn = hoveredIn.get();

        if (game == lastHoveredIn) {
            return;
        }

        hoveredIn.set(game);

        if (lastHoveredIn != null) {
            lastHoveredIn.setHovered(null);
        }

        if (game != null) {
            game.setHovered(this);
        }
    }

    public int getMaxHp ()
    {
        return maxHp;
    }

    public void setMaxHp (int maxHp)
    {
        this.maxHp = maxHp;
    }

    public boolean canAttack(Unit unit) {
        return unit != null
            && isAttackReady()
            && unit.getLeader() != this.leader
            && canAttack.contains(unit.unitType);
    }
    public boolean isAttackReady() {
        return attackReady.get();
    }

    public ObservableBooleanValue attackReadyProperty() {
        return attackReady;
    }

    public void setAttackReady(boolean attackReady) {
        this.attackReady.set(attackReady);
    }
}
