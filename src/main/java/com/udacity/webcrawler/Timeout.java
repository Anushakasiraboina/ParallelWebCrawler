package com.udacity.webcrawler;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A binding annotation for the maximum amount of time the web crawler is allowed to take.
 *
 * <p>The value bound to this annotation is a Java {@link java.time.Duration} created from the
 * {@code "timeoutSeconds"} option in the crawler configuration JSON.
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface Timeout {
}
