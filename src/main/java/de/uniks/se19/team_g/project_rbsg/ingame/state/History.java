package de.uniks.se19.team_g.project_rbsg.ingame.state;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class History {

    private HistoryEntry head;
    private HistoryEntry tail;
    private Property<HistoryEntry> current = new SimpleObjectProperty<>();

    // just observe the entries read only
    private ObservableList<HistoryEntry> entries = FXCollections.observableArrayList();
    private ObservableList<HistoryEntry> roEntries = FXCollections.unmodifiableObservableList(entries);

    public void push(Action action) {
        HistoryEntry newEntry = new HistoryEntry();
        newEntry.setAction(action);

        if (tail == null) {
            head = newEntry;
            tail = newEntry;
        } else {
            tail.setNext(newEntry);
            newEntry.setPrevious(tail);
            tail = newEntry;
        }

        entries.add(newEntry);
    }

    /**
     * forwards the pointer of the current history entry and applys the corresponding action
     */
    public void forward() {
        HistoryEntry current = this.current.getValue();
        HistoryEntry next = current != null ? current.getNext() : head;
        if (next == null) {
            return;
        }
        next.getAction().run();
        next.setApplied(true);
        this.current.setValue(next);
    }

    public void back() {
        HistoryEntry current = this.current.getValue();
        if (current == null) {
            return;
        }
        current.getAction().undo();
        current.setApplied(false);
        this.current.setValue(current.getPrevious());
    }

    public ObservableList<HistoryEntry> getEntries() {
        return roEntries;
    }
}
