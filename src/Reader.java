import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class reads the daily text files of transactions.
 */
public class Reader {

    /**
     * Main method of the Reader class.
     * @param args
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        BufferedReader in = new BufferedReader(new FileReader("daily.txt"));
        Scanner s = new Scanner(in);

        String regex1 = "^(00|01|02|06|07|10) (.{15}) (AA|BS|FS|SS) ([\\d|\\.]{9})$";
        String regex2 = "^(05) (.{15}) (.{15}) ([\\d|\\.]{9})$";
        String regex3 = "^(03) (.{25}) (.{15}) ([\\d|\\.]{5}) ([\\d|\\.]{6})$";
        String regex4 = "^(04) (.{25}) (.{15}) (.{15})$";
        String regex5 = "^(08|09) (.{25}) (.{15}) (.{15})?$";

        Pattern p1 = Pattern.compile(regex1);
        Pattern p2 = Pattern.compile(regex2);
        Pattern p3 = Pattern.compile(regex3);
        Pattern p4 = Pattern.compile(regex4);
        Pattern p5 = Pattern.compile(regex5);

        Pattern[] patterns = {p1, p2, p3, p4, p5};

        Transactions transaction = new Transactions(); // not entirely sure about this, but if we want to make use ot a Transaction class,
        // I guess we will make one in all cases.

        // Inject past information from databases to Transactions
        UserDatabase users = new UserDatabase();
        GameDatabase games = new GameDatabase();
        ForSaleDatabase forSales = new ForSaleDatabase();
        AuctionDatabase auction = new AuctionDatabase();

        transaction.setUsers(users.getUsers());
        transaction.setGames(games.getGames());
        transaction.setGames_for_sale(forSales.getGames());
        transaction.setAuctionSale(auction.getStatus());


        while (s.hasNextLine()) {
            String line = s.nextLine();
            System.out.println(line);
            Matcher temp = null;

            for (Pattern p : patterns) {
                Matcher m = p.matcher(line);
                if (m.matches()) {
                    temp = m;
                    break;
                }
            }

            if (temp != null) {

                 String code = temp.group(1);

                switch(code) {
                case "00": // login
                    transaction.login(temp.group(2), temp.group(3), Double.parseDouble(temp.group(4)));
                    break;
                case "01": // create
                    transaction.create(temp.group(2), temp.group(3), Double.parseDouble(temp.group(4)));
                    break;
                case "02": // delete
                    transaction.delete(temp.group(2));
                    break;
                case "06": // add credit
                    transaction.addCredit(temp.group(2), Double.parseDouble(temp.group(4)));
                    break;
                case "07": // auction sale
                    transaction.auctionSale();
                    break;
                case "10": // logout
                    transaction.logout(temp.group(2), temp.group(3), Double.parseDouble(temp.group(4)));
                    break;
                case "03": // sell
                    transaction.sell(temp.group(2), temp.group(3), Double.parseDouble(temp.group(4)),
                            Double.parseDouble(temp.group(5)));
                    break;
                case "04": // buy
                    transaction.buy(temp.group(2), temp.group(3), temp.group(4));
                    break;
                case "05": // refund
                    transaction.refund(temp.group(2), temp.group(3), Double.parseDouble(temp.group(4)));
                    break;
                case "08": // remove game
                    transaction.removeGame(temp.group(2), temp.group(3), temp.group(4));
                    break;
                case "09": // gift
                    transaction.gift(temp.group(2), temp.group(3), temp.group(4));
                    break;
                }
            }
            else { System.out.println("ERROR: Fatal error has occurred. The line in daily.txt: " + line +
                    "does not follow any of the enforced formats"); }
        }

        // Finished processing transactions, write to databases
        users.update(transaction.getUsers());
        games.update(transaction.getGames());
        forSales.update(transaction.getGames_for_sale());
        auction.update(transaction.getAuction());
    }
}
