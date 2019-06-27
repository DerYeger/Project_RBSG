package de.uniks.se19.team_g.project_rbsg.server.rest.army.persistance.serverResponses;

import de.uniks.se19.team_g.project_rbsg.server.rest.RBSGDataResponse;

import java.util.List;

public class SaveArmyResponse extends RBSGDataResponse<SaveArmyResponse.SaveArmyResponseData> {
       static public class SaveArmyResponseData {

              public String id;
              public String name;
              public List<String> units;
       }
}
