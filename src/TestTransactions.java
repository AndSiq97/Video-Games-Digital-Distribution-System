import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Class to test Transactions methods.
 */
class TestTransactions {

    Transactions t;


    @BeforeEach
    public void setup() {

        t = new Transactions();

        String u1 = "AA - Mike Mike - 343.22";
        String u2 = "BB - Lux# #Lux - 0";
        String u3 = "FS - FPX LWX - 1000.00";
        t.addUsers(u1);
        t.addUsers(u2);
        t.addUsers(u3);

    }
    @Test
    public void testAddUser() {
        assertEquals(3, t.getUsers().size());
    }

    @Test
    public void testLogin() {
        t.login("Mike Mike", "AA", 343.22);
        assertEquals("Mike Mike", t.getLoginUsername());
    }

    @Test
    public void testLoginBalanceConstraint() {
        t.login("Mike Mike", "AA", 222);
        assertEquals("Mike Mike", t.getLoginUsername());
    }

    @Test
    public void testLoginTypeConstraint() {
        t.login("Mike Mike", "BB", 343.22);
        assertEquals("Mike Mike", t.getLoginUsername());
    }

    @Test
    public void testLoginFakeName() {
        t.login("Fake Mike", "AA", 343.22);
        assertEquals("", t.getLoginUsername());
    }

    @Test
    public void testLoginAlready() {
        t.login("Mike Mike", "AA", 343.22);
        t.login("Lux# #Lux", "BB", 0);
        assertEquals("Mike Mike", t.getLoginUsername());
    }

    @Test
    public void testLoginWhitespaceName() {
        t.login("      ", "AA", 999999);
        assertEquals("", t.getLoginUsername());
    }

    @Test
    public void testCreate() {
        // logged in as admin user
        // username is unique
        // username is at most 15 characters
        // credit is not greater than 999,999
    }

    @Test
    public void testGetUser() {
        assertEquals("Lux# #Lux",t.getUser("Lux# #Lux").getUsername());
        assertNull(t.getUser("Fake Mike"));
    }

    @Test
    public void testDelete() {
        t.login("Mike Mike", "AA", 343.22);
        t.delete("Lux# #Lux");
        assertEquals(2, t.getUsers().size());
        assertNull(t.getUser("Lux# #Lux"));
    }

    @Test
    public void testDeleteSelf() {
        t.login("Mike Mike", "AA", 343.22);
        t.delete("Mike Mike");
        assertEquals(3, t.getUsers().size());
        assertNotNull(t.getUser("Mike Mike"));
        assertEquals("Mike Mike", t.getLoginUsername());
    }

    @Test public void testDeleteNonAdmin() {
        t.login("FPX LWX", "FS", 1000);
        t.delete("Mike Mike");
        assertEquals(3, t.getUsers().size());
        assertNotNull(t.getUser("Mike Mike"));
    }

    @Test
    public void testSell() {
        // not logged in as a standard-buy user
        // the maximum price for an game is 999.99
        // the maximum length of an game name is 25 characters
    }

    @Test
    public void testBuy() {
        // not logged in as a standard-sell user
        // existing game in the seller's inventory that is available for sale
        // game does not already exist in buyer's inventory
        // user has enough funds
        // game is in the users' inventory after the transaction
    }

    @Test
    public void testRefund() {
        // buyer and seller must both be current users
        // seller balance has credit deducted
        // buyer balance has credit added
        // not exceeding max credit of 999,999.99
    }

    @Test
    public void testAddCredit() {
        // In admin mode, the username has to be an existing username in the system.
        // A maximum of $1000.00 can be added to an account in a given day.
        // not exceeding max credit of 999,999.99
        Buyer temp = new Buyer("Temp", 0);
        temp.addCredit(1000000);
        assertEquals(0, temp.getBalance());
        assertEquals(0, temp.getCreditAddedToday());

        Buyer temp2 = new Buyer("Temp2", 3.99);
        temp2.addCredit(3.99);
        assertEquals(7.98, temp2.getBalance());
        assertEquals(3.99, temp2.getCreditAddedToday());
        temp2.addCredit(70);
        assertEquals(77.98, temp2.getBalance());
        assertEquals(73.99, temp2.getCreditAddedToday());

        Seller temp3 = new Seller("Temp3", 2000.00);
        temp3.addCredit(1000.00);
        temp3.addCredit(50.00);
        assertEquals(3000.00, temp3.getBalance());
        assertEquals(1000.00, temp3.getCreditAddedToday());

        Admin temp4 = new Admin ("Temp4", 999990.00);
        temp4.addCredit(10);
        assertEquals(999999.99, temp4.getBalance());
        assertEquals(9.99, temp4.getCreditAddedToday());
    }

    @Test
    public void testAuctionSale() {
        // the maximum sale discount is 90 percent
        // a game that was just put up for sale cannot be purchased until the following day.
    }

    @Test
    public void testRemoveGame() {
        // Admin remove a game must exist in selected users' inventory
        // non-admins removing a game must exist in their own inventory
        // Game has not been purchased today
        // Game has not been put on sale today
    }

    @Test
    public void testGift() {
        // Admin gift
        // non-admin gift
        // if the game is one the owner had purchased, it is removed from their inventory when gifted
        // cannot gift a game that was purchased or put up to sale on the same day
        // cannot gift a game to a user who already owns a game with the same name

    }

    @Test
    public void testLogout() {
        // print msg with the correct username
    }
}
