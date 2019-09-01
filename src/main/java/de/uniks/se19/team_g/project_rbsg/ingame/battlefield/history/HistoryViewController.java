package de.uniks.se19.team_g.project_rbsg.ingame.battlefield.history;

import de.uniks.se19.team_g.project_rbsg.ingame.IngameContext;
import de.uniks.se19.team_g.project_rbsg.ingame.state.History;
import de.uniks.se19.team_g.project_rbsg.ingame.state.HistoryEntry;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import org.springframework.stereotype.Component;

@Component
public class HistoryViewController {

    public ListView<HistoryEntry> historyView;
    public Button liveButton;

    final private ActionRenderer actionRenderer;

    private IngameContext context;
    private History history;
    private ChangeListener<HistoryEntry> onHistoryStateChanged = this::onHistoryStateChanged;

    public HistoryViewController(ActionRenderer actionRenderer) {
        this.actionRenderer = actionRenderer;
    }

    public void configureContext(IngameContext context) {
        this.context = context;

        configureHistory();
    }

    private void configureHistory() {
        history = context.getModelManager().getHistory();

        historyView.setCellFactory(param -> new HistoryEntryCell(actionRenderer));
        historyView.setItems(
            history.getEntries().filtered(
                historyEntry -> actionRenderer.supports(historyEntry.getAction())
            )
        );

        historyView.getSelectionModel().selectedItemProperty().addListener(this::onSelectedEntryChanged);

        history.currentProperty().addListener(onHistoryStateChanged);
        onHistoryStateChanged(null, null, null);
    }

    private void handleEntryClicked(HistoryEntry historyEntry, MouseEvent mouseEvent) {
        history.timeTravel(historyEntry);
    }

    private void onHistoryStateChanged(ObservableValue<? extends HistoryEntry> observable, HistoryEntry oldValue, HistoryEntry newValue) {
        historyView.getSelectionModel().select(history.getCurrent());
        historyView.scrollTo(history.getCurrent());
    }


    private void onSelectedEntryChanged(ObservableValue<? extends HistoryEntry> observable, HistoryEntry oldValue, HistoryEntry newValue) {
        HistoryEntry selectedItem = historyView.getSelectionModel().getSelectedItem();
        history.timeTravel(selectedItem);
    }

    public void getLive(){
        history.timeTravel(history.getTail());
    }
}
