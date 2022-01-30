import java.io.IOException;
import java.util.List;

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
            for (Hero hero : result1) {
                System.out.println(hero);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
