package de.uniks.se19.team_g.project_rbsg.skynet;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Game;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import de.uniks.se19.team_g.project_rbsg.skynet.action.*;
import de.uniks.se19.team_g.project_rbsg.skynet.behaviour.Behaviour;
import de.uniks.se19.team_g.project_rbsg.skynet.behaviour.fallback.FallbackBehaviour;
import de.uniks.se19.team_g.project_rbsg.skynet.exception.SkynetExcpetion;
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
    private AutoPlayer bot;
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

        bot = new AutoPlayer(this);
    }

    public Thread getBotThread()
    {
        return botThread;
    }

    public AutoPlayer getBot()
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
        try {
            if (!game.getCurrentPlayer().equals(player)) {
                throw new SkynetExcpetion("Not my turn");
            }

            final Behaviour currentBehaviour = getCurrentBehaviour();

            if (currentBehaviour == null) {
                throw new SkynetExcpetion("No behaviour configured");
            }

            query(currentBehaviour);
        } catch (final SkynetExcpetion e) {
            logger.info(e.getMessage());
        }

        return this;
    }

    private Behaviour getCurrentBehaviour()
    {
        return behaviours.get(game.getPhase());
    }

    private void query(@NonNull final Behaviour behaviour) throws SkynetExcpetion {
        final Optional<? extends Action> action = behaviour.apply(game, player);

        if (action.isPresent())
        {
            actionExecutor.execute(action.get());
        } else {
            final Action fallback = behaviours
                    .get("fallback")
                    .apply(game, player)
                    .orElseThrow(() -> new SkynetExcpetion("Skynet stalled. No fallback available"));
            actionExecutor.execute(fallback);
        }
    }

    public void startBot()
    {
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
