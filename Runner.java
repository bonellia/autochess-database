import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Runner {
    public static void main(String[] args) {
        // Test parsing.
        try {
            Database.getInstance().parseFiles("players.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Test getHeroesOfParticularAlliance.
        try {
            System.out.println("Testing getHeroesOfParticularAlliance for Mage-3");
            List<Hero> result1 = Database.getInstance().getHeroesOfParticularAlliance("Mage", 3);
            result1.forEach(System.out::println);
            System.out.println("Testing getHeroAllianceMatchingTier for 2,2");
            Map<Integer, List<HeroAlliance>> result2 = Database.getInstance()
                    .getHeroAllianceMatchingTier(2, 2);
            result2.forEach((key, value) -> {
                System.out.println("Tier: " + key);
                value.forEach(System.out::println);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
