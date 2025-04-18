package src.test;
import src.main.Student;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class StudentTest {

    @org.junit.jupiter.api.Test
    public void testCreateStudent() {
        Student s1 = new Student();
        s1.setName("Keeper");
        assertEquals("Keeper",s1.getName());
    }
}
