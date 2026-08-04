package com.udacity.webcrawler;

import com.udacity.webcrawler.json.CrawlResult;
import com.udacity.webcrawler.parser.PageParserFactory;

import javax.inject.Inject;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ForkJoinPool;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class ParallelWebCrawler implements WebCrawler {

  private final Clock clock;
  private final Duration timeout;
  private final int popularWordCount;
  private final ForkJoinPool pool;
  private final PageParserFactory parserFactory;
  private final int maxDepth;
  private final List<Pattern> ignoredUrls;

  @Inject
  public ParallelWebCrawler(
          Clock clock,
          PageParserFactory parserFactory,
          @Timeout Duration timeout,
          @PopularWordCount int popularWordCount,
          @MaxDepth int maxDepth,
          @IgnoredUrls List<Pattern> ignoredUrls,
          @TargetParallelism int threadCount) {

    this.clock = clock;
    this.parserFactory = parserFactory;
    this.timeout = timeout;
    this.popularWordCount = popularWordCount;
    this.maxDepth = maxDepth;
    this.ignoredUrls = ignoredUrls;
    this.pool = new ForkJoinPool(Math.min(threadCount, getMaxParallelism()));
  }

  @Override
  public CrawlResult crawl(List<String> startingUrls) {

    // ✅ REQUIRED for zeroMaxDepth test
    if (startingUrls == null || startingUrls.isEmpty() || maxDepth == 0) {
      return new CrawlResult.Builder()
              .setUrlsVisited(0)
              .setWordCounts(Map.of())
              .build();
    }


    Instant deadline = clock.instant().plus(timeout);
    Map<String, Integer> counts = new ConcurrentHashMap<>();
    Set<String> visitedUrls = new ConcurrentSkipListSet<>();

    List<CrawlTask> tasks = startingUrls.stream()
            .map(url -> new CrawlTask(
                    url,
                    maxDepth,
                    deadline,
                    counts,
                    visitedUrls))
            .toList();

    tasks.forEach(pool::invoke);

    return new CrawlResult.Builder()
            .setUrlsVisited(visitedUrls.size())
            .setWordCounts(WordCounts.sort(counts, popularWordCount))
            .build();
  }

  @Override
  public int getMaxParallelism() {
    return Runtime.getRuntime().availableProcessors();
  }

  private final class CrawlTask extends java.util.concurrent.RecursiveAction {

    private final String url;
    private final int depth;
    private final Instant deadline;
    private final Map<String, Integer> counts;
    private final Set<String> visitedUrls;

    CrawlTask(
            String url,
            int depth,
            Instant deadline,
            Map<String, Integer> counts,
            Set<String> visitedUrls) {

      this.url = url;
      this.depth = depth;
      this.deadline = deadline;
      this.counts = counts;
      this.visitedUrls = visitedUrls;
    }

    @Override
    protected void compute() {

      if (depth <= 0 || clock.instant().isAfter(deadline)) {
        return;
      }

      for (Pattern pattern : ignoredUrls) {
        if (pattern.matcher(url).matches()) {
          return;
        }
      }

      if (!visitedUrls.add(url)) {
        return;
      }

      var result = parserFactory.get(url).parse();

      result.getWordCounts()
              .forEach((word, count) ->
                      counts.merge(word, count, Integer::sum));

      List<CrawlTask> subtasks = result.getLinks().stream()
              .map(link ->
                      new CrawlTask(
                              link,
                              depth - 1,
                              deadline,
                              counts,
                              visitedUrls))
              .collect(Collectors.toList());

      invokeAll(subtasks);
    }
  }
}
