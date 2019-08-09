package de.uniks.se19.team_g.project_rbsg.skynet;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Game;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import de.uniks.se19.team_g.project_rbsg.skynet.action.*;
import de.uniks.se19.team_g.project_rbsg.skynet.behaviour.Behaviour;
import de.uniks.se19.team_g.project_rbsg.skynet.behaviour.FallbackBehaviour;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import java.util.HashMap;
import java.util.Optional;

public class Skynet
{

    private final ActionExecutor actionExecutor;
    private final Game game;
    private final Player player;
    private Thread botThread;
    private Bot bot;
    private HashMap<String, Behaviour> behaviours;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public Skynet(@NonNull final ActionExecutor actionExecutor,
                  @NonNull final Game game,
                  @NonNull final Player player)
    {
        this.actionExecutor = actionExecutor;
        this.game = game;
        this.player = player;

        behaviours = new HashMap<>();
        behaviours.put("fallback", new FallbackBehaviour());
    }

    public Thread getBotThread()
    {
        return botThread;
    }

    public Bot getBot()
    {
        return bot;
    }

    public Skynet addBehaviour(@NonNull final Behaviour behaviour,
                               @NonNull final String... keys)
    {
        for (final String key : keys)
        {
            behaviours.put(key, behaviour);
        }
        return this;
    }

    public Skynet turn()
    {
        if (!game.getCurrentPlayer().equals(player))
        {
            //ignore for now
            return this;
        }

        final Behaviour currentBehaviour = getCurrentBehaviour();
        if (currentBehaviour == null) return this;

        final Optional<? extends Action> action = currentBehaviour.apply(game, player);

        if (action.isEmpty())
        {
            actionExecutor.execute((PassAction) behaviours.get("fallback").apply(game, player).get());
        } else if (action.get() instanceof MovementAction)
        {
            actionExecutor.execute((MovementAction) action.get());
        } else if (action.get() instanceof AttackAction)
        {
            actionExecutor.execute((AttackAction) action.get());
        }

        return this;
    }

    private Behaviour getCurrentBehaviour()
    {
        return behaviours.get(game.getPhase());
    }

    public void startBot()
    {
        if (bot == null)
        {
            logger.debug("Creating bot");
            bot = new Bot(this);
        }
        botThread = bot.start();
    }

    public void stopBot()
    {
        bot.stop();
    }

    public boolean isBotRunning()
    {
        if (botThread == null)
        {
            return false;
        }

        return botThread.isAlive();
    }
}
