package com.udacity.webcrawler.json;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
public final class CrawlResultWriter {
  private final CrawlResult result;
  private static final ObjectMapper MAPPER = new ObjectMapper();
  public CrawlResultWriter(CrawlResult result) {
    this.result = Objects.requireNonNull(result);
  }
  public void write(Path path) {
    Objects.requireNonNull(path);
    try {
      Path parent = path.getParent();
      if (parent != null) {
        Files.createDirectories(parent);
      }
      try (Writer writer = Files.newBufferedWriter(
              path,
              StandardOpenOption.CREATE,
              StandardOpenOption.TRUNCATE_EXISTING)) {
        write(writer);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  public void write(Writer writer) {
    Objects.requireNonNull(writer);
    try {
      JsonGenerator generator = MAPPER.getFactory().createGenerator(writer);
      MAPPER.writeValue(generator, result);
      generator.flush();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
