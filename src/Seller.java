import java.util.ArrayList;
/**
 * A class representing a Seller who lists products for sale in the application.
 */
public class Seller extends User {

    private ArrayList<Game> forSale = new ArrayList<>();
    private static final String type = "SS";

    /**
     * Constructor of Seller with a username and balance.
     * @param username
     * @param balance
     */
    public Seller(String username, double balance) {
        super(username, balance);
    }

    /**
     * Remove this game from the games sold by this seller.
     * @param game the game to remove.
     */
    public void removeGame(Game game){
        this.forSale.remove(game);
    }

    /**
     * Check if the given name of the game is unique.
     * @param gameName the name of the game to check
     * @return true if game is unique, false otherwise
     */
    public boolean uniqueGame(String gameName){
        for (Game game: this.forSale){
            if (game.getTitle().equals(gameName)){
                return false;
            }
        }
        return true;

    }

    /**
     * Check to see if the name of the game is being
     * sold by this person.
     * @param game the name of the game to check
     * @return true if the given game is being sold
     * by this person, false otherwise
     */
    public boolean requiredGame(String game){ // check to see if the game with certain name is being sold by this person.
        for (Game games: this.forSale){
            if (games.getTitle().equals(game)){
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieve the given game if present in the list of available games.
     * @param game the name of the game to retrieve
     * @return the game if it is in the list of available games,
     * null otherwise
     */
    public Game retrieveGame(String game){
        for (Game games: this.forSale){
            if (games.getTitle().equals(game)){
                return games;
            }

        }
        return null;
    }

    /**
     * Get the type of User as Seller.
     * @return the type Seller
     */
    @Override
    public String getType() {
        return type;
    }
}