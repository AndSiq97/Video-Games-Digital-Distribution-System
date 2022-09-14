import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * A class that reads and writes to the Game inventory database.
 */
public class GameDatabase {

    static Path path = Paths.get("Games.txt");

    /**
     * Update the Game Database from the previous day's games.
     * @param games List of purchased games from the past day.
     */
    protected void update(ArrayList<Game> games) {
        try {
            List<String> allLines = new ArrayList<>(Files.readAllLines(path, StandardCharsets.UTF_8));

            for (Game game: games) {

                boolean found = false;

                // Search for Game
                for (int i = 0; i < allLines.size(); i++) {
                    if (allLines.get(i).equalsIgnoreCase(game.getTitle())) {
                        for (String username: game.owners) {
                            allLines.set(i + 1, username);
                        }
                        found = true;
                        break;
                    }
                }

                // Game didn't exist
                if (!(found)) {
                    allLines.add(game.getTitle());
                    for (String username: game.owners) {
                        allLines.add(username);
                    }
                    allLines.add("\n");
                }
            }

            Files.write(path, allLines, StandardCharsets.UTF_8);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Return the list of Games that were purchased before the current day.
     * @return List of games that were purchased before the current day.
     */
    protected ArrayList<Game> getGames() {
        try {
            ArrayList<Game> games = new ArrayList<>();
            List<String> allLines = new ArrayList<>(Files.readAllLines(path, StandardCharsets.UTF_8));

            for (int i = 0; i < allLines.size(); i++) {

                while(allLines.get(i).equalsIgnoreCase("")) {   // Skip over whitespace
                    i++;
                    if (i >= allLines.size()) {
                        return games;
                    }
                }
                Game g = new Game(allLines.get(i));                        // Line refers to game
                i++;                                                       // Next line till whitespace are owners
                while(!allLines.get(i).equalsIgnoreCase("")) {
                    g.addOwner(allLines.get(i));
                    i++;
                }
                games.add(g);
            }

            return games;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}