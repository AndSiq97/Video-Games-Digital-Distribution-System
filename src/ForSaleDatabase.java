import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * A class that reads and writes to the ForSale inventory database.
 */
public class ForSaleDatabase {

    static Path path = Paths.get("ForSale.txt");

    /**
     * Updates the ForSale Database with the past day's inventory.
     * @param games List of games on sale in the past day
     */
    protected void update(ArrayList<Game> games) {
        try {
            ArrayList<String> allLines = new ArrayList<>();

            int i = 0;
            for (Game game: games) {


                for (String owner: game.owners) {
                    allLines.add(game.getTitle());
                    allLines.add(owner);
                    allLines.add(Double.toString(game.getPrice()));
                    allLines.add(Double.toString(game.getDiscount()));
                    }
                allLines.add("\n");
                }

            Files.write(path, allLines, StandardCharsets.UTF_8);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Return the games that were on sale in the database
     * @return games that were on sale from the previous day.
     */
    protected ArrayList<Game> getGames() {
        try {
            ArrayList<Game> games = new ArrayList<>();
            List<String> allLines = new ArrayList<>(Files.readAllLines(path, StandardCharsets.UTF_8));

            for (int i = 0; i < allLines.size(); i++) {

                while(allLines.get(i).equalsIgnoreCase("")) {   // Skip over whitespace
                    i++;
                    if (i == allLines.size()) {
                        return games;
                    }
                }  // Next line till whitespace is info
                while(!allLines.get(i).equalsIgnoreCase("")) {
                    String title = allLines.get(i);
                    String owner = allLines.get(i + 1);
                    double price = Double.parseDouble(allLines.get(i + 2));
                    double discount = Double.parseDouble(allLines.get(i + 3));

                    Game g = new Game(title, price, discount);
                    g.addOwner(owner);

                    games.add(g);

                    i += 4;
                }
            }

            return games;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}