package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;


import java.time.Duration;


import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {


    private final SelenideElement amountInput = $("[data-test-id=amount] input");
    private final SelenideElement fromInput = $("[data-test-id=from] input");
    private final SelenideElement transferButton = $("[data-test-id=action-transfer]");
    private final SelenideElement transferHead = $(byText("Пополнение карты"));
    private final SelenideElement errorMessageContent = $("[data-test-id='error-notification'] .notification__content");
    private final SelenideElement errorMessageTitle = $("[data-test-id='error-notification'] .notification__title");

    public TransferPage() {
        transferHead.shouldBe(Condition.visible);
    }

    public void makeTransfer(String amountToTransfer, DataHelper.CardInfo cardInfo) {
        amountInput.setValue(amountToTransfer);
        fromInput.setValue(cardInfo.getCardNumber());
        transferButton.click();
    }

    public void makeTransferError(String amountToTransfer, DataHelper.CardInfo cardInfo) {
        amountInput.setValue(amountToTransfer);
        long first15 = (long) (Math.random() * 1000000000000000L);
        String strFirst15 = Long.toString(first15);
        String errorCard = "0" + strFirst15;
        fromInput.setValue(errorCard);
        transferButton.click();
    }

    public DashboardPage makeValidTransfer(String amountToTransfer, DataHelper.CardInfo cardInfo) {
        makeTransfer(amountToTransfer, cardInfo);
        return new DashboardPage();
    }

    public void findErrorMessageContent(String expectedText, String title) {
        errorMessageContent.shouldHave(Condition.exactText(expectedText), Duration.ofSeconds(15)).shouldBe(Condition.visible);
        errorMessageTitle.shouldHave(Condition.exactText(title), Duration.ofSeconds(15)).shouldBe(Condition.visible);
    }


}