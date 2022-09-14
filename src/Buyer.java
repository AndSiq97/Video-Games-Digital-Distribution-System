import java.util.ArrayList;

/**
 * A class representing a Buyer who purchases products in the application.
 */
public class Buyer extends User {
    private static final String type = "BS";

    /**
     * Constructor of Buyer with a username and balance.
     * @param username
     * @param balance
     */
    public Buyer(String username, double balance) {
        super(username, balance);
    }

    /**
     * Get the type of User as Buyer.
     * @return the type Buyer
     */
    @Override
    public String getType() {
        return type;
    }
}
