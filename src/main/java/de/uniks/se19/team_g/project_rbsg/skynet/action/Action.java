package de.uniks.se19.team_g.project_rbsg.skynet.action;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface Action {

    ObjectNode getServerCommand();
}
