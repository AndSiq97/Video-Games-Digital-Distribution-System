import java.util.ArrayList;

/**
 * A class representing a user who can both purchase and list products for sale in the application.
 */
public class fullStandard extends User {

    private static final String type = "FS";

    /**
     * Constructor of Full Standard User with a username and balance.
     * @param username
     * @param balance
     */
    public fullStandard(String username, double balance) {
        super(username, balance);
    }

    /**
     * Get the type of User as Full Standard User.
     * @return the type Full Standard User
     */
    @Override
    public String getType() {
        return type;
    }
}