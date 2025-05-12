package src.test.CashFlowVisualization_story15;

import org.junit.jupiter.api.Assertions;
import src.main.java.CashFlowVisualization_story15.Transaction;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Title      : TransactionTest.java
 * Description: Unit test for the Transaction model class.
 *              Verifies that income and expense amounts can be correctly
 *              used to calculate net cash flow.
 *
 * @author Haoran Sun
 * @version 1.0
 */
public class TransactionTest {

    /**
     * Tests that the difference between an income transaction and an expense transaction
     * yields the correct net amount.
     * <p>
     * Creates two Transaction instances: one with type "income" of 1000
     * and one with type "expense" of 600, then asserts that
     * income minus expense equals 400.
     * </p>
     */
    @Test
    public void testIncomeAndExpenseCalculation() {
        List<Transaction> data = Arrays.asList(
                new Transaction("income", 1000),
                new Transaction("expense", 600)
        );
        Assertions.assertEquals(
                400,
                data.get(0).getAmount() - data.get(1).getAmount(),
                "Income (1000) minus expense (600) should equal 400"
        );
    }
}
