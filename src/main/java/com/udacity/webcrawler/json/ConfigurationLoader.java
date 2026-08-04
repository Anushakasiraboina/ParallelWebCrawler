package com.udacity.webcrawler.json;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
public final class ConfigurationLoader {
  private final Path path;
  public ConfigurationLoader(Path path) {
    this.path = Objects.requireNonNull(path);
  }
  public CrawlerConfiguration load() {
    try (Reader reader = Files.newBufferedReader(path)) {
      return read(reader);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
  public static CrawlerConfiguration read(Reader reader) {
    Objects.requireNonNull(reader);
    try {
      ObjectMapper mapper = new ObjectMapper();
      mapper.disable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
      return mapper.readValue(reader, CrawlerConfiguration.class);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}


