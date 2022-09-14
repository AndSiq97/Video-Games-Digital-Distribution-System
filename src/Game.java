import java.util.ArrayList;

/**
 * A class representing a game that can be purchased or sold to another User.
 */
public class Game {

    protected final String title;
    protected final ArrayList<String> owners; // users who own this game
    protected double price = 0;
    protected double discount = 0;

    /**
     * Constructor of Game with a title.
     * @param title
     */
    public Game(String title) {
        this.owners = new ArrayList<>();
        this.title = title;
    }

    /**
     * Constructor of Game with a title, price, and discount.
     * @param title
     * @param price
     * @param discount
     */
    public Game(String title, double price, double discount) {
        this(title);
        this.price = price;
        this.discount = discount;
    }

    /**
     * Add the username of owner to this game's list of owners.
     * @param username the username of owner added
     */
    public void addOwner(String username) {
        this.owners.add(username);
    }

    /**
     * Remove the username of owner from this game's list of owners.
     * @param username the username of owner removed
     */
    public void removeOwner(String username) {
        for (String name: this.owners) {
            if (name.equals(username)) {
                this.owners.remove(username);
                break;
            }
        }
    }

    /**
     * Get the list of owners for this games.
     * @return the list of owners
     */
    public ArrayList<String> getOwners() {
        return this.owners;
    }

    /**
     * Get the title of this game.
     * @return the title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Get the price of this game.
     * @return the price
     */
    public double getPrice() {
        return this.price;
    }

    /**
     * Get the discount of this game.
     * @return the discount
     */
    public double getDiscount() {
        return this.discount;
    }

    /**
     * Change the price for auction sale whether auction sale
     * is currently happening or not.
     * @param turnOn the boolean of whether auction sale is happening or not
     */
    public void auctionSale(boolean turnOn){ // change the price for auction-sale.
        if (turnOn && (this.discount <= 90) && (0 <= this.discount)) { // auction-sale is not currently happening
            this.price = (this.price * ((100 - this.discount)/100)); // discount price.

        } else if (!turnOn && (this.discount <= 90) && (0 <= this.discount)){ // auction-sale is currently happening.
            this.price = (this.price/((100 - this.discount)/100)); // return to original price.
        }
        else {
            System.out.println("Fatal Error: the discount for this game exceeds 90 percent.");
        }
    }

}
