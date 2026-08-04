package com.udacity.webcrawler.profiler;

import javax.inject.Inject;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Proxy;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.Objects;

import static java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME;

final class ProfilerImpl implements Profiler {

  private final Clock clock;
  private final ProfilingState state = new ProfilingState();
  private final ZonedDateTime startTime;

  @Inject
  ProfilerImpl(Clock clock) {
    this.clock = Objects.requireNonNull(clock);
    this.startTime = ZonedDateTime.now(clock);
  }

  @Override
  public <T> T wrap(Class<T> klass, T delegate) {
    Objects.requireNonNull(klass);
    Objects.requireNonNull(delegate);

    boolean hasProfiledMethod = false;
    for (var method : klass.getMethods()) { // use getMethods() to include inherited interface methods
      if (method.isAnnotationPresent(Profiled.class)) {
        hasProfiledMethod = true;
        break;
      }
    }
    if (!hasProfiledMethod) {
      throw new IllegalArgumentException("No @Profiled methods in interface");
    }

    //noinspection unchecked
    return (T) Proxy.newProxyInstance(
            klass.getClassLoader(),
            new Class[]{klass},
            new ProfilingMethodInterceptor(clock, delegate, state)
    );
  }

  @Override
  public void writeData(Path path) throws IOException {
    Objects.requireNonNull(path);
    try (Writer writer = Files.newBufferedWriter(path)) {
      writeData(writer);
    }
  }

  @Override
  public void writeData(Writer writer) throws IOException {
    Objects.requireNonNull(writer);
    writer.write("Run at " + RFC_1123_DATE_TIME.format(startTime));
    writer.write(System.lineSeparator());
    state.write(writer); // make sure ProfilingState.write() is implemented correctly
    writer.write(System.lineSeparator());
  }
}
