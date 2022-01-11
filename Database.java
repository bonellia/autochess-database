import java.io.IOException;
import java.util.*;

public class Database {
    private static final Database _inst = new Database();
    public static Database getInstance() { return _inst; }

    private List<Hero> heroes;
    private List<HeroAlliance> heroAlliances;
    private List<Alliance> alliances;
    private List<Player> players;

    public Database() {
        //TODO optional

    }

    //Parse the CSV files and fill the four lists given above.
    public void parseFiles(String playerCSVFile) throws IOException {
        //TODO
    }

    //Gets the heroes belonging to a particular alliance and sorts them according to their DPS. It should only return
    //count number of heroes. Heroes should be distinct in a sense that, different levels of the same hero should not be
    //in the result.
    //15pts
    public List<Hero> getHeroesOfParticularAlliance(String alliance, int count) {
        //TODO
        return null;
    }

    //Returns a map of HeroAlliances based on tier where the alliance required count and alliance level counts match.
    //15pts
    public Map<Integer, List<HeroAlliance>> getHeroAllianceMatchingTier(int allianceRequiredCount, int allianceLevelCount) {
        //TODO
        return null;
    }

    //Return the heroes of each player that have bigger than the mana, health and move speed given as arguments.
    //10pts
    public List<List<Hero>> getPlayerHeros(int mana, int health, int moveSpeed) {
        return null;
    }

    //Calculate and print the average maximum damage of players whose heroes has minimum damage is bigger than the given first argument.
    //10 pts
    public void printAverageMaxDamage(int minDamage) {
        //TODO
    }

    //In this function, print each player and its heroes. However, you should only print heroes belonging to
    // any of the particular alliances and whose attack speed is smaller than or equal to the value given.
    //30pts
    public void printAlliances(String[] alliances, double attackSpeed) {
        //TODO
    }
}
