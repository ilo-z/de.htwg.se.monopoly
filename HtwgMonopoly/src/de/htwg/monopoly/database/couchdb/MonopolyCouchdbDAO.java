package de.htwg.monopoly.database.couchdb;

import de.htwg.monopoly.context.IMonopolyGame;
import de.htwg.monopoly.database.IMonopolyDAO;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;

import java.net.MalformedURLException;
import java.util.List;

/**
 * @author Timi.
 */
public class MonopolyCouchdbDAO implements IMonopolyDAO {

    CouchDbConnector db;

    public MonopolyCouchdbDAO() {
        HttpClient client = null;
        try {
            client = new StdHttpClient.Builder().url("http://lenny2.in.htwg-konstanz.de:5984").build();
        } catch (MalformedURLException e) {
        }
        CouchDbInstance dbInstance = new StdCouchDbInstance(client);
        db = dbInstance.createConnector("htwg_monopoly", true);
        db.createDatabaseIfNotExists();
    }

    @Override
    public void saveGame(IMonopolyGame context) {

    }

    @Override
    public IMonopolyGame getGameById(String id) {
        return null;
    }

    @Override
    public void deleteGameById(String id) {

    }

    @Override
    public void updateGame(IMonopolyGame game) {

    }

    @Override
    public List<IMonopolyGame> getAllGames() {
        return null;
    }

    @Override
    public boolean containsGameById(String id) {
        return false;
    }
}
