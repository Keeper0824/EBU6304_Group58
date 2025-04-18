package src.test;
import src.main.java.Transaction;

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

    }
}




