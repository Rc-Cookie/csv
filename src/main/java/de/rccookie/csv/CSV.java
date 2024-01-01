package de.rccookie.csv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.rccookie.util.Arguments;
import de.rccookie.util.IterableIterator;
import de.rccookie.util.ListStream;
import de.rccookie.util.RowMajorTable;
import de.rccookie.util.StepIterator;
import de.rccookie.util.Table;
import de.rccookie.util.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Utility class for reading and writing CSV (comma separated values) formatted text and files.
 */
public final class CSV {

    private CSV() { }


    public static final int ALWAYS_ENQUOTE = 1 << 1;
    public static final int ONLY_LINE_FEED = 1 << 2;



    public static String toString(Table<?,?> table) {
        return toString(table, 0);
    }

    public static String toString(Table<?,?> table, long options) {
        return toString(table, ',', options);
    }

    public static String toString(Table<?,?> table, char delimiter) {
        return toString(table, delimiter, 0);
    }

    public static String toString(Table<?,?> table, char delimiter, long options) {
        StringBuilder str = new StringBuilder();
        write(table, str, delimiter, options);
        return str.toString();
    }


    public static void write(Table<?,?> table, String file) {
        write(table, file, 0);
    }

    public static void write(Table<?,?> table, File file) {
        write(table, file, 0);
    }

    public static void write(Table<?,?> table, Path file) {
        write(table, file, 0);
    }

    public static void write(Table<?,?> table, OutputStream out) {
        write(table, out, 0);
    }

    public static void write(Table<?,?> table, Appendable out) {
        write(table, out, 0);
    }


    public static void write(Table<?,?> table, String file, long options) {
        try {
            write(table, new FileWriter(file), options);
        } catch(IOException e) {
            throw Utils.rethrow(e);
        }
    }

    public static void write(Table<?,?> table, File file, long options) {
        try {
            write(table, new FileWriter(file), options);
        } catch(IOException e) {
            throw Utils.rethrow(e);
        }
    }

    public static void write(Table<?,?> table, Path file, long options) {
        try {
            write(table, Files.newOutputStream(file), options);
        } catch(IOException e) {
            throw Utils.rethrow(e);
        }
    }

    public static void write(Table<?,?> table, OutputStream out, long options) {
        write(table, new OutputStreamWriter(out), options);
    }

    public static void write(Table<?,?> table, Appendable out, long options) {
        write(table, out, ',', options);
    }


    public static void write(Table<?,?> table, String file, char delimiter) {
        write(table, file, delimiter, 0);
    }

    public static void write(Table<?,?> table, File file, char delimiter) {
        write(table, file, delimiter, 0);
    }

    public static void write(Table<?,?> table, Path file, char delimiter) {
        write(table, file, delimiter, 0);
    }

    public static void write(Table<?,?> table, OutputStream out, char delimiter) {
        write(table, out, delimiter, 0);
    }

    public static void write(Table<?,?> table, Appendable out, char delimiter) {
        write(table, out, delimiter, 0);
    }


    public static void write(Table<?,?> table, String file, char delimiter, long options) {
        try {
            write(table, new FileWriter(file), delimiter, options);
        } catch(IOException e) {
            throw Utils.rethrow(e);
        }
    }

    public static void write(Table<?,?> table, File file, char delimiter, long options) {
        try {
            write(table, new FileWriter(file), delimiter, options);
        } catch(IOException e) {
            throw Utils.rethrow(e);
        }
    }

    public static void write(Table<?,?> table, Path file, char delimiter, long options) {
        try {
            write(table, Files.newOutputStream(file), delimiter, options);
        } catch(IOException e) {
            throw Utils.rethrow(e);
        }
    }

    public static void write(Table<?,?> table, OutputStream out, char delimiter, long options) {
        write(table, new OutputStreamWriter(out), delimiter, options);
    }

    public static void write(Table<?,?> table, Appendable out, char delimiter, long options) {
        write(table, out, delimiter, '"', false, options);
    }

    @SuppressWarnings("SameParameterValue")
    static void write(Table<?, ?> table, Appendable out, char delimiter, int quotes, boolean backslashEscapes, long options) {
        try {
            boolean alwaysEnquote = (options & ALWAYS_ENQUOTE) != 0;
            boolean onlyLineFeed = (options & ONLY_LINE_FEED) != 0;

            int width = table.columnCount();
            boolean rowsAreLabeled = table.rowsAreLabeled();

            if(table.columnsAreLabeled()) {
                if(rowsAreLabeled && width != 0)
                    out.append(delimiter);
                writeRow(table.columnLabels(), width, out, delimiter, quotes, backslashEscapes, alwaysEnquote, onlyLineFeed);
            }
            for(Table.Vector<?,?> row : table.rows()) {
                if(rowsAreLabeled) {
                    writeValue(row.label(), out, delimiter, quotes, backslashEscapes, alwaysEnquote);
                    if(width != 0)
                        out.append(delimiter);
                }
                writeRow(row, width, out, delimiter, quotes, backslashEscapes, alwaysEnquote, onlyLineFeed);
            }
        } catch(IOException e) {
            throw Utils.rethrow(e);
        }
    }

    private static void writeRow(Iterable<?> row, int width, Appendable out, char delimiter, int quotes, boolean backslashEscapes, boolean alwaysEnquote, boolean onlyLineFeed) throws IOException {
        if(width == 0) {
            if(!onlyLineFeed)
                out.append('\r');
            out.append('\n');
            return;
        }
        int i = 0;
        for(Object value : row) {
            writeValue(value, out, delimiter, quotes, backslashEscapes, alwaysEnquote);
            if(++i == width) {
                if(!onlyLineFeed)
                    out.append('\r');
                out.append('\n');
            }
            else out.append(delimiter);
        }
    }

    private static void writeValue(Object value, Appendable out, char delimiter, int quotes, boolean backslashEscapes, boolean alwaysEnquote) throws IOException {
        if(value == null) {
            if(alwaysEnquote)
                out.append(delimiter).append(delimiter);
            return;
        }
        String str = value+"";
        if(backslashEscapes) {
            if(quotes >= 0 && (alwaysEnquote || str.indexOf(quotes) != -1 || str.indexOf(delimiter) != -1)) {
                out.append((char) quotes);
                for(int i=0; i<str.length(); i++) {
                    char c = str.charAt(i);
                    switch(c) {
                        case '\r':
                            out.append('\\').append('r');
                            break;
                        case '\n':
                            out.append('\\').append('n');
                            break;
                        case '\t':
                            out.append('\\').append('t');
                            break;
                        default:
                            if(c == quotes)
                                out.append((char) quotes);
                            out.append(c);
                    }
                }
                out.append((char) quotes);
            }
            else {
                for(int i=0; i<str.length(); i++) {
                    char c = str.charAt(i);
                    switch(c) {
                        case '\r':
                            out.append('\\').append('r');
                            break;
                        case '\n':
                            out.append('\\').append('n');
                            break;
                        case '\t':
                            out.append('\\').append('t');
                            break;
                        default:
                            if(c == delimiter)
                                out.append('\\');
                            out.append(c);
                    }
                }
            }
        }
        else if(quotes >= 0 && (alwaysEnquote || str.indexOf(quotes) != -1 || str.indexOf(delimiter) != -1 || str.indexOf('\r') != -1 || str.indexOf('\n') != -1)) {
            out.append((char) quotes);
            for(int i=0; i<str.length(); i++) {
                char c = str.charAt(i);
                out.append(c);
                if(c == quotes)
                    out.append((char) quotes);
            }
            out.append((char) quotes);
        }
        else out.append(str);
    }



    public static <L> Table<L, String> load(String file, Collection<? extends L> labels) {
        try {
            return parse(new FileReader(file), ',', labels);
        } catch(FileNotFoundException e) {
            throw Utils.rethrow(e);
        }
    }

    public static <L> Table<L, String> parse(String csv, Collection<? extends L> labels) {
        return parse(new StringReader(csv), ',', labels);
    }

    public static <L> Table<L, String> parse(File file, Collection<? extends L> labels) {
        try {
            return parse(new FileReader(file), ',', labels);
        } catch(FileNotFoundException e) {
            throw Utils.rethrow(e);
        }
    }

    public static <L> Table<L, String> parse(Path file, Collection<? extends L> labels) {
        try {
            return parse(Files.newInputStream(file), labels);
        } catch(IOException e) {
            throw Utils.rethrow(e);
        }
    }

    public static <L> Table<L, String> parse(InputStream csv, Collection<? extends L> labels) {
        return parse(new InputStreamReader(csv), ',', labels);
    }

    public static <L> Table<L, String> parse(Reader in, Collection<? extends L> labels) {
        return parse(in, ',', labels);
    }


    public static <L> Table<L, String> load(String file, char delimiter, Collection<? extends L> labels) {
        try {
            return parse(new FileReader(file), delimiter, labels);
        } catch(FileNotFoundException e) {
            throw Utils.rethrow(e);
        }
    }

    public static <L> Table<L, String> parse(String csv, char delimiter, Collection<? extends L> labels) {
        return parse(new StringReader(csv), delimiter, labels);
    }

    public static <L> Table<L, String> parse(File file, char delimiter, Collection<? extends L> labels) {
        try {
            return parse(new FileReader(file), delimiter, labels);
        } catch(FileNotFoundException e) {
            throw Utils.rethrow(e);
        }
    }

    public static <L> Table<L, String> parse(Path file, char delimiter, Collection<? extends L> labels) {
        try {
            return parse(Files.newInputStream(file), delimiter, labels);
        } catch(IOException e) {
            throw Utils.rethrow(e);
        }
    }

    public static <L> Table<L, String> parse(InputStream csv, char delimiter, Collection<? extends L> labels) {
        return parse(new InputStreamReader(csv), delimiter, labels);
    }

    public static <L> Table<L, String> parse(Reader in, char delimiter, Collection<? extends L> labels) {
        return toTable(parseRaw(in, delimiter), labels);
    }

    static <L> Table<L, String> toTable(List<List<String>> data, Collection<? extends L> labels) {
        Table<L,String> table = new RowMajorTable<>("", Arguments.checkNull(labels, "labels"));
        if(!data.isEmpty())
            table.addRowsOrdered(data.subList(1, data.size()));
        return table;
    }


    public static Table<String, String> load(String file, boolean header) {
        try {
            return parse(new FileReader(file), ',', header);
        } catch(FileNotFoundException e) {
            throw Utils.rethrow(e);
        }
    }

    public static Table<String, String> parse(String csv, boolean header) {
        return parse(new StringReader(csv), ',', header);
    }

    public static Table<String, String> parse(File file, boolean header) {
        try {
            return parse(new FileReader(file), ',', header);
        } catch(FileNotFoundException e) {
            throw Utils.rethrow(e);
        }
    }

    public static Table<String, String> parse(Path file, boolean header) {
        try {
            return parse(Files.newInputStream(file), header);
        } catch(IOException e) {
            throw Utils.rethrow(e);
        }
    }

    public static Table<String, String> parse(InputStream csv, boolean header) {
        return parse(new InputStreamReader(csv), ',', header);
    }

    public static Table<String, String> parse(Reader in, boolean header) {
        return parse(in, ',', header);
    }


    public static Table<String, String> load(String file, char delimiter, boolean header) {
        try {
            return parse(new FileReader(file), delimiter, header);
        } catch(FileNotFoundException e) {
            throw Utils.rethrow(e);
        }
    }

    public static Table<String, String> parse(String csv, char delimiter, boolean header) {
        return parse(new StringReader(csv), delimiter, header);
    }

    public static Table<String, String> parse(File file, char delimiter, boolean header) {
        try {
            return parse(new FileReader(file), delimiter, header);
        } catch(FileNotFoundException e) {
            throw Utils.rethrow(e);
        }
    }

    public static Table<String, String> parse(Path file, char delimiter, boolean header) {
        try {
            return parse(Files.newInputStream(file), delimiter, header);
        } catch(IOException e) {
            throw Utils.rethrow(e);
        }
    }

    public static Table<String, String> parse(InputStream csv, char delimiter, boolean header) {
        return parse(new InputStreamReader(csv), delimiter, header);
    }

    public static Table<String, String> parse(Reader in, char delimiter, boolean header) {
        return toTable(parseRaw(in, delimiter), header);
    }

    static Table<String, String> toTable(List<List<String>> data, boolean header) {
        Table<String, String> table = new RowMajorTable<>("");
        if(data.isEmpty()) return table;
        if(header) {
            table.setColumnLabels(data.get(0));
            table.addRowsOrdered(data.subList(1, data.size()));
        }
        else table.addRowsOrdered(data);
        return table;
    }

    public static ListStream<List<String>> parseRaw(@NotNull Reader in) {
        return parseRaw(in, ',');
    }

    public static ListStream<List<String>> parseRaw(@NotNull Reader in, char delimiter) {
        return parseFormat(in, delimiter, '"', null);
    }

    public static ListStream<List<String>> parseFormat(@NotNull Reader in, char delimiter, @Nullable Character quotes, @Nullable Character backslash) {
        return ListStream.of(parse0(
                Arguments.checkNull(in, "in"),
                delimiter,
                quotes != null ? quotes : -2,
                backslash != null ? backslash : -2
        ));
    }

    private static IterableIterator<List<String>> parse0(Reader in, char delimiter, int quotes, int backslash) {
        if(quotes == delimiter)
            throw new IllegalArgumentException("Quotes cannot be the same character as the delimiter");
        if(backslash == delimiter)
            throw new IllegalArgumentException("Backslash cannot be the same character as the delimiter");
        if(quotes != -2 && quotes == backslash)
            throw new IllegalArgumentException("Quotes cannot be the same character as backslash");
        return new StepIterator<>() {
            int c;
            {
                try {
                    c = in.read();
                } catch(IOException e) {
                    throw Utils.rethrow(e);
                }
            }
            final StringBuilder str = new StringBuilder();
            @Override
            protected List<String> getNext() {
                try {
                    if(c == -1) {
                        in.close();
                        return null;
                    }
                    List<String> record = new ArrayList<>();
                    while(c != -1) {
                        str.setLength(0);
                        if(c == quotes) {
                            while((c = in.read()) != -1 && (c != quotes || (c = in.read()) == quotes)) {
                                if(c == backslash) {
                                    c = in.read();
                                    switch(c) {
                                        case 'r':
                                            c = '\r';
                                            break;
                                        case 'n':
                                            c = '\n';
                                            break;
                                        case 't':
                                            c = '\t';
                                            break;
                                        default:
                                            if(c != delimiter && c != quotes)
                                                str.append(backslash);
                                    }
                                }
                                str.append((char) c);
                            }
                        }
                        while(c != -1 && c != delimiter && c != '\r' && c != '\n') {
                            str.append((char) c);
                            c = in.read();
                        }
                        if(c == '\r') {
                            c = in.read();
                            if(c == '\n')
                                c = in.read();
                            record.add(str.toString());
                            return record;
                        }
                        else if(c == '\n') {
                            c = in.read();
                            record.add(str.toString());
                            return record;
                        }
                        else c = in.read();
                        record.add(str.toString());
                    }
                    return record;
                } catch(IOException e) {
                    throw Utils.rethrow(e);
                }
            }
        };
    }
}
