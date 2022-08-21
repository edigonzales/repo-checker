package ch.so.agi.repochecker;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.filter.ForwardedHeaderFilter;
import org.springframework.nativex.hint.TypeHint;
import org.springframework.nativex.hint.TypeAccess;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


// String.class wegen split() beim properties file auslesen.
@TypeHint(
        types = {/*ch.so.agi.repochecker.model.IliRepos.class, 
                ch.so.agi.repochecker.model.IliRepo.class,
                ch.so.agi.repochecker.model.Result.class,
                ch.so.agi.repochecker.model.Check.class,*/
                java.lang.String.class},
        access= {TypeAccess.DECLARED_METHODS, 
              TypeAccess.DECLARED_FIELDS, 
              TypeAccess.DECLARED_CONSTRUCTORS, 
              TypeAccess.PUBLIC_METHODS,
              TypeAccess.PUBLIC_FIELDS,
              TypeAccess.PUBLIC_CONSTRUCTORS}               
)
@Configuration
@EnableScheduling
@ServletComponentScan
@SpringBootApplication
public class RepoCheckerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RepoCheckerApplication.class, args);
	}

    @Bean
    ForwardedHeaderFilter forwardedHeaderFilter() {
        return new ForwardedHeaderFilter();
    }
    
    @Bean 
    XmlMapper xmlMapper() {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        xmlMapper.registerModule(new JavaTimeModule());

        return xmlMapper; 
    }
    
    // Anwendung ist fertig gestartet.
    @Bean
    CommandLineRunner init(CheckerService checker) {
        return args -> {
            checker.checkRepos();
        };
    }
}
