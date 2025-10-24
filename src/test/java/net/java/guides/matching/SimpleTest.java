package net.java.guides.matching;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SimpleTest {
    @Test
    void contextLoads() {
        System.out.println("✅ Test de contexte Spring - Si ça passe, le problème est ailleurs");
    }
}