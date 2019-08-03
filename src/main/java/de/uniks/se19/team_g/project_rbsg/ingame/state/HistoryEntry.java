package de.uniks.se19.team_g.project_rbsg.ingame.state;

public class HistoryEntry {

    private HistoryEntry next;
    private HistoryEntry previous;
    private Action action;

    public HistoryEntry getNext() {
        return next;
    }

    public void setNext(HistoryEntry next) {
        this.next = next;
    }

    public HistoryEntry getPrevious() {
        return previous;
    }

    public void setPrevious(HistoryEntry previous) {
        this.previous = previous;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
}
