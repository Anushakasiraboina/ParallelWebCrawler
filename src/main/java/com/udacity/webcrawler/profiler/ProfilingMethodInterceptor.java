package com.udacity.webcrawler.profiler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

final class ProfilingMethodInterceptor implements InvocationHandler {

  private final Clock clock;
  private final Object delegate;
  private final ProfilingState state;

  ProfilingMethodInterceptor(Clock clock, Object delegate, ProfilingState state) {
    this.clock = Objects.requireNonNull(clock);
    this.delegate = Objects.requireNonNull(delegate);
    this.state = Objects.requireNonNull(state);
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    // Don't profile Object's equals, hashCode, toString, etc.
    if (method.getDeclaringClass() == Object.class) {
      return method.invoke(delegate, args);
    }

    boolean profiled = method.isAnnotationPresent(Profiled.class);
    Instant start = profiled ? clock.instant() : null;

    try {
      // Invoke the actual method
      Object result = method.invoke(delegate, args);
      if (profiled && start != null) {
        state.record(delegate.getClass(), method, Duration.between(start, clock.instant()));
      }
      return result;
    } catch (InvocationTargetException e) {
      // Unwrap the underlying exception
      Throwable cause = e.getCause() != null ? e.getCause() : e;
      if (profiled && start != null) {
        state.record(delegate.getClass(), method, Duration.between(start, clock.instant()));
      }
      throw cause;
    }
  }
}
