import java.lang.reflect.Array;
import java.util.ArrayList;

public class Transactions {

    private ArrayList<User> users;
    private ArrayList<Game> games_for_sale;
    private ArrayList<Game> games;
    private String loginUsername = "";
    private boolean auctionSale = false;

    /**
     * Constructor for Transactions.
     */
    public Transactions() {
        users = new ArrayList<User>();
        games_for_sale = new ArrayList<Game>();
        games = new ArrayList<Game>();
    }

    /**
     * Get the list of Users.
     * @return the list of Users
     */
    public ArrayList<User> getUsers() {     // Used to update the user database
        return this.users;
    }

    /**
     * Get the username of the User logged in.
     * @return the login username
     */
    public String getLoginUsername() {
        return loginUsername;
    }

    /**
     * User login to the system.
     * @param name the name of the User
     * @param type the type of User
     * @param balance the balance of the User
     */
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

    /**
     * User logout of the system.
     * @param name the name of the User
     * @param type the type of User
     * @param balance the balance of the User
     */
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
                    System.out.println(name + " has been logged out");
                    loginUsername = "";
                }
            }
        } else {
            System.out.println("ERROR: constraint error from logout transaction, whitespace only username input.");
        }
    }

    /**
     * Create a User account.
     * @param name the name of the User
     * @param type the type of User
     * @param credit the amount of credit User has
     */
    public void create(String name, String type, double credit) {

        if (loggedInAs(loginUsername, "AA")) {  // logged in as is an existing admin.
            double maximum = 999999.99;
            double minimum = 0;
            String username = name.trim();
            if (!username.equals("")) { // chosen name is not white space
                if (this.unique(username) && (minimum <= credit) && (credit <= maximum)) {
                    // check that the username entered is unique and valid
                    // check that the credit value is between 0 and 999,999.99
                    User newUser;
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
            }
            else {
                System.out.println("ERROR: Constraint Error - chosen name is whitespace.");
            }
        }
        else {
            System.out.println("ERROR: Constraint Error - not logged in as an admin");
        }
    }

    /**
     * Remove a User from system.
     * @param username the username to be removed
     */
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

    /**
     * Add a game to the User's inventory and to the list of games for sale.
     * @param title the title of the game
     * @param seller the seller's username
     * @param discount the discount of the game
     * @param price the price of the game
     */
    public void sell(String title, String seller, double discount, double price) {

        String gameName = title.trim();
        String sellerName = seller.trim();

        if (!gameName.equals("")) { // not a white-space title
            Game newRelease = new Game(gameName, price, discount);
            if (loggedInAs(this.loginUsername, "AA")) {
                if (loggedInAs(sellerName, "SS")) {
                    Seller user = (Seller) this.getUser(sellerName);
                    sellNewGame(newRelease, user);
                } else if (loggedInAs(sellerName, "FS")) {
                    fullStandard user = (fullStandard) this.getUser(sellerName);
                    sellNewGame(newRelease, user);
                } else if (loggedInAs(sellerName, "AA")) {
                    Admin user = (Admin) this.getUser(sellerName);
                    sellNewGame(newRelease, user);
                } else {
                    System.out.println("ERROR: Constraint Error - Logged in as admin, but seller name is " +
                            "not a valid seller, admin, or full-standard account.");
                }
            } else if (loggedInAs(this.loginUsername, "FS")) {
                if (this.loginUsername.equals(sellerName)) {
                    fullStandard user = (fullStandard) this.getUser(this.loginUsername);
                    sellNewGame(newRelease, user);
                } else {
                    System.out.println("ERROR: Fatal Error - Currently logged in as full-standard " +
                            "but not as the seller.");
                }
            } else if (loggedInAs(this.loginUsername, "SS")) {
                if (this.loginUsername.equals(sellerName)) {
                    Seller user = (Seller) this.getUser(this.loginUsername);
                    sellNewGame(newRelease, user);
                } else {
                    System.out.println("ERROR: Fatal Error - Logged in as seller account, but trying to " +
                            "register seller as someone else.");
                }
            }
            else {
                System.out.println("ERROR: Constraint Error - Not logged in as admin, full-standard, or seller-standard.");
            }
        }
        else {
            System.out.println("ERROR: Constraint Error from sell transaction, white space only game title");
        }
    }

    /**
     * Purchase a game from another User.
     * @param game the name of the game
     * @param seller the seller's username
     * @param buyer the buyer's username
     */
    public void buy(String game, String seller, String buyer) {
        // User is logged in as any account other than SS, and the buyer is the logged in user
        String sellerName = seller.trim();
        String buyerName = buyer.trim();
        String gameName = game.trim();
        User sellerUser = getUser(sellerName);
        User buyerUser = getUser(buyerName);
        User loginUser = getUser(loginUsername);
        if (!loggedIn()) {
            System.out.println("ERROR: constraint error, no user is logged in");
        }
        else if (getUser(loginUsername).getType().equals("SS")) {
            System.out.println("ERROR: constraint error from buy transaction, " +
                    "sell-standard user performing buy transaction");
        }
        else if (getGame(gameName) == null) {
            System.out.println("ERROR: constraint error from buy transaction, game doesn't exist");
        }
        else {
            if (loginUser.getType().equals("AA")) {
                if (sellerUser != null && buyerUser != null && isSellingGame(gameName, sellerName) &&
                        !hasGame(gameName, buyerName)) {
                    for (Game listing: games_for_sale) {
                        if (listing.owners.contains(sellerName)) {
                            if (buyerUser.getBalance() >= listing.getPrice()) {
                                buyerUser.addGame(listing);
                                sellerUser.addAmount(listing.getPrice());
                                buyerUser.addAmount(-1 * listing.getPrice());
                                System.out.println(buyerName + " has bought " + gameName);
                            }
                            else {
                                System.out.println("ERROR: constraint error, buyer has insufficient funds");
                            }
                        }
                    }
                }
                else {
                    System.out.println("ERROR: constraint error from buy transaction");
                }
            }
            else {
                if (sellerUser != null && buyerUser != null && isSellingGame(gameName, sellerName) &&
                        !hasGame(gameName, buyerName) && loginUsername.equals(buyerName)) {
                    for (Game listing: games_for_sale) {
                        if (listing.owners.contains(sellerName)) {
                            if (buyerUser.getBalance() >= listing.getPrice()) {
                                buyerUser.addGame(listing);
                                sellerUser.addAmount(listing.getPrice());
                                buyerUser.addAmount(-1 * listing.getPrice());
                                System.out.println(buyerName + " has bought " + gameName);
                            }
                            else {
                                System.out.println("ERROR: constraint error, buyer has insufficient funds");
                            }
                        }
                    }
                }
                else {
                    System.out.println("ERROR: constraint error from buy transaction");
                }
            }
        }
    }

    /**
     * Amount of credit refunded.
     * @param buyer the buyer's username
     * @param seller the seller's username
     * @param credit the amount of credit
     */
    public void refund(String buyer, String seller, double credit) {
        if (loggedInAs(this.loginUsername, "AA") &&  this.validAccount(buyer) && this.validAccount(seller)){
            // check that we are logged in as admin.
            String buyerType = this.getUser(buyer).getType(); // get the type of the buyer account.
            String sellerType = this.getUser(seller).getType(); // get the type of the seller account.
            double buyerBalance = this.getUser(buyer).getBalance(); // current balance of the buyer.
            double sellerBalance = this.getUser(seller).getBalance(); // current balance of seller.
            if ((buyerBalance + credit <= 999999.99) && (sellerBalance - credit >= 0)) {
                // check that the balances are valid after the refund.
            switch (buyerType){

                case "AA": // if the buyer type is admin.
                    switch (sellerType) {
                        case "SS": { // if the seller type is standard-seller.
                            Admin buyerUser = (Admin) this.getUser(buyer);
                            Seller sellerUser = (Seller) this.getUser(seller);
                            buyerUser.addAmount(credit);
                            sellerUser.addAmount((-1) * credit);

                            break;
                        }
                        case "FS": {  // if the seller type is full-standard.
                            Admin buyerUser = (Admin) this.getUser(buyer);
                            fullStandard sellerUser = (fullStandard) this.getUser(seller);
                            buyerUser.addAmount(credit);
                            sellerUser.addAmount((-1) * credit);

                            break;
                        }
                        case "AA": { // if the seller type is admin.
                            Admin buyerUser = (Admin) this.getUser(buyer);
                            Admin sellerUser = (Admin) this.getUser(seller);
                            buyerUser.addAmount(credit);
                            sellerUser.addAmount((-1) * credit);
                            break;
                        }
                        default:
                            System.out.println("Constraint Error: Buyer is an admin, but seller type is invalid.");

                    }
                    break;
                case "FS": // if the buyer type is full-standard
                    switch (sellerType) {
                        case "SS": { // if the seller type is seller-standard.
                            fullStandard buyerUser = (fullStandard) this.getUser(buyer);
                            Seller sellerUser = (Seller) this.getUser(seller);
                            buyerUser.addAmount(credit);
                            sellerUser.addAmount((-1) * credit);

                            break;
                        }
                        case "FS": { // if the seller type is full-standard.
                            fullStandard buyerUser = (fullStandard) this.getUser(buyer);
                            fullStandard sellerUser = (fullStandard) this.getUser(seller);
                            buyerUser.addAmount(credit);
                            sellerUser.addAmount((-1) * credit);
                            break;
                        }
                        case "AA": { // if the seller type is admin.
                            fullStandard buyerUser = (fullStandard) this.getUser(buyer);
                            Admin sellerUser = (Admin) this.getUser(seller);
                            buyerUser.addAmount(credit);
                            sellerUser.addAmount((-1) * credit);

                            break;
                        }
                        default:
                            System.out.println("Constraint Error: Buyer is a valid full-standard account, but" +
                                    "seller type is invalid.");

                    }

                    break;
                case "BS": // if the buyer type is buy-standard.
                    switch (sellerType) {
                        case "SS": { // if the seller type is sell-standard.
                            Buyer buyerUser = (Buyer) this.getUser(buyer);
                            Seller sellerUser = (Seller) this.getUser(seller);
                            buyerUser.addAmount(credit);
                            sellerUser.addAmount((-1) * credit);

                            break;
                        }
                        case "FS": { // if the seller type is full-standard.
                            Buyer buyerUser = (Buyer) this.getUser(buyer);
                            fullStandard sellerUser = (fullStandard) this.getUser(seller);

                            buyerUser.addAmount(credit);
                            sellerUser.addAmount((-1) * credit);
                            break;
                        }
                        case "AA": { // if the seller type is admin.
                            Buyer buyerUser = (Buyer) this.getUser(buyer);
                            Admin sellerUser = (Admin) this.getUser(seller);

                            buyerUser.addAmount(credit);
                            sellerUser.addAmount((-1) * credit);
                            break;
                        }
                        default:
                            System.out.println("Constraint Error: Buyer is a valid buy-standard account but " +
                                    "seller type is invalid.");
                    }
                    break;
                default:
                    System.out.println("Constraint Error: the buyer account is not one that can buy games."
                    );
            }}

            System.out.println("Fatal Error: refund causes buyer account to exceed maximum value or seller account" +
                    "to go below 0.");

        } System.out.println("Fatal Error: Not logged in as a valid admin account or the buyer or seller account is" +
                "not a valid account.");
    }


    /**
     * Check if the account with given username is a valid account.
     * @param userName the username to be checked
     * @return true if valid account with given username, false otherwise
     */
    private boolean validAccount(String userName){ // return whether the account with userName is a valid account.
        for (User user: this.users){
            if (user.getUsername().equals(userName)){
                return true;
            }
        }
        return false;

    } // return whether account with this username is a valid account.

    /**
     * Add credit into the system with given username.
     * @param username the username
     * @param credit the amount of credit added
     */
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

    /**
     * Incorporate discount to all games for sale.
     */
    public void auctionSale() {

        if (this.loggedInAs(this.loginUsername, "AA") && (!this.auctionSale)){
            // logged in as admin, and auction sale is false. (Auction is not ongoing right now)
            for (Game game: this.games_for_sale){
                game.auctionSale(true);
            }
            this.auctionSale = true;

        } else if ((this.loggedInAs(this.loginUsername, "AA") && (this.auctionSale))){
            // logged in as admin, and auction sale is true. (Auction is already happening.)
            for (Game game: this.games_for_sale){
                game.auctionSale(false);
            }
            this.auctionSale = false;
        }
        else {
            System.out.println("Constraint Error: not logged in as an admin.");
        }

    }

    /**
     * Remove game from User's list of games transferred to another User.
     * @param title the title of the game
     * @param owner the owner's username
     * @param receiver the receiver's username
     */
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

    /**
     * Give a User a game from another User.
     * @param title the title of the game
     * @param owner the owner's username
     * @param receiver the receiver's username
     */
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

    /**
     * Check if User is logged in.
     * @return true of false
     */
    private boolean loggedIn() { // to check that we are logged in at all. This function might be a bit un-neccassary
        // since I made loggedInAs, but leave it here for now I guess.
        return !(this.loginUsername.equals(""));
    }

    /**
     * Check if username account exist and has account type in system.
     * @param userName the User's username
     * @param type the User's type
     * @return true or false
     */
    private boolean loggedInAs(String userName, String type) { // to check if account with userName exists and has correct
        // account type in the system.
        for (User user : this.users) {
            if (user.getUsername().equals(userName)) {
                return user.getType().equals(type);
            }
        }
        return false;
    }

    /**
     * Get User with given username.
     * @param userName
     * @return User
     */
    public User getUser(String userName) { // Get the user with the userName.

        for (User user : this.users) {
            if (user.getUsername().equals(userName)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Get game with given title.
     * @param title
     * @return Game
     */
    public Game getGame(String title) { // Get the game with the title.

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

    /**
     * Check if User with given username has a game with given title.
     * @param title the game's title
     * @param username the User's username
     * @return true or false
     */
    public boolean hasGame(String title, String username) {
        for (Game game : this.games) {
            if (game.getTitle().equals(title)) { // The user has this game for sale
                return game.getOwners().contains(username);
            }
        }
        for (Game game : this.games_for_sale) {
            if (game.getTitle().equals(title)) { // The user has this game in their inventory
                return game.getOwners().contains(username);
            }
        }
        return false;
    }

    /**
     * Check whether User is selling the specified game.
     * @param title the title of the game
     * @param username the User's username
     * @return true if selling game, false otherwise
     */
    public boolean isSellingGame(String title, String username) {
        for (Game game : this.games_for_sale) {
            if (game.getTitle().equals(title)) { // The user has this game in their inventory
                return game.getOwners().contains(username);
            }
        }
        return false;
    }

    /**
     * Sell new game.
     * @param newGame
     * @param seller
     */
    public void sellNewGame(Game newGame, User seller) {
        if (!hasGame(newGame.title, seller.getUsername())) { // seller does not already sell this game
            seller.addForSale(newGame);
            games_for_sale.add(newGame);
            System.out.println(seller.getUsername() + " is selling " + newGame.getTitle());
        }
        else {
            System.out.println("ERROR: constraint error, user is already selling this game");
        }
    }

    /**
     * Set auction sale status.
     * @param auctionStatus
     */
    public void setAuctionSale(boolean auctionStatus) {
        this.auctionSale = auctionStatus;
    }

    /**
     * Set Users.
     * @param users list of Users
     */
    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    /**
     * Set games.
     * @param games list of games
     */
    public void setGames(ArrayList<Game> games) {
        this.games = games;
    }

    /**
     * Set games for sale.
     * @param forSale list of games
     */
    public void setGames_for_sale(ArrayList<Game> forSale) {
        this.games_for_sale = forSale;
    }

    /**
     * Get list of games.
     * @return list of games
     */
    public ArrayList<Game> getGames() {
        return this.games;
    }

    /**
     * Get list of games for sale.
     * @return list of games for sale
     */
    public ArrayList<Game> getGames_for_sale() {
        return this.games_for_sale;
    }

    /**
     * Return whether auction sale is happening or not.
     * @return boolean of auction sale
     */
    public boolean getAuction() {
        return this.auctionSale;
    }
}
