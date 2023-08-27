package ru.netology.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.data.DataHelper.*;


public class MoneyTransferTest {

    DashboardPage dashboardPage;

    @BeforeEach
    void setup() {
        var loinPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = getAuthInfo();
        var verificationPage = loinPage.validLogin(authInfo);
        var verificationCode = getVerificationCode();
        dashboardPage = verificationPage.validVerify(verificationCode);
    }

    @Test
    void transferFromFirstToSecondCardTest() {
        var firstCardInfo = getFirstCard();
        var secondCardInfo = getSecondCard();
        var firstCardBalance = dashboardPage.getCardBalance(firstCardInfo);
        var secondCardBalance = dashboardPage.getCardBalance(secondCardInfo);
        var amount = generalValidAmount(firstCardBalance);
        var transferPage = dashboardPage.selectCardToTransfer(secondCardInfo);
        dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), firstCardInfo);

        var expectedBalanceFirstCard = firstCardBalance - amount;
        var expectedBalanceSecondCard = secondCardBalance + amount;
        var actualBalanceFirstCard = dashboardPage.getCardBalance(firstCardInfo);
        var actualBalanceSecondCard = dashboardPage.getCardBalance(secondCardInfo);

        Assertions.assertEquals(expectedBalanceFirstCard, actualBalanceFirstCard);
        Assertions.assertEquals(expectedBalanceSecondCard, actualBalanceSecondCard);

    }

    @Test
    void transferFromSecondToFirstCardTest() {
        var firstCardInfo = getFirstCard();
        var secondCardInfo = getSecondCard();
        var firstCardBalance = dashboardPage.getCardBalance(firstCardInfo);
        var secondCardBalance = dashboardPage.getCardBalance(secondCardInfo);
        var amount = generalValidAmount(secondCardBalance);
        var transferPage = dashboardPage.selectCardToTransfer(firstCardInfo);
        dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), secondCardInfo);

        var expectedBalanceFirstCard = firstCardBalance + amount;
        var expectedBalanceSecondCard = secondCardBalance - amount;
        var actualBalanceFirstCard = dashboardPage.getCardBalance(firstCardInfo);
        var actualBalanceSecondCard = dashboardPage.getCardBalance(secondCardInfo);

        Assertions.assertEquals(expectedBalanceFirstCard, actualBalanceFirstCard);
        Assertions.assertEquals(expectedBalanceSecondCard, actualBalanceSecondCard);

    }

    @Test
    void transferFromTheFirstToTheSecondCardWithTheWrongNumberTest() {
        var firstCardInfo = getFirstCard();
        var secondCardInfo = getSecondCard();
        var invalidCardInfo = getInvalidCard();
        var firstCardBalance = dashboardPage.getCardBalance(firstCardInfo);
        var amount = generalValidAmount(firstCardBalance);
        var transferPage = dashboardPage.selectCardToTransfer(secondCardInfo);
        transferPage.makeTransfer(String.valueOf(amount), invalidCardInfo);
        transferPage.findErrorMessageContent("Ошибка! Произошла ошибка", "Ошибка");
    }

    @Test
    void ttransferFromFirstToSecondWithIncorrectAmountTest() {
        var firstCardInfo = getFirstCard();
        var secondCardInfo = getSecondCard();
        var firstCardBalance = dashboardPage.getCardBalance(firstCardInfo);
        var amount = generalInvalidAmount(firstCardBalance);
        var transferPage = dashboardPage.selectCardToTransfer(secondCardInfo);
        transferPage.makeTransfer(String.valueOf(amount), firstCardInfo);
        transferPage.findErrorMessageContent("Сумма перевода превышает остаток на карте списания", "Ошибка");


    }

}