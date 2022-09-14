import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Class to test Regex patterns.
 */
class TestRegex {

    public static Pattern p1, p2, p3, p4, p5;

    @BeforeAll
   public static void setUp() {

       //Regular expressions
       String regex1 = "^(00|01|02|06|07|10) ([\\w| ]{15}) (AA|BS|FS|SS) ([\\d|\\.]{9})$";
       String regex2 = "^(05) ([\\w| ]{15}) ([\\w| ]{15}) ([\\d|\\.]{9})$";
       String regex3 = "^(03) ([\\w| ]{19}) ([\\w| ]{15}) ([\\d|\\.]{5}) ([\\d|\\.]{6})$";
       String regex4 = "^(04) ([\\w| ]{19}) ([\\w| ]{15}) ([\\w| ]{15})";
       String regex5 = "^(08|09) ([\\w| ]{19}) ([\\w| ]{14}) ([\\w| ]{13})$";
       //Creating a pattern objects
       p1 = Pattern.compile(regex1);
       p2 = Pattern.compile(regex2);
       p3 = Pattern.compile(regex3);
       p4 = Pattern.compile(regex4);
       p5 = Pattern.compile(regex5);

   }

   @Test
    public void login() {
        String input = "00 Michael M       AA 000000000";
        Matcher m1 = p1.matcher(input);
       Assertions.assertTrue(m1.matches());
       Assertions.assertEquals("00", m1.group(1));
       Assertions.assertEquals("Michael M", m1.group(2).stripTrailing());
       Assertions.assertEquals("AA", m1.group(3));
       Assertions.assertEquals("000000000", m1.group(4));
   }

   @Test
    public void create() {
   }

   @Test
    public void delete() {
   }

   @Test
    public void sell() {
   }

   @Test
    public void buy() {
   }

   @Test
    public void refund() {
   }

   @Test
    public void addCredit() {
   }

   @Test
    public void removeGame() {
   }

   @Test
    public void gift() {
   }

   @Test
    public void logout() {
   }
}
