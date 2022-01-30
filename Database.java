import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class Database {
    private static final Database _inst = new Database();

    public static Database getInstance() {
        return _inst;
    }

    private List<Hero> heroes;
    private List<HeroAlliance> heroAlliances;
    private List<Alliance> alliances;
    private List<Player> players;

    public Database() {
        //TODO optional

    }

    //Parse the CSV files and fill the four lists given above.
    public void parseFiles(String playerCSVFile) throws IOException {
        // Parse herostats.csv if necessary.
        if (this.heroes == null) {
            try {
                File heroesFile = new File("herostats.csv");
                InputStream inputStream = new FileInputStream(heroesFile);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                this.heroes = bufferedReader.lines().map(line -> {
                    String[] heroTokens = line.split(",");
                    return new Hero(
                            heroTokens[0], // name
                            parseInt(heroTokens[1]), // level
                            parseInt(heroTokens[2]), // health
                            parseInt(heroTokens[3]), // mana
                            parseInt(heroTokens[4]), // DPS
                            parseInt(heroTokens[5]), // damageMin
                            parseInt(heroTokens[6]), // damageMax
                            parseDouble(heroTokens[7]), // attackSpeed
                            parseInt(heroTokens[8]), // moveSpeed
                            parseInt(heroTokens[9]), // attackRange
                            parseInt(heroTokens[10]), // magicResist
                            parseInt(heroTokens[11]) // armor
                    );
                }).collect(Collectors.toList());
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Parse alliances.csv if necessary.
        if (this.alliances == null) {
            try {
                File alliancesFile = new File("alliances.csv");
                InputStream inputStream = new FileInputStream(alliancesFile);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                this.alliances = bufferedReader.lines().map(line -> {
                    String[] allianceTokens = line.split(",");
                    return new Alliance(
                            allianceTokens[0], // name
                            parseInt(allianceTokens[1]), // requiredCount
                            parseInt(allianceTokens[2]) // requiredCount
                    );
                }).collect(Collectors.toList());
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Parse heroalliances.csv if necessary.
        if (this.heroAlliances == null) {
            try {
                File heroAlliancesFile = new File("heroalliances.csv");
                InputStream inputStream = new FileInputStream(heroAlliancesFile);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                this.heroAlliances = bufferedReader.lines().map(line -> {
                    String[] heroAllianceTokens = line.split(",");
                    String[] heroAlliances = Arrays.copyOfRange(heroAllianceTokens, 2, heroAllianceTokens.length);
                    return new HeroAlliance(
                            heroAllianceTokens[0], // name
                            parseInt(heroAllianceTokens[1]), // tier
                            heroAlliances // alliances
                    );
                }).collect(Collectors.toList());
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Parse input players file.
        try {
            File playersFile = new File(playerCSVFile);
            InputStream inputStream = new FileInputStream(playersFile);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            this.players = bufferedReader.lines().map(line -> {
                String[] playerTokens = line.split(",");
                List<Hero> playerHeroes = Arrays.stream(Arrays.copyOfRange(playerTokens, 2, playerTokens.length)).map(heroToken -> {
                    String heroName = heroToken.split("\\|")[0];
                    int heroLevel = parseInt(heroToken.split("\\|")[1]);
                    List<Hero> qualifyingHeroes = this.heroes.stream()
                            .filter(hero -> Objects.equals(hero.getName(), heroName) && hero.getLevel() == heroLevel)
                            .collect(Collectors.toList());
                    return qualifyingHeroes.get(0);
                }).collect(Collectors.toList());
                return new Player(
                        playerTokens[0], // name
                        playerHeroes // heroes
                );
            }).collect(Collectors.toList());
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
