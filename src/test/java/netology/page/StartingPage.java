package netology.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class StartingPage {


    final private static SelenideElement buttonBuy = $x("//*[@id='root']/div/button[1]");
    ;

    final private static SelenideElement buttonCredit = $x("//*[@id='root']/div/button[2]");


    public static void selectBuyForm() {
        buttonBuy.click();

    }

    public static void selectCreditForm() {
        buttonCredit.click();

    }

}


