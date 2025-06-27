package freerider.application;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;


/**
 * Class that implements the {@link Runner} interface with {@code run(String[] args)}
 * method that executes tasks of the <i>C12</i> assignment.
 *
 * @version <code style=color:green>{@value application.package_info#Version}</code>
 * @author <code style=color:blue>{@value application.package_info#Author}</code>
 */
public class TableFormatter {


    /**
     * Format specifiers for each column.
     */
    private final List<String> fmts;

    /**
     * Width of each column.
     */
    private final List<Integer> widths;

    /**
     * Collect formatted rows.
     */
    private final StringBuilder sb;


    /**
     * Constructor with String.format(fmt) specifiers for each column.
     * 
     * @param fmtArgs String.format(fmt) specifiers for each column.
     */
    public TableFormatter(String... fmtArgs) {
        this((StringBuilder)null, fmtArgs);
    }

    /**
     * Constructor with external collector of table rows and String.format(fmt)
     * specifiers for each column.
     * 
     * @param sb external collector for table rows.
     * @param fmtArgs String.format(fmt) specifiers for each column.
     */
    public TableFormatter(StringBuilder sb, String... fmtArgs) {
        this.sb = sb != null? sb : new StringBuilder();
        this.fmts = Arrays.stream(fmtArgs).toList();
        this.widths = fmts.stream().map(fmt -> String.format(fmt, "").length()).toList();
    }

    /**
     * Add row to table. Each cell is formatted according to the column fmt specifier.
     * 
     * @param cells variable array of cells.
     * @return chainable self-reference.
     */
    public TableFormatter row(String... cells) {
        IntStream.range(0, Math.min(fmts.size(), cells.length)).forEach(i -> {
            sb.append(fillCell(i, cells[i], t -> {
                String fmt = fmts.get(i);
                int i1 = fmt.indexOf('%');  // offset width by format chars, e.g. '%-20s'
                int i2 = Math.max(fmt.indexOf('s'), fmt.indexOf('d'));  // end '%s', '%d'
                int offset = fmt.length() - (i2 - i1) -1;
                // cut cell text to effective column width
                t = t.substring(0, Math.min(t.length(), widths.get(i) - offset));
                return String.format(fmt, t);
            }));
        });
        return this.endRow();
    }

    /**
     * Add line comprised of segments for each column to the table.
     * Segments are drawn based on segment spefifiers with:
     * <pre>
     * seg: null    - empty or blank segment
     *      ""      - segment filled with default character: "-"
     *      "="     - segment is filled with provided character.
     * </pre>
     * 
     * @param segs variable array of segment specifiers.
     * @return chainable self-reference.
     */
    public TableFormatter line(String... segs) {
        if(segs.length==0) {    // print full line when segs is empty
            String[] args = fmts.stream().map(f -> "").toArray(String[]::new);
            return line(args);  // invoke recursively with ""-args
        }
        IntStream.range(0, Math.min(fmts.size(), segs.length)).forEach(i -> {
            sb.append(fillCell(i, segs[i], s -> {
                s = s.length() > 0? s.substring(0, 1) : "-"; // filler char
                return String.format(fmts.get(i), "")
                        .replaceAll("[^\\|]", s).replaceAll("[\\|]", "+");
            }));
        });
        return this.endRow();
    }

    /**
     * Getter to collected table content.
     * 
     * @return table content.
     */
    public StringBuilder get() { return sb; }

    /*
        * private helper methods.
        */

    private String fillCell(int i, String text, Function<String, String> cellFiller) {
        return text != null? cellFiller.apply(text) : " ".repeat(widths.get(i));
    }

    private TableFormatter endRow() { sb.append("\n"); return this; }
}