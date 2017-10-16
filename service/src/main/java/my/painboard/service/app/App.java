package my.painboard.service.app;

import my.painboard.db.config.DBConfig;
import my.painboard.service.config.WebConf;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

//@Configuration
@SpringBootApplication
@Import(value = {DBConfig.class, WebConf.class})
public class App {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(App.class, args);
    }

}
