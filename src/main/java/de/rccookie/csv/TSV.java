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
import java.util.Collection;
import java.util.List;

import de.rccookie.util.ListStream;
import de.rccookie.util.Table;
import de.rccookie.util.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Utility class for reading and writing TSV (tab separated values) formatted text and files.
 */
public final class TSV {

    private TSV() { }


    public static final int ONLY_LINE_FEED = CSV.ONLY_LINE_FEED;




    public static String toString(Table<?,?> table) {
        return toString(table, 0);
    }

    public static String toString(Table<?,?> table, long options) {
        return toString(table, '\t', options);
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
        write(table, out, '\t', options);
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
        CSV.write(table, out, delimiter, -2, true, options);
    }



    public static <L> Table<L, String> load(String file, Collection<? extends L> labels) {
        try {
            return parse(new FileReader(file), '\t', labels);
        } catch(FileNotFoundException e) {
            throw Utils.rethrow(e);
        }
    }

    public static <L> Table<L, String> parse(String csv, Collection<? extends L> labels) {
        return parse(new StringReader(csv), '\t', labels);
    }

    public static <L> Table<L, String> parse(File file, Collection<? extends L> labels) {
        try {
            return parse(new FileReader(file), '\t', labels);
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
        return parse(new InputStreamReader(csv), '\t', labels);
    }

    public static <L> Table<L, String> parse(Reader in, Collection<? extends L> labels) {
        return parse(in, '\t', labels);
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
        return CSV.toTable(parseRaw(in, delimiter), labels);
    }


    public static Table<String, String> load(String file, boolean header) {
        try {
            return parse(new FileReader(file), '\t', header);
        } catch(FileNotFoundException e) {
            throw Utils.rethrow(e);
        }
    }

    public static Table<String, String> parse(String csv, boolean header) {
        return parse(new StringReader(csv), '\t', header);
    }

    public static Table<String, String> parse(File file, boolean header) {
        try {
            return parse(new FileReader(file), '\t', header);
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
        return parse(new InputStreamReader(csv), '\t', header);
    }

    public static Table<String, String> parse(Reader in, boolean header) {
        return parse(in, '\t', header);
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
        return CSV.toTable(parseRaw(in, delimiter), header);
    }

    public static ListStream<List<String>> parseRaw(@NotNull Reader in) {
        return parseRaw(in, '\t');
    }

    public static ListStream<List<String>> parseRaw(@NotNull Reader in, char delimiter) {
        return parseFormat(in, delimiter, null, '\\');
    }

    public static ListStream<List<String>> parseFormat(@NotNull Reader in, char delimiter, @Nullable Character quotes, @Nullable Character backslash) {
        return CSV.parseFormat(in, delimiter, quotes, backslash);
    }
}
