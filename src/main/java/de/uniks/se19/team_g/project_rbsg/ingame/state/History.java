package de.uniks.se19.team_g.project_rbsg.ingame.state;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;

public class History {

    private HistoryEntry head;
    private HistoryEntry tail;
    private Property<HistoryEntry> current = new SimpleObjectProperty<>();

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
    }

    /**
     * forwards the pointer of the current history entry and applys the corresponding action
     */
    public void forward() {
        HistoryEntry current = this.current.getValue();
        HistoryEntry next = current != null ? current.getNext() : head;
        if (next == null) {
            throw new IllegalStateException("can't forward history");
        }
        next.getAction().run();
        this.current.setValue(next);
    }
}
