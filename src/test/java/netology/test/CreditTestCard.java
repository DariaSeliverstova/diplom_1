package netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import netology.data.DBHelper;
import netology.data.DataHelper;
import netology.page.CreditPage;
import netology.page.StartingPage;
import netology.util.ScreenShooterReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.open;

@ExtendWith({ScreenShooterReportPortalExtension.class})
public class CreditTestCard {
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

    public final CreditPage creditPage = new CreditPage();


    public void creditApprovedStatus() {
        String statusExpected = "APPROVED";
        String statusActual = DBHelper.getCreditStatus();
        Assertions.assertEquals(statusExpected, statusActual);
    }

    public void creditDeclinedStatus() {
        String statusExpected = "DECLINED";
        String statusActual = DBHelper.getCreditStatus();
        Assertions.assertEquals(statusExpected, statusActual);
    }

    public void creditAcceptId() {
        long idExpected = 1;
        long idActual = DBHelper.getCreditId();
        Assertions.assertEquals(idExpected, idActual);
    }

    public void creditRejectedId() {
        long idExpected = 0;
        long idActual = DBHelper.getCreditId();
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
    void shouldBuyCreditOnValidCard() {
        String date = DataHelper.generateYear(2);
        String month = "10",
                year = date,
                holder = DataHelper.generateHolder("En"),
                cvc = "123";

        var card = DataHelper.setValidCard(month, year, holder, cvc);
        StartingPage.selectCreditForm();


        creditPage.cleanFields();
        creditPage.completeCardData(card);
        creditPage.acceptPay();
        creditApprovedStatus();
        creditAcceptId();
        orderAcceptId();
    }


    @Test
    void shouldNotBuyCreditOnDeclinedCard() {
        String date = DataHelper.generateYear(2);
        String month = "10",
                year = date,
                holder = DataHelper.generateHolder("En"),
                cvc = "123";

        var card = DataHelper.setInvalidCard(month, year, holder, cvc);
        StartingPage.selectCreditForm();

        creditPage.cleanFields();
        creditPage.completeCardData(card);

        creditPage.rejectedPay();
        creditDeclinedStatus();
        creditRejectedId();
        orderRejectedId();

    }


    @Test
    void shouldNotBuyCreditOnCardLess16Numbers() {
        String date = DataHelper.generateYear(2);
        String month = "10",
                year = date,
                holder = DataHelper.generateHolder("En"),
                cvc = "123",
                shortCardNumber = "4444 4444 4444 444";

        var card = DataHelper.setCard(shortCardNumber, month, year, holder, cvc);
        StartingPage.selectCreditForm();

        creditPage.cleanFields();
        creditPage.completeCardData(card);
        creditPage.shouldVerifyErrorOfField();
        creditRejectedId();
        orderRejectedId();

    }


    @Test
    void shouldNotBuyCreditOnZeroCard() {
        String date = DataHelper.generateYear(2);
        String month = "10",
                year = date,
                holder = DataHelper.generateHolder("En"),
                cvc = "123",
                zeroCardNumber = "0000 0000 0000 0000";

        var card = DataHelper.setCard(zeroCardNumber, month, year, holder, cvc);
        StartingPage.selectCreditForm();

        creditPage.cleanFields();
        creditPage.completeCardData(card);
        creditPage.rejectedPay();
        creditRejectedId();
        orderRejectedId();


    }


    @Test
    void shouldNotBuyCreditOnEmptyNumberOfCard() {
        String date = DataHelper.generateYear(2);
        String month = "10",
                year = date,
                holder = DataHelper.generateHolder("En"),
                cvc = "123",
                emptyCardNumber = "";

        var card = DataHelper.setCard(emptyCardNumber, month, year, holder, cvc);
        StartingPage.selectCreditForm();

        creditPage.cleanFields();
        creditPage.completeCardData(card);
        creditPage.shouldVerifyErrorOfField();
        creditRejectedId();
        orderRejectedId();


    }


    @Test
    void shouldNotBuyCreditOnNotRegisteredCard() {
        String date = DataHelper.generateYear(2);
        String month = "10",
                year = date,
                holder = DataHelper.generateHolder("En"),
                cvc = "123",
                notRegisteredCardNumber = "4444 4444 4444 4443";

        var card = DataHelper.setCard(notRegisteredCardNumber, month, year, holder, cvc);
        StartingPage.selectCreditForm();

        creditPage.cleanFields();
        creditPage.completeCardData(card);
        creditPage.rejectedPay();

        creditRejectedId();
        orderRejectedId();


    }


    @Test
    void shouldNotBuyCreditWithZeroMonth() {
        String date = DataHelper.generateYear(2);
        String month = "00",
                year = date,
                holder = DataHelper.generateHolder("En"),
                cvc = "123";


        var card = DataHelper.setValidCard(month, year, holder, cvc);
        StartingPage.selectCreditForm();

        creditPage.cleanFields();
        creditPage.completeCardData(card);
        creditPage.shouldVerifyErrorOfField();

        creditRejectedId();
        orderRejectedId();


    }


    @Test
    void shouldNotBuyCreditWith13Month() {
        String date = DataHelper.generateYear(2);
        String month = "13",
                year = date,
                holder = DataHelper.generateHolder("En"),
                cvc = "123";


        var card = DataHelper.setValidCard(month, year, holder, cvc);
        StartingPage.selectCreditForm();

        creditPage.cleanFields();
        creditPage.completeCardData(card);
        creditPage.shouldVerifyIncorrectExpiryDate();

        creditRejectedId();
        orderRejectedId();


    }


    @Test
    void shouldNotBuyCreditWithEmptyMonth() {
        String date = DataHelper.generateYear(2);
        String month = "",
                year = date,
                holder = DataHelper.generateHolder("En"),
                cvc = "123";


        var card = DataHelper.setValidCard(month, year, holder, cvc);
        StartingPage.selectCreditForm();

        creditPage.cleanFields();
        creditPage.completeCardData(card);
        creditPage.shouldVerifyErrorOfField();

        creditRejectedId();
        orderRejectedId();


    }


    @Test
    void shouldNotBuyCreditWith1NumberOfMonth() {
        String date = DataHelper.generateYear(2);
        String month = "1",
                year = date,
                holder = DataHelper.generateHolder("En"),
                cvc = "123";


        var card = DataHelper.setValidCard(month, year, holder, cvc);
        StartingPage.selectCreditForm();

        creditPage.cleanFields();
        creditPage.completeCardData(card);
        creditPage.shouldVerifyErrorOfField();

        creditRejectedId();
        orderRejectedId();


    }


    @Test
    void shouldNotBuyCreditWithLastYear() {
        String date = DataHelper.generateYear(-1);
        String month = "10",
                year = date,
                holder = DataHelper.generateHolder("En"),
                cvc = "123";


        var card = DataHelper.setValidCard(month, year, holder, cvc);
        StartingPage.selectCreditForm();

        creditPage.cleanFields();
        creditPage.completeCardData(card);
        creditPage.shouldVerifyCardDateExpired();

        creditRejectedId();
        orderRejectedId();


    }


    @Test
    void shouldNotBuyCreditWithEmptyYear() {

        String month = "10",
                year = "",
                holder = DataHelper.generateHolder("En"),
                cvc = "123";


        var card = DataHelper.setValidCard(month, year, holder, cvc);
        StartingPage.selectCreditForm();

        creditPage.cleanFields();
        creditPage.completeCardData(card);
        creditPage.shouldVerifyErrorOfField();

        creditRejectedId();
        orderRejectedId();


    }


    @Test
    void shouldNotBuyCreditWithZeroYear() {

        String month = "10",
                year = "00",
                holder = DataHelper.generateHolder("En"),
                cvc = "123";


        var card = DataHelper.setValidCard(month, year, holder, cvc);
        StartingPage.selectCreditForm();

        creditPage.cleanFields();
        creditPage.completeCardData(card);
        creditPage.shouldVerifyCardDateExpired();

        creditRejectedId();
        orderRejectedId();


    }


    @Test
    void shouldNotBuyCreditWith1Year() {

        String month = "10",
                year = "1",
                holder = DataHelper.generateHolder("En"),
                cvc = "123";


        var card = DataHelper.setValidCard(month, year, holder, cvc);
        StartingPage.selectCreditForm();

        creditPage.cleanFields();
        creditPage.completeCardData(card);
        creditPage.shouldVerifyErrorOfField();

        creditRejectedId();
        orderRejectedId();


    }


    @Test
    void shouldNotBuyCreditWithCyrillicNameOfHolder() {

        String month = "10",
                year = "24",
                holder = DataHelper.generateHolder("Ru"),
                cvc = "123";


        var card = DataHelper.setValidCard(month, year, holder, cvc);
        StartingPage.selectCreditForm();

        creditPage.cleanFields();
        creditPage.completeCardData(card);
        creditPage.shouldVerifyErrorOfField();

        creditRejectedId();
        orderRejectedId();


    }


    @Test
    void shouldNotBuyCreditWithSymbolNameOfHolder() {

        String month = "10",
                year = "24",
                holder = "&!%$",
                cvc = "123";


        var card = DataHelper.setValidCard(month, year, holder, cvc);
        StartingPage.selectCreditForm();

        creditPage.cleanFields();
        creditPage.completeCardData(card);
        creditPage.shouldVerifyErrorOfField();

        creditRejectedId();
        orderRejectedId();


    }


    @Test
    void shouldNotBuyCreditWithNumberNameOfHolder() {

        String month = "10",
                year = "24",
                holder = "12345",
                cvc = "123";


        var card = DataHelper.setValidCard(month, year, holder, cvc);
        StartingPage.selectCreditForm();

        creditPage.cleanFields();
        creditPage.completeCardData(card);
        creditPage.shouldVerifyErrorOfField();

        creditRejectedId();
        orderRejectedId();


    }


    @Test
    void shouldNotBuyCreditWithEmptyNameOfHolder() {

        String month = "10",
                year = "24",
                holder = "",
                cvc = "123";


        var card = DataHelper.setValidCard(month, year, holder, cvc);
        StartingPage.selectCreditForm();

        creditPage.cleanFields();
        creditPage.completeCardData(card);
        creditPage.shouldVerifyRequiredField();
        creditRejectedId();
        orderRejectedId();

    }


    @Test
    void shouldNotBuyCreditWith2NumbersOfCVC() {

        String month = "10",
                year = "24",
                holder = DataHelper.generateHolder("En"),
                cvc = "12";


        var card = DataHelper.setValidCard(month, year, holder, cvc);
        StartingPage.selectCreditForm();

        creditPage.cleanFields();
        creditPage.completeCardData(card);
        creditPage.shouldVerifyErrorOfField();

        creditRejectedId();
        orderRejectedId();


    }


    @Test
    void shouldNotBuyCreditWithZeroCVC() {

        String month = "10",
                year = "24",
                holder = DataHelper.generateHolder("En"),
                cvc = "000";


        var card = DataHelper.setValidCard(month, year, holder, cvc);
        StartingPage.selectCreditForm();

        creditPage.cleanFields();
        creditPage.completeCardData(card);
        creditPage.shouldVerifyErrorOfField();

        creditRejectedId();
        orderRejectedId();


    }


    @Test
    void shouldNotBuyCreditWithEmptyCVC() {

        String month = "10",
                year = "24",
                holder = DataHelper.generateHolder("En"),
                cvc = "";


        var card = DataHelper.setValidCard(month, year, holder, cvc);
        StartingPage.selectCreditForm();

        creditPage.cleanFields();
        creditPage.completeCardData(card);
        creditPage.shouldVerifyErrorOfField();

        creditRejectedId();
        orderRejectedId();


    }

}
