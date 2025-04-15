package src.test;
import src.main.CashFlowAnalyzer;
import src.main.Transaction;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TransactionTest {

    @Test
    public void testIncomeAndExpenseCalculation() {
        List<Transaction> data = Arrays.asList(
                new Transaction("income", 1000),
                new Transaction("expense", 600)
        );

        CashFlowAnalyzer analyzer = new CashFlowAnalyzer(data);
        assertEquals(6000, analyzer.getTotalIncome());
        assertEquals(500, analyzer.getTotalExpense());
    }
}




