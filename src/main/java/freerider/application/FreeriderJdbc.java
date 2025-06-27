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

    // @Autowired
    // private org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
    //                     namedParameterJdbcTemplate;

    @FunctionalInterface
    public interface CustomerRowPrinter {
        void printRow(String id, String name, String contact, String status);
    }

    @FunctionalInterface
    public interface VehicleRowPrinter {
        void printRow(String id, String make, String model, String seats, String category, String power, String status);
    }

    @FunctionalInterface
    public interface ReservationRowPrinter {
        void printRow(String id, String cust_id, String veh_id, String begin, String end, String pickup, String drop, String status);
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

    public void findAll(VehicleRowPrinter rm) {
        // 
        jdbcTemplate.query("SELECT * FROM VEHICLE", rs -> {
            try {
                rm.printRow(
                    String.format("%d", rs.getInt("ID")),
                    rs.getString("MAKE"),
                    rs.getString("MODEL"),
                    String.format("%d", rs.getInt("SEATS")),
                    rs.getString("CATEGORY"),
                    rs.getString("POWER"),
                    rs.getString("STATUS")
                );
            } catch(SQLException e) { }
        });
    }

    public void findAll(ReservationRowPrinter rm) {
        // 
        jdbcTemplate.query("SELECT * FROM RESERVATION", rs -> {
            try {
                rm.printRow(
                    String.format("%d", rs.getInt("ID")),
                    String.format("%d", rs.getInt("CUSTOMER_ID")),
                    String.format("%d", rs.getInt("VEHICLE_ID")),
                    rs.getString("RBEGIN"),
                    rs.getString("REND"),
                    rs.getString("PICKUP"),
                    rs.getString("DROPOFF"),
                    rs.getString("STATUS")
                );
            } catch(SQLException e) { }
        });
    }
}