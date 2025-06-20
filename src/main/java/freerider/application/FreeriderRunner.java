package freerider.application;

// make sure to import 'org.slf4j.Logger' and not 'java.lang.System.Logger',
// which is Java's built-in logger (former 'java.util.logging' JUL), see
// https://stackoverflow.com/questions/11359187/why-not-use-java-util-logging
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class FreeriderRunner implements CommandLineRunner {

    /**
     * Any String can be used to create or identify a new logger instance.
     * A common technique has become to use the fully qualified class name.
     */
    private static final Logger log = LoggerFactory.getLogger(FreeriderRunner.class.getName());

    /*
     * Access value of 'spring.application.name' property
     */
    @Value("${spring.application.name}")
    private String programName;

    /**
     * Constructor executes when the Spring runtime system creates the Application Bean.
     */
    FreeriderRunner() {
        log.info("-<1>--> FreeriderRunner constructor called");
    }


    @Override
    public void run(String... args) throws Exception {
        /*
         * print program name as welcome message
         */
        log.info(String.format("-<2>--> FreeriderRunner.run() for %s", programName));

        // Trace > Debug > Info > Warn > Error > Fatal > Off
        log.trace("trace log message");
        log.debug("debug log message");
        log.info("info log message");
        log.warn("warning log message");
        log.error("error log message");
    }
}
