import java.util.ArrayList;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * An class to represent a general User of the application. A User can be a Buyer, Seller,
 * fullStandard, or Admin.
 */
public abstract class User {

    private final String username;
    private double balance;
    private ArrayList<Game> inventory;
    private ArrayList<Game> forSale;
    private double creditAddedToday = 0.00;
    private static final double MAX_DAILY = 1000.00;
    private static final double MAX_BALANCE = 999999.99;

    /**
     * Constructor of User with a username and balance.
     * @param username
     * @param balance
     */
    public User(String username, double balance) {
        this.inventory = new ArrayList<>();
        this.forSale = new ArrayList<>();
        this.username = username;
        this.balance = balance;
    }

    /**
     * Get the username of this User.
     * @return the username
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Get the balance of this User.
     * @return the balance
     */
    public double getBalance() {
        return this.balance;
    }

    /**
     * Get the amount of credit added today.
     * @return the credit added today
     */
    public double getCreditAddedToday() {
        return creditAddedToday;
    }

    /**
     * Abstract method of get type of User.
     */
    abstract String getType();

    /**
     * Add an amount to a User's balance.
     * @param amount the amount added
     */
    public void addAmount(double amount) {
        this.balance += amount;
    }

    /**
     * Amount of credit added to this User's balance.
     * @param amount the amount added
     */
    public void addCredit(double amount) {
        double temp = creditAddedToday + amount;
        BigDecimal result = new BigDecimal(MAX_BALANCE - balance).setScale(2, RoundingMode.HALF_UP);
        double added = result.doubleValue();
        if (temp <= MAX_DAILY) {
            if (balance + amount > MAX_BALANCE) {
                System.out.println("ERROR: constraint error from addCredit transaction, balance capped");
                System.out.println(this.username + " added "+ (added));
                creditAddedToday = added;
                this.balance = MAX_BALANCE;
            }
            else {
                System.out.println(this.username + " added "+ amount);
                this.balance += amount;
                this.creditAddedToday = temp;
            }
        }
        else {
            System.out.println("Error: constraint error from addCredit transaction, daily limit reached");
            }
    }

    /**
     * Remove specified game from this User's inventory of games.
     * @param gameRemove the game to remove
     */
    public void removeGame(Game gameRemove) {
        for (Game game : inventory) {
            if (game.getTitle().equals(gameRemove.getTitle())) {
                inventory.remove(game);
                gameRemove.removeOwner(this.username);
                break;
            }
        }
        for (Game game : forSale) {
            if (game.getTitle().equals(gameRemove.getTitle())) {
                forSale.remove(game);
                gameRemove.removeOwner(this.username);
                break;
            }
        }
    }

    /**
     * Remove all games from this User's inventory of games.
     */
    public void removeAllGames() {
        for (Game game: this.inventory) {
            game.removeOwner(this.username);
        }
        for (Game forSale: this.forSale) {
            forSale.removeOwner(this.username);
        }
        this.inventory.clear();
        this.forSale.clear();
    }

    /**
     * Add a game to thus User's inventory of games.
     * @param game the game to add
     */
    public void addGame(Game game) {
        this.inventory.add(game);
        game.addOwner(this.username);
    }

    /**
     * Add the given game for sale.
     * @param game the game to add
     */
    public void addForSale(Game game) {
        this.forSale.add(game);
        game.addOwner(this.username);
    }
}
