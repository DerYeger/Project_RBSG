package de.uniks.se19.team_g.project_rbsg.ingame.model;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.ArrayList;

public class Unit {

    private final String id;

    private Game game;

    private Player leader;

    private Cell position;

    private Type type;

    private int mp;
    private int hp;

    private ArrayList<Type> canAttack;

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

    public Cell getPosition() {
        return position;
    }

    public Type getType() {
        return type;
    }

    public int getMp() {
        return mp;
    }

    public int getHp() {
        return hp;
    }

    public ArrayList<Type> getCanAttack() {
        return canAttack;
    }

    public Unit setGame(@NonNull final Game game) {
        doSetGame(game);
        game.withUnit(this);
        return this;
    }

    Unit doSetGame(@NonNull final Game game) {

        return this;
    }

    public Unit setLeader(@NonNull final Player leader) {
        doSetLeader(leader);
        leader.doAddUnit(this);
        return this;
    }

    public void doSetLeader(@NonNull final Player leader) {
        this.game = game;
    }

    public Unit setPosition(@Nullable final Cell position) {
        if (this.position != null) this.position.doSetUnit(null);
        doSetPosition(position);
        if (position != null) position.doSetUnit(this);
        return this;
    }

    void doSetPosition(@Nullable final Cell position) {
        this.position = position;
    }

    public Unit setType(@NonNull final Type type) {
        this.type = type;
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

    public Unit setCanAttack(@NonNull final ArrayList<Type> canAttack) {
        this.canAttack = canAttack;
        return this;
    }
}
