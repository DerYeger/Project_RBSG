package de.uniks.se19.team_g.project_rbsg.ingame.state;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.annotation.Nonnull;

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

    public void timeTravel(HistoryEntry target)
    {
        if (target == null) {
            return;
        }

        if (target.isApplied()) {
            doTimeTravelBackwards(target);
        } else {
            doTimeTravelForward(target);
        }
    }

    private void doTimeTravelForward(@Nonnull HistoryEntry target) {
        while (current.getValue() != null && current.getValue() != target && current.getValue().getNext() != null) {
            forward();
        }

        if (current.getValue() != target) {
            throw new IllegalStateException("couldn't travel forward to target");
        }
    }

    private void doTimeTravelBackwards(@Nonnull HistoryEntry target) {
        while (current.getValue() != null && current.getValue() != target) {
            back();
        }

        if (current.getValue() != target) {
            throw new IllegalStateException("couldn't travel back to target");
        }
    }

    public ObservableList<HistoryEntry> getEntries() {
        return roEntries;
    }

    public HistoryEntry getCurrent() {
        return current.getValue();
    }

    public ObservableValue<HistoryEntry> currentProperty() {
        return current;
    }

    public boolean isLatest() {
        return tail == this.current.getValue();
    }


    public HistoryEntry getTail() {
        return tail;
    }

    public void setTail(HistoryEntry tail) {
        this.tail = tail;
    }

}
