import java.util.ArrayList;

/**
 * A class representing an Admin who can perform privileged transactions.
 */
public class Admin extends User {

    private static final String type = "AA";

    /**
     * Constructor of Admin with a username and balance.
     * @param username
     * @param balance
     */
    public Admin(String username, double balance) {
        super(username, balance);
    }

    /**
     * Get the type of User as Admin.
     * @return the type Admin
     */
    @Override
    public String getType() {
        return type;
    }



}