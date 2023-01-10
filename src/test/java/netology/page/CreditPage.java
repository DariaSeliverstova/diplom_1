package netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import netology.data.DataHelper;
import org.openqa.selenium.Keys;

import java.time.Duration;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class CreditPage {
    final private static SelenideElement headingBuyCredit = $(byText("Кредит по данным карты"));
    final private SelenideElement notificationAcceptTitle = $(".notification_status_ok");
    final private SelenideElement notificationAcceptContent = $(".notification_status_ok .notification__content");
    final private SelenideElement notificationErrorTitle = $(".notification_status_error");
    final private SelenideElement notificationErrorContent = $(".notification_status_error .notification__content");
    final private static SelenideElement number = $("input[placeholder='0000 0000 0000 0000']");
    final private static SelenideElement month = $("input[placeholder='08']");
    ;
    final private static SelenideElement year = $("input[placeholder='22']");
    final private static SelenideElement holder = $x("//*[.='Владелец'] //input");
    final private static SelenideElement cvc = $("[placeholder='999']");
    final private static SelenideElement incorrectFormat = $(byText("Неверный формат"));
    final private static SelenideElement incorrectExpiryDate = $(byText("Неверно указан срок действия карты"));
    final private static SelenideElement cardDateExpired = $(byText("Истёк срок действия карты"));
    final private static SelenideElement requiredField = $(byText("Поле обязательно для заполнения"));
    final private static SelenideElement buttonContinue = $(".form-field button");


    public static void CreditForm() {
        headingBuyCredit.shouldBe(Condition.visible);
    }


    public void acceptPay() {
        notificationAcceptTitle.shouldBe(Condition.text("Успешно"), Duration.ofSeconds(10)).shouldBe(Condition.visible);
        notificationAcceptContent.shouldBe(Condition.text("Операция одобрена Банком."), Duration.ofSeconds(10)).shouldBe(Condition.visible);
    }

    public void rejectedPay() {
        notificationErrorTitle.shouldBe(Condition.text("Ошибка"), Duration.ofSeconds(10)).shouldBe(Condition.visible);
        notificationErrorContent.shouldBe(Condition.text("Ошибка! Банк отказал в проведении операции."), Duration.ofSeconds(10)).shouldBe(Condition.visible);
    }


    public final void completeCardData(DataHelper.Card info) {
        number.setValue(info.getNumber());
        month.setValue(info.getMonth());
        year.setValue(info.getYear());
        holder.setValue(info.getHolder());
        cvc.setValue(info.getCvc());
        buttonContinue.click();
    }

    public final void cleanFields() {
        number.sendKeys(Keys.LEFT_CONTROL + "A");
        number.sendKeys(Keys.DELETE);
        month.sendKeys(Keys.LEFT_CONTROL + "A");
        month.sendKeys(Keys.DELETE);
        year.sendKeys(Keys.LEFT_CONTROL + "A");
        year.sendKeys(Keys.DELETE);
        holder.sendKeys(Keys.LEFT_CONTROL + "A");
        holder.sendKeys(Keys.DELETE);
        cvc.sendKeys(Keys.LEFT_CONTROL + "A");
        cvc.sendKeys(Keys.DELETE);
    }

    public void shouldVerifyErrorOfField() {
        incorrectFormat.shouldBe(Condition.visible);
    }

    public void shouldVerifyIncorrectExpiryDate() {
        incorrectExpiryDate.shouldBe(Condition.visible);
    }

    public void shouldVerifyCardDateExpired() {
        cardDateExpired.shouldBe(Condition.visible);
    }

    public void shouldVerifyRequiredField() {
        requiredField.shouldBe(Condition.visible);
    }

}
