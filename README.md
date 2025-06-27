# 7. *Jdbc-Template*

Steps:

1. [*JDBC* Basics](#1-jdbc-basics)

1. [*Spring Boot:* *JdbcTemplate*](#2-spring-boot-jdbctemplate)



&nbsp;

## 1. *JDBC* Basics

[*Java Database Connectivity (JDBC)*](https://www.tutorialspoint.com/jdbc/index.htm)
is a Java API to access tabular data stored in Relational Databases.

*JDBC* prepares statements, a so-called
([*"PreparedStatement"*](https://docs.oracle.com/javase/8/docs/api/java/sql/PreparedStatement.html)),
which consists of SQL and `?`-markers substituted by parameters such as an
*id*-value. The resulting SQL Query is then sent to the database (as SQL) and
a result in form of a
[*"ResultSet"*](https://docs.oracle.com/javase/8/docs/api/java/sql/ResultSet.html)
(a table) is returned to iterate over.

```java
try(
    // create PreparedStatement as SQL with embedded '?' markers to replace
    PreparedStatement pstmt = dbconnection.prepareStatement(
        "SELECT * FROM CUSTOMER WHERE id = ?");
) {
    // replace '?' with 100 -> "SELECT * FROM CUSTOMER WHERE id = 1000"
    pstmt.setInt(1, 1000);
    // 
    // send SQL to database and receive ResultSet as answer
    ResultSet rs = pstmt.executeQuery();
    // 
    // iterate over ResultSet (at most one customer with id=1000)
    while(rs.next()) {
        var id = String.format("%d", rs.getInt("ID"));
        var name = rs.getString("NAME");
        var contact = rs.getString("CONTACT");
        var status = rs.getString("STATUS");
        System.out.println(String.format(" - row(%s, %s, %s, %s)", id, name, contact, status));
    }
} catch (SQLException e) {
    System.out.println(e.getMessage());
}
```



&nbsp;

## 2. *Spring Boot:* *JdbcTemplate*

*Spring Boot* supports *JDBC* by class
[*JdbcTemplate*](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/jdbc/core/JdbcTemplate.html)
that is part of the *spring-boot-starter-jdbc* package and must be included
as a new dependency in *pom.xml* :

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
```

The new dependency will (transitively) add new *.jar* files to *CLASSPATH* :

```sh
# re-source the project to rebuild CLASSPATH
source .env/env.sh
```

New *.jar* files have been added to *CLASSPATH* :
```
project environment has been set with:
 - created file: .classpath
 - CLASSPATH:
   - target/classes
   - ...
   - HikariCP-6.3.0.jar                     <-- new .jar files
   - spring-boot-starter-jdbc-3.5.0.jar
   - spring-jdbc-6.2.7.jar
   - spring-tx-6.2.7.jar
```

Java-code shows the basic use of *JdbcTemplate* in file
[*FreeriderJdbc.java*](src/main/java/freerider/application/FreeriderJdbc.java) :

```java
package freerider.application;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;


@Component
public class FreeriderJdbc {

    /*
     * Spring-initialized variable ("autowired")
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @FunctionalInterface
    public interface CustomerRowPrinter {
        void printRow(String id, String name, String contact, String status);
    }


    public void findAll(CustomerRowPrinter rm) {
        // 
        // https://mkyong.com/spring-boot/spring-boot-jdbc-examples/
        // https://www.baeldung.com/spring-jdbc-jdbctemplate
        // 
        jdbcTemplate.query("SELECT * FROM CUSTOMER", rs -> {
            try {
                rm.printRow(
                    String.format("%d", rs.getInt("ID")),
                    rs.getString("NAME"),
                    rs.getString("CONTACT"),
                    rs.getString("STATUS")
                );
            } catch(SQLException e) { }
        });
    }

    public void findById(int id, CustomerRowPrinter rm) {
        // 
        // use NamedParameterJdbcTemplate for substitution of named parameters
        jdbcTemplate.query("SELECT * FROM CUSTOMER WHERE ID = ?",
            ps -> ps.setInt(1, id),
            rs -> {
                rm.printRow(
                        String.format("%d", rs.getInt("ID")),
                        rs.getString("NAME"),
                        rs.getString("CONTACT"),
                        rs.getString("STATUS")
                    );
            });
        // 
        // Alternatively, NamedParameterJdbcTemplate can be used for substitution
        // of named parameters:
        // 
        // namedParameterJdbcTemplate.query("SELECT * FROM CUSTOMER WHERE ID = :id",
        //     new org.springframework.jdbc.core.namedparam.MapSqlParameterSource()
        //         .addValue("id", id),
        //     rs -> {
        //         try {
        //             rm.printRow(
        //                 String.format("%d", rs.getInt("ID")),
        //                 rs.getString("NAME"),
        //                 rs.getString("CONTACT"),
        //                 rs.getString("STATUS")
        //             );
        //         } catch(SQLException e) { }
        //     });
    }
}
```

Running the program outputs:

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/

 :: Spring Boot ::                (v3.5.0)

[INFO ] [00:09:14:757, freerider.application.Application               ] - Starting Application using Java 21 with PID 17780 (C:\Sven1\svgr2\tmp\svgr\workspaces\2-se\spring-freerider\target\classes started by svgr2 in c:\Sven1\svgr2\tmp\svgr\workspaces\2-se\spring-freerider)
[INFO ] [00:09:14:776, freerider.application.Application               ] - No active profile set, falling back to 1 default profile: "default"
[INFO ] [00:09:16:263, com.zaxxer.hikari.HikariDataSource              ] - HikariPool-1 - Starting...
[INFO ] [00:09:16:595, com.zaxxer.hikari.pool.HikariPool               ] - HikariPool-1 - Added connection conn0: url=jdbc:h2:mem:freerider user=SA
[INFO ] [00:09:16:616, com.zaxxer.hikari.HikariDataSource              ] - HikariPool-1 - Start completed.
[INFO ] [00:09:16:951, freerider.application.Application               ] - Started Application in 3.082 seconds (process running for 3.611)

findAll(CustomerRowPrinter rm):
+------+---------------------+---------------------+---------------+
| ID   | NAME                | CONTACT             | STATUS        |
+------+---------------------+---------------------+---------------+
| 1000 | Meyer, Eric         | eme22@gmail.com     | Active        |
| 1001 | Sommer, Tina        | 030 22458 29425     | Active        |
| 1002 | Schulze, Tim        | +49 171 2358124     | InRegistratio |
+------+---------------------+---------------------+---------------+

findById(int id, CustomerRowPrinter rm):
+------+---------------------+---------------------+---------------+
| ID   | NAME                | CONTACT             | STATUS        |
+------+---------------------+---------------------+---------------+
| 1000 | Meyer, Eric         | eme22@gmail.com     | Active        |
+------+---------------------+---------------------+---------------+

findAll(VehicleRowPrinter rm):
+------+---------------------+---------------------+-----+---------+----------+------------+
| ID   | MAKE                | MODEL               | SEA | CATEG   | POWER    | STATUS     |
+------+---------------------+---------------------+-----+---------+----------+------------+
| 8000 | VW                  | ID.3                |   4 | Sedan   | Electric | Active     |
| 8001 | VW                  | Golf                |   4 | Sedan   | Gasoline | Active     |
| 8002 | VW                  | Golf                |   4 | Sedan   | Hybrid   | Active     |
| 8003 | BMW                 | 320d                |   4 | Sedan   | Diesel   | Active     |
| 8004 | Mercedes            | EQS                 |   4 | Sedan   | Electric | Active     |
| 8005 | VW                  | Multivan Life       |   8 | Van     | Gasoline | Active     |
| 8006 | Tesla               | Model 3             |   4 | Sedan   | Electric | Active     |
| 8007 | Tesla               | Model S             |   4 | Sedan   | Electric | Serviced   |
+------+---------------------+---------------------+-----+---------+----------+------------+

void findAll(ReservationRowPrinter rm):
+-------+------+------+------------------+------------------+-----------------+----------------+------------+
| ID    |CUS_ID|VEH_ID| BEGIN            | END              | PICKUP          | DROP-OFF       | STATUS     |
+-------+------+------+------------------+------------------+-----------------+----------------+------------+
| 145373|  1001|  8002| 2025-07-04 20:00:| 2025-07-04 23:00:| Berlin Wedding  | Hamburg        | Inquired   |
| 201235|  1000|  8002| 2025-07-20 10:00:| 2025-07-20 20:00:| Berlin Wedding  | Berlin Wedding | Booked     |
| 351682|  1002|  8001| 2025-06-11 08:00:| 2025-06-11 20:00:| Berlin Wedding  | Hamburg        | Inquired   |
| 382565|  1000|  8006| 2025-07-18 18:00:| 2025-07-07 18:10:| Berlin Wedding  | Hamburg        | Inquired   |
| 682351|  1002|  8003| 2025-07-18 09:00:| 2025-07-07 18:00:| Potsdam         | Teltow         | Inquired   |
+-------+------+------+------------------+------------------+-----------------+----------------+------------+

[INFO ] [00:09:17:084, com.zaxxer.hikari.HikariDataSource              ] - HikariPool-1 - Shutdown initiated...
[INFO ] [00:09:17:110, com.zaxxer.hikari.HikariDataSource              ] - HikariPool-1 - Shutdown completed.
```


<!--
An alternative to CommandLineRunner @Component is a @Bean factory method
at a @Configuration class:

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FreeriderDatabaseRunner {

    @Autowired
    FreeriderDatabaseJdbc freeriderDB;

    @Bean
    public CommandLineRunner startup() {
        return args -> {
            freeriderDB.findAll();
        };
    }
}
-->
