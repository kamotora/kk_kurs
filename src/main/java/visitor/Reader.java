package visitor;

import lombok.SneakyThrows;

import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Collectors;

public class Reader {
    @SneakyThrows
    public String fromFile(String fileName) {
        Path path = Optional.ofNullable(getClass().getClassLoader())
                .map(loader -> loader.getResource(fileName))
                .map(Reader::toURI)
                .map(Paths::get)
                .orElseThrow(() -> new FileNotFoundException(fileName));

        return Files.lines(path).collect(Collectors.joining("\n"));
    }

    @SneakyThrows
    private static URI toURI(URL url){
        return url.toURI();
    }
}
