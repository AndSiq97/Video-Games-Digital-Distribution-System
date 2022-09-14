import java.lang.reflect.Array;
import java.util.ArrayList;

public class Transactions {

    private final ArrayList<User> users;
    private ArrayList<Game> games_for_sale;
    private ArrayList<Game> games;
    private String loginUsername = "";
    private boolean auctionSale = false;

    public Transactions() {
        users = new ArrayList<User>();
        games_for_sale = new ArrayList<Game>();
        games = new ArrayList<Game>();
    }

    public ArrayList<User> getUsers() {     // Used to update the user database
        return this.users;
    }

    public String getLoginUsername() {
        return loginUsername;
    }

    public void addUsers(String fileLine) {
        String[] arrOfStr = fileLine.split(" - ");
        User user;
        switch (arrOfStr[0]) {
            case "AA":
                user = new Admin(arrOfStr[1], Double.parseDouble(arrOfStr[2]));
                // this.admins.add((Admin) user);
                break;
            case "FS":
                user = new fullStandard(arrOfStr[1], Double.parseDouble(arrOfStr[2]));
                // this.fullStandards
                break;
            case "BS":
                user = new Buyer(arrOfStr[1], Double.parseDouble(arrOfStr[2]));
                break;
            default:
                user = new Seller(arrOfStr[1], Double.parseDouble(arrOfStr[2]));
                break;
        }
        this.users.add(user);
    }

    public void login(String name, String type, double balance) {

        String inputName = name.trim();

        if (!inputName.equals("")) {
            if (loginUsername.equals(inputName)) {
                System.out.println(name + " is already logged in");
            }
            if (!loginUsername.equals("")) {
                System.out.println("ERROR: constraint error from login transaction, A different user is still " +
                        "logged in");
            } else {
                User selectedUser;
                selectedUser = getUser(inputName);
                if (selectedUser != null) {
                    if (!selectedUser.getType().equals(type)) {
                        System.out.println("ERROR: constraint error has occurred. " + type + " was used for the " +
                                "login transaction instead of " + selectedUser.getType());
                    }
                    if (selectedUser.getBalance() != balance) {
                        System.out.println("ERROR: constraint error has occurred. " + balance + " was used for the " +
                                "login transaction instead of " + selectedUser.getBalance());
                    }
                    System.out.println(name + " has logged in");
                    loginUsername = inputName;
                } else {
                    System.out.println("ERROR: constraint error from login transaction, name does not exist");
                }
            }
        } else {
            System.out.println("ERROR: constraint error from login transaction, whitespace only username input.");
        }
    }

    public void logout(String name, String type, double balance) {

        String inputName = name.trim();

        if (!inputName.equals("")) {
            if (!loginUsername.equals(inputName)) {
                System.out.println("ERROR: constraint error from logout transaction, " + inputName +
                        " was used to logout " + loginUsername);
            }
            if (loginUsername.equals("")) {
                System.out.println("ERROR: constraint error from logout transaction, no user is logged in");
            } else {
                User selectedUser;
                selectedUser = getUser(inputName);
                if (selectedUser != null) {
                    if (!selectedUser.getType().equals(type)) {
                        System.out.println("ERROR: constraint error has occurred, " + type + " was used for the " +
                                "logout transaction instead of " + selectedUser.getType());
                    }
                    if (selectedUser.getBalance() != balance) {
                        System.out.println("ERROR: constraint error has occurred. " + balance + " was used for " +
                                "the logout transaction instead of " + selectedUser.getBalance());
                    }
                    System.out.println(name + "has been logged out");
                    loginUsername = "";
                }
            }
        } else {
            System.out.println("ERROR: constraint error from logout transaction, whitespace only username input.");
        }
    }

    public void create(String username, String type, double credit) {
        boolean error = false;
        if (this.loggedIn()) { // check to see that we are actually logged in.
            for (User user : users) {
                if (user.getUsername().equals(loginUsername) && user.getType().equals("AA")) { // check that the user
                    // we currently logged in as is an existing admin.
                    double maximum = 999999.99;
                    double minimum = 0;
                    if (this.unique(username) && (minimum <= credit) && (credit <= maximum)) {
                        // check that the username entered is unique and that the credit value is between 0 and 999,999.99
                        User newUser = null;
                        switch (type) {
                            case "AA":
                                newUser = new Admin(username, credit);
                                break;
                            case "FS":
                                newUser = new fullStandard(username, credit);
                                break;
                            case "BS":
                                newUser = new Buyer(username, credit);
                                break;
                            default:
                                newUser = new Seller(username, credit);
                                break;
                        }
                        this.users.add(newUser);
                        System.out.println(username + " of type " + type + " created.");

                    } else {
                        System.out.println("ERROR: Constraint Error - User name not unique or invalid credit amount.");
                    }

                } else {
                    System.out.println("ERROR: Fatal Error - not currently logged in as an admin user.");
                }
            }
        } else {
            System.out.println("ERROR: Fatal Error - Not currently logged in!");
        }
    }


    public void delete(String username) {

        String inputName = username.trim();

        User loginUser = getUser(loginUsername);
        User selectedUser = getUser(inputName);

        if (inputName.equals("")) {
            System.out.println("ERROR: constraint error from delete transaction, whitespace only username input.");
        }
        else if (loginUser == null) {
            System.out.println("ERROR: constraint error from delete transaction, no one is logged in");
        }
        else if (!loginUser.getType().equals("AA")) {
            System.out.println("ERROR: constraint error from delete transaction, non-admin access");
        }
        else {
            if (selectedUser != null) {
                    if (!inputName.equals(loginUsername)) {
                        users.remove(selectedUser);
                        selectedUser.removeAllGames();
                        System.out.println(inputName + " has been deleted");
                    } else {
                        System.out.println("ERROR: constraint error from delete transaction, self delete");
                    }
            }
            else {
                System.out.println("ERROR: constraint error from delete transaction, unknown username");
            }
        }
    }
    public void sell(String gameName, String sellerName, double discount, double price) {

        if (loggedInAs(this.loginUsername, "AA")) { // check that we are logged in as an admin, and that it is a
            // valid admin account in the system.
            if (loggedInAs(sellerName, "SS")) {
                Seller user = (Seller) this.getUser(sellerName);
                user.addGame(gameName, price, discount);
            } else if (loggedInAs(sellerName, "FS")) {
                fullStandard user = (fullStandard) this.getUser(sellerName);
                user.addGame(gameName, price, discount);

            } else {
                System.out.println("ERROR: Constraint Error - Logged in as admin, but seller name is not a valid seller or " +
                        "full-standard account.");
            }
        } else if (loggedInAs(this.loginUsername, "FS")) {
            // check that we are logged in as full-standard and that it is a valid full-standard account in the system.
            if (this.loginUsername.equals(sellerName)) {
                fullStandard user = (fullStandard) this.getUser(this.loginUsername);
                user.addGame(gameName, price, discount);

            } else {
                System.out.println("ERROR: Fatal Error - Currently logged in as full-standard but not as the seller.");
            }


        } else if (loggedInAs(this.loginUsername, "SS")) { // check that we are logged in as standard-sell and that
            // it is a valid standard-sell account in the system.
            if (this.loginUsername.equals(sellerName)) {
                Seller user = (Seller) this.getUser(this.loginUsername);
                user.addGame(gameName, price, discount);

            } else {
                System.out.println("ERROR: Fatal Error - Logged in as seller account, but trying to register seller as someone" +
                        "else.");
            }
        }
        System.out.println("ERROR: Constraint Error - Not logged in as admin, full-standard, or seller-standard.");
    }

    public void buy(String game, String seller, String buyer) {
        // User is logged in as any account other than SS, and the buyer is the logged in user
        if (!this.loggedInAs(this.loginUsername, "SS")
                && this.loggedIn()
                && this.loginUsername.equalsIgnoreCase(buyer)) {

            Seller sellingPerson = (Seller) this.getUser(seller);
            Buyer buyingPerson = (Buyer) this.getUser(this.loginUsername);
             if (sellingPerson.requiredGame(game)) {    // Seller has the game
                 if(sellingPerson.retrieveGame(game).getPrice() <= buyingPerson.getBalance()) {  // User has the funds
                     if(!hasGame(game, this.loginUsername)) {   // All conditions cleared, buy the game
                         double amount = getGame(game).getPrice();
                         buyingPerson.addCredit(-amount);
                         sellingPerson.addCredit(amount);

                         sellingPerson.removeGame(getGame(game));       // Game is no longer for sale
                         buyingPerson.addGame(getGame(game));

                     } else {
                         System.out.println("ERROR: Constraint - Cannot purchase a game the buyer already owns.");
                     }

                 } else {
                     System.out.println("ERROR: Constraint - Buyer has insufficient funds for the transaction.");
                 }

            } else {
                 System.out.println("ERROR: Constraint - Seller does not own the game being sold.");
             }

        } else {
            System.out.println("ERROR: Constraint - Not logged into an account that can purchase a game.");
        }
    }

    public void refund(String buyer, String seller, double credit) {
        if (loggedInAs(this.loginUsername, "AA") && this.loggedIn()) {
            Seller sellingPerson = (Seller) this.getUser(seller);
            sellingPerson.addCredit((-1) * credit);
            Buyer buyingPerson = (Buyer) this.getUser(buyer);
            buyingPerson.addCredit(credit);
            System.out.println(seller + " has refunded " + String.valueOf(credit) + " to " + buyer);
        } else {
            System.out.println("ERROR: Fatal Error - not currently logged in as an admin user.");
        }
    }

    public void addCredit(String username, double credit) {
        if (loggedInAs(this.loginUsername, "SS")) { // if we are logged in as standard seller, can only add credit
            // to our own account.
            Seller user = (Seller) this.getUser(this.loginUsername);
            user.addCredit(credit);
        } else if (loggedInAs(this.loginUsername, "BS")) { // logged in as standard buyer, can only add credit to
            // our own account.
            Buyer user = (Buyer) this.getUser(this.loginUsername);
            user.addCredit(credit);
        } else if (loggedInAs(this.loginUsername, "FS")) { // logged in as full-standard, can only add credit to our
            // own account.
            fullStandard user = (fullStandard) this.getUser(this.loginUsername);
            user.addCredit(credit);

        } else if (loggedInAs(this.loginUsername, "AA")) { // we are logged into an admin account.
            switch (this.getUser(username).getType()) {
                case "AA":
                    if (this.loggedInAs(username, "AA")) {
                        Admin user = (Admin) this.getUser(username);
                        user.addCredit(credit);

                    }
                    break;
                case "FS":
                    if (this.loggedInAs(username, "FS")) {
                        fullStandard user = (fullStandard) this.getUser(username);
                        user.addCredit(credit);
                    }
                    break;
                case "BS":
                    if (this.loggedInAs(username, "BS")) {
                        Buyer user = (Buyer) this.getUser(username);
                        user.addCredit(credit);
                    }
                    break;
                case "SS":
                    if (this.loggedInAs(username, "SS")) {
                        Seller user = (Seller) this.getUser(username);
                        user.addCredit(credit);
                    }
                    break;
            }

        } else {
            System.out.println("ERROR: - Fatal Error: not logged into a valid account.");
        }

    }

    public void auctionSale() {
        if (this.loggedInAs(this.loginUsername, "AA")) {
            if (auctionSale == false){
                auctionSale = true;
                for (Game game : games_for_sale) {
                    game.price -= (game.price * game.discount);
            } else {
                    auctionSale = false;
                }
        }
    }

    public void removeGame(String title, String owner, String receiver) {
        String gameName = title.trim();
        String ownerName = owner.trim();
        User loginUser = getUser(loginUsername);
        User ownerUser = getUser(ownerName);
        Game game = getGame(gameName);
        if (receiver.trim().equals("")) {
            System.out.println("ERROR: constraint error from remove game transaction, no user login");
        }
        if (!loggedIn()) {
            System.out.println("ERROR: constraint error from remove game transaction, no user login");
        }
        else if (game == null) {
            System.out.println("ERROR: constraint error from gift transaction, game does not exist");
        }
        else {
            if (loginUser.getType().equals("AA")) {
                if (ownerUser != null) {
                    if (hasGame(gameName, loginUsername)) {
                        loginUser.removeGame(game);
                        System.out.println(loginUser + " has removed the game " + gameName);
                    }
                }
                else {
                    System.out.println("ERROR: constraint error from remove game transaction, invalid game owner");
                }
            }
            else {
                if (hasGame(gameName, loginUsername)) {
                    loginUser.removeGame(game);
                    System.out.println(loginUser + " has removed the game " + gameName);
                }
                else {
                    System.out.println("ERROR: constraint error from remove game transaction, invalid game owner");
                }
            }
        }
    }

    public void gift(String title, String owner, String receiver) {
        String gameName = title.trim();
        String ownerName = owner.trim();
        String receiverName = receiver.trim();
        User loginUser = getUser(loginUsername);
        User receiverUser = getUser(receiverName);
        User ownerUser = getUser(ownerName);
        Game game = getGame(gameName);

        if (!loggedIn()) {
            System.out.println("ERROR: constraint error from gift transaction, no user login");
        }
        else if (game == null) {
            System.out.println("ERROR: constraint error from gift transaction, game does not exist");
        }
        else if (receiverUser == null) {
            System.out.println("ERROR: constraint error from gift transaction, receiver does not exist");
        }
        else if (receiverUser.getType().equals("SS")) {
                System.out.println("ERROR: constraint error from gift transaction, seller cannot receive a gift");
        }
        else if (hasGame(gameName, receiverName)) {
            System.out.println("ERROR: constraint error from gift transaction, receiver already owns the game");
        }
        else {
            if (loginUser.getType().equals("AA")) { // admin user
                if (ownerUser == null) {
                    receiverUser.addGame(game);
                    System.out.println(receiverName + " has been gifted");
                } else {
                    if (hasGame(gameName, ownerName)) {
                        receiverUser.addGame(game);
                        ownerUser.removeGame(game);
                        System.out.println(receiverName + " has been gifted");
                    } else {
                        System.out.println("ERROR: constraint error from gift transaction, " +
                                "game is not owned by sender");
                    }
                }
            } else {
                if (hasGame(gameName, ownerName)) {
                    receiverUser.addGame(game);
                    ownerUser.removeGame(game);
                    System.out.println(receiverName + " has been gifted");
                } else {
                    System.out.println("ERROR: constraint error from gift transaction, game is not owned by sender");
                }
            }
        }
    }

    private boolean unique(String username) { // this method checks to see that the username is unique.
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return false;
            }

        }
        return true;

    }

    private boolean loggedIn() { // to check that we are logged in at all. This function might be a bit un-neccassary
        // since I made loggedInAs, but leave it here for now I guess.
        return !(this.loginUsername.equals(""));
    }

    private boolean loggedInAs(String userName, String type) { // to check if account with userName exists and has correct
        // account type in the system.
        for (User user : this.users) {
            if (user.getUsername().equals(userName)) {
                return user.getType().equals(type);
            }
        }
        return false;
    }

    public User getUser(String userName) { // Get the user with the userName.

        for (User user : this.users) {
            if (user.getUsername().equals(userName)) {
                return user;
            }
        }
        return null;
    }

    public Game getGame(String title) { // Get the user with the userName.

        for (Game game : this.games_for_sale) {
            if (game.getTitle().equals(title)) {
                return game;
            }
        }
        for (Game game : this.games) {
            if (game.getTitle().equals(title)) {
                return game;
            }
        }
        return null;
    }

    public boolean hasGame(String title, String username) {
        for (Game game : this.games) {
            if (game.getTitle().equals(title)) {
                return game.getOwners().contains(username);
            }
        }
        for (Game game : this.games_for_sale) {
            if (game.getTitle().equals(title)) {
                return game.getOwners().contains(username);
            }
        }
        return false;
    }
}
