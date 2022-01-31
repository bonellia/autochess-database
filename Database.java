import java.io.*;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
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
                List<Hero> playerHeroes = Arrays
                        .stream(Arrays.copyOfRange(
                                playerTokens,
                                1, // I had a bug here, it was 2, thanks for the remark on discussion forum!
                                playerTokens.length)).map(heroToken -> {
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
        // Gonna split into two for sake of readability.
        // 1. Find hero names for the given alliance.
        List<String> qualifyingHeroNames = this.heroAlliances
                .stream()
                .filter(
                        ha -> ha.getAlliances()
                                .stream()
                                .anyMatch(a -> a.equals(alliance))) // At this point, we have qualifying hero alliances.
                .map(HeroAlliance::getName).collect(Collectors.toList()); // Now we have names of heroes.
        // 2. Find heroes with the highest dps in a map, then from values of that map, return top n heroes.
        return this.heroes
                .stream()
                .filter(
                        h -> qualifyingHeroNames.contains(h.getName()))
                .sorted(Comparator.comparing((Hero::getDPS)))
                .collect(
                        Collectors.toMap(
                                Hero::getName,
                                Function.identity(),
                                BinaryOperator.maxBy(Comparator.comparing(Hero::getDPS))
                        )
                ) // At this point, we have heroes without duplicates, highest dps per hero in a map.
                .values() // Could split the code here (actually did for debugging) but chaining looked readable enough.
                .stream()
                .sorted(
                        Comparator
                                .comparing(Hero::getDPS)
                                .reversed())
                .limit(count) // Final touch to get top n heroes.
                .collect(Collectors.toList());
    }

    //Returns a map of HeroAlliances based on tier where the alliance required count and alliance level counts match.
    //15pts
    public Map<Integer, List<HeroAlliance>> getHeroAllianceMatchingTier(int allianceRequiredCount, int allianceLevelCount) {
        // Gonna split into two for sake of readability.
        // 1. Find alliance names for the given counts.
        Set<String> qualifyingAlliances = this.alliances
                .stream()
                .filter(a -> a.getRequiredCount() == allianceRequiredCount && a.getLevelCount() == allianceLevelCount)
                .map(Alliance::getName).collect(Collectors.toSet());
        // Using the set above, filter hero alliances with correct alliances.
        return this.heroAlliances
                .stream()
                .filter(ha -> {
                    HashSet<String> intersection = new HashSet<>(ha.getAlliances()); // Initialize it from current ha.
                    intersection.retainAll(qualifyingAlliances); // Get rid of non-qualifying alliances.
                    return !intersection.isEmpty(); // If intersection is not empty, keep within stream.
                })
                .collect(Collectors.groupingBy(HeroAlliance::getTier));
    }

    //Return the heroes of each player that have bigger than the mana, health and move speed given as arguments.
    //10pts
    public List<List<Hero>> getPlayerHeros(int mana, int health, int moveSpeed) {
        // Initially I created an intermediary list of qualifying heroes, then I realized I can just do it in filter.
        // Note that this was only possible since players already have a list of Hero objects, instead of "Puck|3" etc.
        return this.players
                .stream()
                .map(player -> player
                        .getHeroes()
                        .stream()
                        .filter(h ->
                                h.getMana() > mana &&
                                        h.getHealth() > health &&
                                        h.getMoveSpeed() > moveSpeed
                        ).
                        collect(Collectors.toList())
                )
                .collect(Collectors.toList());
    }

    //Calculate and print the average maximum damage of players whose heroes has minimum damage is bigger than the given first argument.
    //10 pts
    public void printAverageMaxDamage(int minDamage) {
        this.players.forEach(player -> {
            System.out.println(
                    player.getHeroes()
                            .stream()
                            .filter(hero -> hero.getDamageMin() > minDamage)
                            .map(Hero::getDamageMax)
                            .mapToDouble(d -> d)
                            .summaryStatistics().getAverage()
            );
        });
    }

    //In this function, print each player and its heroes. However, you should only print heroes belonging to
    // any of the particular alliances and whose attack speed is smaller than or equal to the value given.
    //30pts
    public void printAlliances(String[] alliances, double attackSpeed) {
        // This one seemed lengthy enough to separate as well.
        // First, get rid of heroes with high attack speed.
        HashSet<String> heroesWithCorrectAttackSpeed = this.heroes
                .stream()
                .filter(hero -> hero.getAttackSpeed() <= attackSpeed)
                .map(Hero::getName)
                .collect(Collectors.toCollection(HashSet::new));
        // I could just && the following withing previous set's filter predicate, but readability would be awful.
        Set<String> heroesWithCorrectAlliance = this.heroAlliances
                .stream()
                .filter(ha -> {
                    HashSet<String> intersection = new HashSet<>(ha.getAlliances()); // Initialize it from current ha.
                    intersection.retainAll(new HashSet<>(Arrays.asList(alliances))); // Intersect with correct alliances.
                    return !intersection.isEmpty();
                }).map(HeroAlliance::getName)
                .collect(Collectors.toSet());

        heroesWithCorrectAlliance.retainAll(heroesWithCorrectAttackSpeed); // This intersection only has correct heroes.

        this.players.stream()
                .filter(player -> player.getHeroes()
                        .stream()
                        .anyMatch(hero -> heroesWithCorrectAlliance.contains(hero.getName())
                        )) // At this point we have only qualifying players.
                .forEach(player -> {
                    System.out.println(player.getName()); // We can print the player name.
                    player.getHeroes()
                            .stream()
                            .filter(hero -> heroesWithCorrectAlliance
                                    .contains(hero.getName())) // Qualifying heroes of the current player.
                            .forEach(hero ->
                                    System.out.println("Name: " + hero.getName() + " Level: " + hero.getLevel()));
                });
    }
}
