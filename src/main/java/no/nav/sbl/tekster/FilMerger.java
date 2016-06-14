package no.nav.sbl.tekster;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;

public class FilMerger {
    private static final Pattern FILEPATTERN = Pattern.compile("(.*?)(_([a-zA-Z]{2}_?[a-zA-Z]{0,2}))?\\.([a-z]*)$");

    public static Map<String, List<Tekst>> mergeFiler(String directory, String baseKey) {
        return mapDir(FileSystems.getDefault().getPath(directory), baseKey != null ? baseKey : "").stream().collect(Collectors.groupingBy(pair -> pair.locale));
    }

    static List<Tekst> mapDir(Path path, final String baseKey) {
        Stream<Path> files;
        try {
            files = Files.list(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return files.map(file -> mapFileOrDir(file, baseKey)).flatMap(Collection::stream).collect(Collectors.toList());
    }

    static List<Tekst> mapFileOrDir(Path path, String baseKey) {
        if (path.toFile().isDirectory()) {
            return mapDir(path, (baseKey != null && baseKey.length() != 0? ".": "") + path.toFile().getName());
        }
        return singletonList(mapFile(path, baseKey));

    }

    static Tekst mapFile(Path file, String baseKey) {
        List<String> strings = null;
        try {
            strings = Files.readAllLines(file, Charset.forName("UTF-8"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String fileName = file.toFile().getName();
        Matcher matcher = FILEPATTERN.matcher(fileName);
        return matcher.find() ? Tekst.of(baseKey, matcher.group(1), matcher.group(3), matcher.group(4), strings.stream().collect(Collectors.joining("\\n"))) : null;
    }

    public static void main(String[] args) {
        Pattern pattern = Pattern.compile("(.*?)(_([a-zA-Z]{2}_?[a-zA-Z]{0,2}))?\\.([a-z]*)$");
        Matcher matcher = pattern.matcher("test.test.test_nb_NO.txt");
        System.out.println(matcher.find());
        System.out.println(matcher.group(0));
        System.out.println(matcher.group(1));
        System.out.println(matcher.group(2));
        System.out.println(matcher.group(3));
        System.out.println(matcher.group(4));
    }

    public static class Tekst {
        public String key, value, locale, type;

        public static Tekst of(String baseKey, String key, String locale, String type, String value) {
            Tekst pair = new Tekst();
            pair.key = (baseKey != null && baseKey.length() != 0 ? baseKey + "." : "") + key;
            pair.locale = locale;
            pair.type = type;
            pair.value = value;
            return pair;
        }

        @Override
        public String toString() {
            return "Tekst{" +
                    "key='" + key + '\'' +
                    ", value='" + value + '\'' +
                    ", locale='" + locale + '\'' +
                    ", type='" + type + '\'' +
                    '}';
        }
    }
}
