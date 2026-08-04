package com.udacity.webcrawler.profiler;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Helper class that records method performance data from the method interceptor.
 */
final class ProfilingState {

  private final Map<String, Duration> data = new ConcurrentHashMap<>();

  /**
   * Records the given method invocation data.
   *
   * @param callingClass the Java class of the object that called the method.
   * @param method       the method that was called.
   * @param elapsed      the amount of time that passed while the method was called.
   */
  void record(Class<?> callingClass, Method method, Duration elapsed) {
    Objects.requireNonNull(callingClass);
    Objects.requireNonNull(method);
    Objects.requireNonNull(elapsed);
    if (elapsed.isNegative()) {
      throw new IllegalArgumentException("Negative elapsed time is not allowed");
    }

    String key = formatMethodCall(callingClass, method);
    data.merge(key, elapsed, Duration::plus);
  }

  /**
   * Writes the method invocation data to the given {@link Writer}.
   *
   * @param writer the writer to which profiling data is written
   * @throws IOException if an I/O error occurs
   */
  void write(Writer writer) throws IOException {
    List<String> entries = data.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .map(e -> e.getKey() + " took " + formatDuration(e.getValue()) + System.lineSeparator())
            .toList();

    for (String entry : entries) {
      writer.write(entry);
    }
  }

  /** Formats a method call for output. */
  private static String formatMethodCall(Class<?> callingClass, Method method) {
    return callingClass.getName() + "#" + method.getName();
  }

  /** Formats the duration in minutes, seconds, milliseconds. */
  private static String formatDuration(Duration duration) {
    return String.format("%dm %ds %dms",
            duration.toMinutes(),
            duration.toSecondsPart(),
            duration.toMillisPart());
  }
}
