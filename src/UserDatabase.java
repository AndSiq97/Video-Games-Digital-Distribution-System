import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class that reads and writes to the Users database.
 */
public class UserDatabase {


    static Path path = Paths.get("Users.txt");

    /**
     * Updates the User Database with all of the Users from the previous day
     * @param users List of Users that have been created from the past day.
     */
    protected void update(ArrayList<User> users) {
        try {
            List<String> allLines = new ArrayList<>(Files.readAllLines(path, StandardCharsets.UTF_8));

            for (User user : users) {

                boolean found = false;

                // Search for User
                for (int i = 0; i < allLines.size(); i++) {
                    if (allLines.get(i).equalsIgnoreCase(user.getUsername())) {
                        allLines.set(i + 1, user.getType());
                        allLines.set(i + 2, Double.toString(user.getBalance()));
                        found = true;
                        break;
                    }
                }

                // User didn't exist
                if (!(found)) {
                    allLines.add(user.getUsername());
                    allLines.add(user.getType());
                    allLines.add(Double.toString(user.getBalance()));
                    allLines.add("\n");
                }
            }

            Files.write(path, allLines, StandardCharsets.UTF_8);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the list of Users that were created before the current day.
     * @return list of Users that were created before the current day.
     */
    protected ArrayList<User> getUsers() {
        try {
            ArrayList<User> users = new ArrayList<>();
            List<String> allLines = new ArrayList<>(Files.readAllLines(path, StandardCharsets.UTF_8));
            String accountRegex = "^([aA]{2})$|^([bBsS]{2})$|^([sS]{2})$|^([fsFS]{2})$";
            Pattern p = Pattern.compile(accountRegex);

            for (int i = 1; i < allLines.size() - 1; i++) {
                Matcher m = p.matcher(allLines.get(i));
                if (m.matches()) {
                    switch (allLines.get(i)) {
                        case "AA":
                            users.add(new Admin(allLines.get(i - 1), Double.parseDouble(allLines.get(i + 1))));
                            break;
                        case "FS":
                            users.add(new fullStandard(allLines.get(i - 1), Double.parseDouble(allLines.get(i + 1))));
                            break;
                        case "BS":
                            users.add(new Buyer(allLines.get(i - 1), Double.parseDouble(allLines.get(i + 1))));
                            break;
                        default:
                            users.add(new Seller(allLines.get(i - 1), Double.parseDouble(allLines.get(i + 1))));
                            break;
                    }
                }
            }

            return users;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}