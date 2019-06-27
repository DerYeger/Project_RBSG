package de.uniks.se19.team_g.project_rbsg.server.rest.army.deletion.serverResponses;

import de.uniks.se19.team_g.project_rbsg.server.rest.RBSGDataResponse;

import java.util.List;

public class DeleteArmyResponse extends RBSGDataResponse<DeleteArmyResponse.DeleteArmyResponseData> {
    static public class DeleteArmyResponseData {

        public String id;
        public String name;
        public List<String> units;
    }

}
