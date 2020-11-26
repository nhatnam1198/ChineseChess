package ai.chinesechess;

import ai.chinesechess.controllers.GreetingController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication

public class ChineseChessApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChineseChessApplication.class, args);
    }

}
