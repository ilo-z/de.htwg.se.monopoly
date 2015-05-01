package de.htwg.monopoly.persistence.couchdb;

import de.htwg.monopoly.entities.IFieldObject;
import de.htwg.monopoly.util.PlayerIcon;
import lombok.Getter;
import lombok.Setter;
import org.ektorp.support.CouchDbDocument;

import java.util.List;

/**
 * @author Timi.
 */

public class PersistenUser extends CouchDbDocument{
    
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private int budget;
    @Getter
    @Setter
    private int position;
    @Getter
    @Setter
    private int prisonRound;
    @Getter
    @Setter
    private boolean inPrison;
    @Getter
    @Setter
    private List<IFieldObject> ownership;
    @Getter
    @Setter
    private int prisonFreeCard;
    @Getter
    @Setter
    private PlayerIcon icon;
    
    
}