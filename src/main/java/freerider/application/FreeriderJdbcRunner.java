package freerider.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class FreeriderJdbcRunner implements CommandLineRunner {

    @Autowired
    private FreeriderJdbc freeriderDB;


    @Override
    public void run(String... args) throws Exception {
        // 
        // +------+---------------------+---------------------+---------------+
        // | ID   | NAME                | CONTACT             | STATUS        |
        // +------+---------------------+---------------------+---------------+
        // | 1000 | Meyer, Eric         | eme22@gmail.com     | Active        |
        // | 1001 | Sommer, Tina        | 030 22458 29425     | Active        |
        // | 1002 | Schulze, Tim        | +49 171 2358124     | InRegistratio |
        // +------+---------------------+---------------------+---------------+
        final TableFormatter tf1 = new TableFormatter("| %-5s", "| %-20s", "| %-20s", "| %-13s |")
            .line()
            .row("ID", "NAME", "CONTACT", "STATUS")
            .line();
        // 
        freeriderDB.findAll( (id, name, contact, status) -> tf1.row(id, name, contact, status));
        tf1.line();
        System.out.println("findAll(CustomerRowPrinter rm):\n" + tf1.get().toString());
        // 
        // +------+---------------------+---------------------+---------------+
        // | ID   | NAME                | CONTACT             | STATUS        |
        // +------+---------------------+---------------------+---------------+
        // | 1000 | Meyer, Eric         | eme22@gmail.com     | Active        |
        // +------+---------------------+---------------------+---------------+
        final TableFormatter tf2 = new TableFormatter("| %-5s", "| %-20s", "| %-20s", "| %-13s |")
            .line()
            .row("ID", "NAME", "CONTACT", "STATUS")
            .line();
        // 
        freeriderDB.findById(1000, (id, name, contact, status) -> tf2.row(id, name, contact, status));
        tf2.line();
        System.out.println("findById(int id, CustomerRowPrinter rm):\n" + tf2.get().toString());

        // +------+---------------------+---------------------+-----+---------+----------+------------+
        // | ID   | MAKE                | MODEL               | SEA | CATEG   | POWER    | STATUS     |
        // +------+---------------------+---------------------+-----+---------+----------+------------+
        // | 8000 | VW                  | ID.3                |   4 | Sedan   | Electric | Active     |
        // | 8001 | VW                  | Golf                |   4 | Sedan   | Gasoline | Active     |
        // | 8002 | VW                  | Golf                |   4 | Sedan   | Hybrid   | Active     |
        // | 8003 | BMW                 | 320d                |   4 | Sedan   | Diesel   | Active     |
        // | 8004 | Mercedes            | EQS                 |   4 | Sedan   | Electric | Active     |
        // | 8005 | VW                  | Multivan Life       |   8 | Van     | Gasoline | Active     |
        // | 8006 | Tesla               | Model 3             |   4 | Sedan   | Electric | Active     |
        // | 8007 | Tesla               | Model S             |   4 | Sedan   | Electric | Serviced   |
        // +------+---------------------+---------------------+-----+---------+----------+------------+
        final TableFormatter tf3 = new TableFormatter("| %-5s", "| %-20s", "| %-20s", "| %3s ", "| %-8s", "| %-9s", "| %-10s |")
            .line()
            .row("ID", "MAKE", "MODEL", "SEA", "CATEG", "POWER", "STATUS")
            .line();
        // 
        freeriderDB.findAll( (id, make, model, seats, category, power, status) ->
            // 
            tf3.row(id, make, model, seats, category, power, status));
        // 
        tf3.line();
        System.out.println("findAll(VehicleRowPrinter rm):\n" + tf3.get().toString());

        // +-------+------+------+------------------+------------------+-----------------+----------------+------------+
        // | ID    |CUS_ID|VEH_ID| BEGIN            | END              | PICKUP          | DROP-OFF       | STATUS     |
        // +-------+------+------+------------------+------------------+-----------------+----------------+------------+
        // | 145373|  1001|  8002| 2025-07-04 20:00:| 2025-07-04 23:00:| Berlin Wedding  | Hamburg        | Inquired   |
        // | 201235|  1000|  8002| 2025-07-20 10:00:| 2025-07-20 20:00:| Berlin Wedding  | Berlin Wedding | Booked     |
        // | 351682|  1002|  8001| 2025-06-11 08:00:| 2025-06-11 20:00:| Berlin Wedding  | Hamburg        | Inquired   |
        // | 382565|  1000|  8006| 2025-07-18 18:00:| 2025-07-07 18:10:| Berlin Wedding  | Hamburg        | Inquired   |
        // | 682351|  1002|  8003| 2025-07-18 09:00:| 2025-07-07 18:00:| Potsdam         | Teltow         | Inquired   |
        // +-------+------+------+------------------+------------------+-----------------+----------------+------------+
        final TableFormatter tf4 = new TableFormatter("| %-6s", "|%6s", "|%6s", "| %-17s", "| %-17s", "| %-15s ", "| %-15s", "| %-10s |")
            .line()
            .row("ID", "CUS_ID", "VEH_ID", "BEGIN", "END", "PICKUP", "DROP-OFF", "STATUS")
            .line();
        // 
        freeriderDB.findAll( (id, cust_id, veh_id, begin, end, pickup, drop, status) ->
            // 
            tf4.row(id, cust_id, veh_id, begin, end, pickup, drop, status));
        // 
        tf4.line();
        System.out.println("void findAll(ReservationRowPrinter rm):\n" + tf4.get().toString());
    }
}