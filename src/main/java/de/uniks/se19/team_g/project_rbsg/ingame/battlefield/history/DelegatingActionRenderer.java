package de.uniks.se19.team_g.project_rbsg.ingame.battlefield.history;

import de.uniks.se19.team_g.project_rbsg.ingame.state.Action;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class DelegatingActionRenderer implements ActionRenderer {

    private final List<ActionRenderer> renderers = new ArrayList<>();
    private Action lastAction;
    private ActionRenderer lastMatch;

    public void addRenderer(ActionRenderer renderer) {
        renderers.add(renderer);
    }

    @Override
    public HistoryRenderData render(Action action) {
        ActionRenderer renderer = getDelegatedRenderer(action);
        return renderer != null ? renderer.render(action) : null;
    }

    @Override
    public boolean supports(Action action) {
        return getDelegatedRenderer(action) != null;
    }

    @Nullable
    private ActionRenderer getDelegatedRenderer(Action action) {
        if (action != lastAction) {
            ActionRenderer match = null;

            for (ActionRenderer renderer : renderers) {
                if (renderer.supports(action)) {
                    match = renderer;
                    break;
                }
            }

            this.lastAction = action;
            this.lastMatch = match;
        }

        return lastMatch;
    }
}
