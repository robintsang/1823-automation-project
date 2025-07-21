package academy.teenfuture.crse.config;
import org.hibernate.dialect.MySQLDialect;

/**
 * Extends MySQL5InnoDBDialect and sets the default charset to be UTF-8
 * @author Sorin Postelnicu
 * @since Aug 13, 2007
 */
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomMysqlDialect extends MySQLDialect {

    public String getTableTypeString() {
        return " ENGINE=InnoDB DEFAULT CHARSET=utf8";
    }
}