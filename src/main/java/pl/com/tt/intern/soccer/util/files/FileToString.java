package pl.com.tt.intern.soccer.util.files;

import lombok.NoArgsConstructor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class FileToString {

    public static String readFileToString(String path) throws IOException {
        StringBuilder content = new StringBuilder();
        Files.lines(Paths.get(path), StandardCharsets.UTF_8)
                .forEach(content::append);
        return content.toString();
    }
}

