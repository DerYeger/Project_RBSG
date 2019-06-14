package de.uniks.se19.team_g.project_rbsg.util;

import org.springframework.lang.NonNull;

public class Tuple<A, B> {

    public final A first;

    public final B second;

    public Tuple(@NonNull final A first, @NonNull final B second) {
        this.first = first;
        this.second = second;
    }
}
