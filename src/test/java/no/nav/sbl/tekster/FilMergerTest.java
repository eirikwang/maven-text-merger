package no.nav.sbl.tekster;

import org.junit.Test;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class FilMergerTest {

    @Test
    public void skalLeseFiler() throws URISyntaxException {
        Map<String, List<FilMerger.Tekst>> tekster = FilMerger.mergeFiler(Paths.get(ClassLoader.getSystemResource("tekster").toURI()).toFile().getAbsolutePath(), "");
        tekster.entrySet().stream().forEach(stringListEntry -> System.out.println(stringListEntry.getKey() + " " + stringListEntry.getValue()));

    }
}