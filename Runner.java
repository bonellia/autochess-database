import java.io.IOException;

public class Runner {
    public static void main(String[] args) {
        System.out.println("anan");
        Database db = new Database();
        try {
            db.parseFiles("players.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
