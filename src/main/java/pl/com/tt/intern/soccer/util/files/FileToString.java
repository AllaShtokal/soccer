package pl.com.tt.intern.soccer.util.files;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@NoArgsConstructor(access = PRIVATE)
public final class FileToString {

    public static String readFileToString(String path) {
        StringBuilder content = new StringBuilder();
        try (Stream<String> lines = Files.lines(Paths.get(path), UTF_8)) {
            lines.forEach(content::append);
        } catch (IOException e) {
            log.error(FileToString.class.getName() + ": readFileToString caught exception while trying to read lines from file");
        }
        return content.toString();
    }
}