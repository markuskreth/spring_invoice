package de.kreth.clubhelper.invoice;

import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
@EnableCaching
public class ClubhelperInvoiceApplication {

    public static void main(String[] args) {
	Locale.setDefault(Locale.GERMANY);
	SpringApplication.run(ClubhelperInvoiceApplication.class, args);
    }

}
