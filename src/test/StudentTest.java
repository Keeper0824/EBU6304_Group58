package src.test;
import src.main.Student;

import static org.junit.jupiter.api.Assertions.*;


public class StudentTest {

    @org.junit.jupiter.api.Test
    public void testCreateStudent() {
        Student s1 = new Student();
        s1.getName();
    }
}
