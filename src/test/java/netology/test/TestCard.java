package netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import netology.data.DBHelper;
import netology.data.DataHelper;
import netology.page.PaymentPage;
import netology.page.StartingPage;
import netology.util.ScreenShooterReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.open;

@ExtendWith({ScreenShooterReportPortalExtension.class})
public class TestCard {
    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void shouldStart() {
        DBHelper.clearSUTData();
        open("http://localhost:8080/");
    }

    @AfterEach
    void afterCase() {
        DBHelper.clearSUTData();
    }

    public final PaymentPage paymentPage = new PaymentPage();


    public void paymentApprovedStatus() {
        String statusExpected = "APPROVED";
        String statusActual = DBHelper.getPaymentStatus();
        Assertions.assertEquals(statusExpected, statusActual);
    }

    public void paymentDeclinedStatus() {
        String statusExpected = "DECLINED";
        String statusActual = DBHelper.getPaymentStatus();
        Assertions.assertEquals(statusExpected, statusActual);
    }

    public void paymentAcceptId() {
        long idExpected = 1;
        long idActual = DBHelper.getPaymentId();
        Assertions.assertEquals(idExpected, idActual);
    }

    public void paymentRejectedId() {
        long idExpected = 0;
        long idActual = DBHelper.getPaymentId();
        Assertions.assertEquals(idExpected, idActual);
    }

    public void orderAcceptId() {
        long idExpected = 1;
        long idActual = DBHelper.getOrderId();
        Assertions.assertEquals(idExpected, idActual);
    }

    public void orderRejectedId() {
        long idExpected = 0;
        long idActual = DBHelper.getOrderId();
        Assertions.assertEquals(idExpected, idActual);
    }


    @Test
    void shouldBuyOnValidCard() {
        String date = DataHelper.generateYear(2);
        String month = "10",
                year = date,
                holder = DataHelper.generateHolder("En"),
                cvc = "123";

        var card = DataHelper.setValidCard(month, year, holder, cvc);
        StartingPage.selectBuyForm();


        paymentPage.cleanFields();
        paymentPage.completeCardData(card);
        paymentPage.acceptPay();
        paymentApprovedStatus();
        paymentAcceptId();
        orderAcceptId();
    }

    //2.
    @Test
    void shouldNotBuyOnDeclinedCard() {
        String date = DataHelper.generateYear(2);
        String month = "10",
                year = date,
                holder = DataHelper.generateHolder("En"),
                cvc = "123";

        var card = DataHelper.setInvalidCard(month, year, holder, cvc);
        StartingPage.selectBuyForm();


        paymentPage.cleanFields();
        paymentPage.completeCardData(card);

        paymentPage.rejectedPay();
        paymentDeclinedStatus();
        paymentRejectedId();
        orderRejectedId();

    }

    //3.
    @Test
    void shouldNotBuyOnCardLess16Numbers() {
        String date = DataHelper.generateYear(2);
        String month = "10",
                year = date,
                holder = DataHelper.generateHolder("En"),
                cvc = "123",
                shortCardNumber = "4444 4444 4444 444";

        var card = DataHelper.setCard(shortCardNumber, month, year, holder, cvc);
        StartingPage.selectBuyForm();


        paymentPage.cleanFields();
        paymentPage.completeCardData(card);
        paymentPage.shouldVerifyErrorOfField();
        paymentRejectedId();
        orderRejectedId();
    }


    @Test
    void shouldNotBuyOnZeroCard() {
        String date = DataHelper.generateYear(2);
        String month = "10",
                year = date,
                holder = DataHelper.generateHolder("En"),
                cvc = "123",
                zeroCardNumber = "0000 0000 0000 0000";

        var card = DataHelper.setCard(zeroCardNumber, month, year, holder, cvc);
        StartingPage.selectBuyForm();

        paymentPage.cleanFields();
        paymentPage.completeCardData(card);
        paymentPage.rejectedPay();
        paymentRejectedId();
        orderRejectedId();


    }

    @Test
    void shouldNotBuyOnEmptyNumberOfCard() {
        String date = DataHelper.generateYear(2);
        String month = "10",
                year = date,
                holder = DataHelper.generateHolder("En"),
                cvc = "123",
                emptyCardNumber = "";

        var card = DataHelper.setCard(emptyCardNumber, month, year, holder, cvc);
        StartingPage.selectBuyForm();

        paymentPage.cleanFields();
        paymentPage.completeCardData(card);
        paymentPage.shouldVerifyErrorOfField();
        paymentRejectedId();
        orderRejectedId();


    }



    @Test
    void shouldNotBuyOnNotRegisteredCard() {
        String date = DataHelper.generateYear(2);
        String month = "10",
                year = date,
                holder = DataHelper.generateHolder("En"),
                cvc = "123",
                notRegisteredCardNumber = "4444 4444 4444 4443";

        var card = DataHelper.setCard(notRegisteredCardNumber, month, year, holder, cvc);
        StartingPage.selectBuyForm();

        paymentPage.cleanFields();
        paymentPage.completeCardData(card);
        paymentPage.rejectedPay();

        paymentRejectedId();
        orderRejectedId();


    }


    @Test
    void shouldNotBuyWithZeroMonth() {
        String date = DataHelper.generateYear(2);
        String month = "00",
                year = date,
                holder = DataHelper.generateHolder("En"),
                cvc = "123";


        var card = DataHelper.setValidCard(month, year, holder, cvc);
        StartingPage.selectBuyForm();

        paymentPage.cleanFields();
        paymentPage.completeCardData(card);
        paymentPage.shouldVerifyErrorOfField();

        paymentRejectedId();
        orderRejectedId();


    }


    @Test
    void shouldNotBuyWith13Month() {
        String date = DataHelper.generateYear(2);
        String month = "13",
                year = date,
                holder = DataHelper.generateHolder("En"),
                cvc = "123";


        var card = DataHelper.setValidCard(month, year, holder, cvc);
        StartingPage.selectBuyForm();

        paymentPage.cleanFields();
        paymentPage.completeCardData(card);
        paymentPage.shouldVerifyIncorrectExpiryDate();

        paymentRejectedId();
        orderRejectedId();


    }


    @Test
    void shouldNotBuyWithEmptyMonth() {
        String date = DataHelper.generateYear(2);
        String month = "",
                year = date,
                holder = DataHelper.generateHolder("En"),
                cvc = "123";


        var cardInfo = DataHelper.setValidCard(month, year, holder, cvc);
        StartingPage.selectBuyForm();

        paymentPage.cleanFields();
        paymentPage.completeCardData(cardInfo);
        paymentPage.shouldVerifyErrorOfField();

        paymentRejectedId();
        orderRejectedId();


    }


    @Test
    void shouldNotBuyWith1NumberOfMonth() {
        String date = DataHelper.generateYear(2);
        String month = "1",
                year = date,
                holder = DataHelper.generateHolder("En"),
                cvc = "123";


        var card = DataHelper.setValidCard(month, year, holder, cvc);
        StartingPage.selectBuyForm();

        paymentPage.cleanFields();
        paymentPage.completeCardData(card);
        paymentPage.shouldVerifyErrorOfField();

        paymentRejectedId();
        orderRejectedId();


    }


    @Test
    void shouldNotBuyWithLastYear() {
        String date = DataHelper.generateYear(-1);
        String month = "10",
                year = date,
                holder = DataHelper.generateHolder("En"),
                cvc = "123";


        var card = DataHelper.setValidCard(month, year, holder, cvc);
        StartingPage.selectBuyForm();

        paymentPage.cleanFields();
        paymentPage.completeCardData(card);
        paymentPage.shouldVerifyCardDateExpired();

        paymentRejectedId();
        orderRejectedId();


    }


    @Test
    void shouldNotBuyWithEmptyYear() {

        String month = "10",
                year = "",
                holder = DataHelper.generateHolder("En"),
                cvc = "123";


        var card = DataHelper.setValidCard(month, year, holder, cvc);
        StartingPage.selectBuyForm();

        paymentPage.cleanFields();
        paymentPage.completeCardData(card);
        paymentPage.shouldVerifyErrorOfField();

        paymentRejectedId();
        orderRejectedId();


    }


    @Test
    void shouldNotBuyWithZeroYear() {

        String month = "10",
                year = "00",
                holder = DataHelper.generateHolder("En"),
                cvc = "123";


        var card = DataHelper.setValidCard(month, year, holder, cvc);
        StartingPage.selectBuyForm();

        paymentPage.cleanFields();
        paymentPage.completeCardData(card);
        paymentPage.shouldVerifyCardDateExpired();

        paymentRejectedId();
        orderRejectedId();


    }


    @Test
    void shouldNotBuyWith1Year() {

        String month = "10",
                year = "1",
                holder = DataHelper.generateHolder("En"),
                cvc = "123";


        var card = DataHelper.setValidCard(month, year, holder, cvc);
        StartingPage.selectBuyForm();

        paymentPage.cleanFields();
        paymentPage.completeCardData(card);
        paymentPage.shouldVerifyErrorOfField();

        paymentRejectedId();
        orderRejectedId();


    }


    @Test
    void shouldNotBuyWithCyrillicNameOfHolder() {

        String month = "10",
                year = "24",
                holder = DataHelper.generateHolder("Ru"),
                cvc = "123";


        var card = DataHelper.setValidCard(month, year, holder, cvc);
        StartingPage.selectBuyForm();

        paymentPage.cleanFields();
        paymentPage.completeCardData(card);
        paymentPage.shouldVerifyErrorOfField();

        paymentRejectedId();
        orderRejectedId();


    }


    @Test
    void shouldNotBuyWithSymbolNameOfHolder() {

        String month = "10",
                year = "24",
                holder = "&!%$",
                cvc = "123";


        var card = DataHelper.setValidCard(month, year, holder, cvc);
        StartingPage.selectBuyForm();

        paymentPage.cleanFields();
        paymentPage.completeCardData(card);
        paymentPage.shouldVerifyErrorOfField();

        paymentRejectedId();
        orderRejectedId();

    }


    @Test
    void shouldNotBuyWithNumberNameOfHolder() {

        String month = "10",
                year = "24",
                owner = "12345",
                cvv = "123";


        var card = DataHelper.setValidCard(month, year, owner, cvv);
        StartingPage.selectBuyForm();
        paymentPage.cleanFields();
        paymentPage.completeCardData(card);
        paymentPage.shouldVerifyErrorOfField();

        paymentRejectedId();
        orderRejectedId();


    }


    @Test
    void shouldNotBuyWithEmptyNameOfHolder() {

        String month = "10",
                year = "24",
                holder = "",
                cvc = "123";


        var card = DataHelper.setValidCard(month, year, holder, cvc);
        StartingPage.selectBuyForm();
        paymentPage.cleanFields();
        paymentPage.completeCardData(card);
        paymentPage.shouldVerifyRequiredField();
        paymentRejectedId();
        orderRejectedId();

    }


    @Test
    void shouldNotBuyWith2NumbersOfCVC() {

        String month = "10",
                year = "24",
                holder = DataHelper.generateHolder("En"),
                cvc = "12";


        var cardInfo = DataHelper.setValidCard(month, year, holder, cvc);
        StartingPage.selectBuyForm();

        paymentPage.cleanFields();
        paymentPage.completeCardData(cardInfo);
        paymentPage.shouldVerifyErrorOfField();

        paymentRejectedId();
        orderRejectedId();


    }


    @Test
    void shouldNotBuyWithZeroCVC() {

        String month = "10",
                year = "24",
                holder = DataHelper.generateHolder("En"),
                cvc = "000";


        var card = DataHelper.setValidCard(month, year, holder, cvc);
        StartingPage.selectBuyForm();

        paymentPage.cleanFields();
        paymentPage.completeCardData(card);
        paymentPage.shouldVerifyErrorOfField();

        paymentRejectedId();
        orderRejectedId();


    }


    @Test
    void shouldNotBuyWithEmptyCVC() {

        String month = "10",
                year = "24",
                holder = DataHelper.generateHolder("En"),
                cvc = "";


        var card = DataHelper.setValidCard(month, year, holder, cvc);
        StartingPage.selectBuyForm();

        paymentPage.cleanFields();
        paymentPage.completeCardData(card);
        paymentPage.shouldVerifyErrorOfField();

        paymentRejectedId();
        orderRejectedId();


    }
}


