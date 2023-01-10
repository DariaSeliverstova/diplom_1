package netology.data;

import com.github.javafaker.Faker;
import com.github.javafaker.service.RandomService;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.DriverManager;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataHelper {

    @Value
    public static class Card {
        String number;
        String month;
        String year;
        String holder;
        String cvc;
    }

    public static String generateHolder(String locale) {
        Faker faker = new Faker(new Locale(locale));
        return faker.name().firstName() + " " + faker.name().lastName();
    }

    final private static String approvedCardNumber = "4444444444444441";
    final private static String declinedCardNumber = "4444444444444442";

    public static Card setCard(String setNumber, String setMonth, String setYear, String setHolder, String setCvc) {
        return new Card(setNumber, setMonth, setYear, setHolder, setCvc);
    }

    public static Card setValidCard(String month, String year, String holder, String cvc) {
        return setCard(approvedCardNumber, month, year, holder, cvc);
    }

    public static Card setInvalidCard(String month, String year, String holder, String cvc) {
        return setCard(declinedCardNumber, month, year, holder, cvc);
    }

    public static String generateYear(int shift) {
        String date = LocalDate.now().plusYears(shift).format(DateTimeFormatter.ofPattern("yy"));
        return date;
    }


}

