package test;

import org.junit.jupiter.api.Assertions;
import src.main.java.CashFlowVisualization_story15.Transaction;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

public class TransactionTest {

    @Test
    public void testIncomeAndExpenseCalculation() {
        List<Transaction> data = Arrays.asList(
                new Transaction("income", 1000),
                new Transaction("expense", 600)
        );
        Assertions.assertEquals(400, data.get(0).getAmount() - data.get(1).getAmount());
    }
}
