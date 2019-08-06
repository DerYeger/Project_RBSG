package de.uniks.se19.team_g.project_rbsg.ingame.event;

import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.Tour;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;
import org.springframework.lang.NonNull;

import javax.annotation.Nonnull;

public class IngameApi {

    private GameEventManager gameEventManager;

    public void setGameEventManager(@Nonnull GameEventManager gameEventManager) {

        this.gameEventManager = gameEventManager;
    }

    public GameEventManager getGameEventManager() {
        return gameEventManager;
    }

    public void move(@NonNull final Unit unit, @NonNull final Tour tour) {
        gameEventManager.sendMessage(CommandBuilder.moveUnit(unit, tour.getPath()));
    }

    public void attack(Unit attacker, Unit target) {
        gameEventManager.sendMessage(buildAttack(attacker, target));
    }

    @Nonnull
    public AttackCommand buildAttack(Unit attacker, Unit target) {

        AttackCommand attackCommand = new AttackCommand();
        attackCommand.data.unitId = attacker.getId();
        attackCommand.data.toAttackId = target.getId();

        return attackCommand;
    }

    public static class BasicCommand {
        final public String messageType = "command";
        final public String action;

        public BasicCommand(String action) {
            this.action = action;
        }
    }

    public static class Command<T> extends BasicCommand {
        public T data;

        public Command(String action) {
            super(action);
        }
    }

    public static class AttackCommand extends Command<AttackCommand.Data> {
        public AttackCommand() {
            super("attackUnit");
            data = new Data();
        }

        public static class Data {
            public String unitId;
            public String toAttackId;
        }
    }

}
