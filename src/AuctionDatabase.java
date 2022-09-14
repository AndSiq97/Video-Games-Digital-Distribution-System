import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * A class that reads and writes to the AuctionStatus database.
 */
public class AuctionDatabase {

    static Path path = Paths.get("D:\\Isham's Stuff\\Work\\University\\CSC207 - FINAL\\Assignments\\a2-the-bois\\AuctionStatus.txt");

    /**
     * Updates the Auction Database with the past day's auction status.
     * @param auctionStatus whether the auction sale was active in the past day.
     */
    protected void update(boolean auctionStatus) {
        try {
            ArrayList<String> allLines = new ArrayList<>();

            allLines.add(Boolean.toString(auctionStatus));

            Files.write(path, allLines, StandardCharsets.UTF_8);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Return the status of the auction sale from the past day.
     * @return boolean whether auction was active or not
     */
    protected boolean getStatus() {
        try {
            List<String> allLines = new ArrayList<>(Files.readAllLines(path, StandardCharsets.UTF_8));

            return Boolean.parseBoolean(allLines.get(0));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}