# Parallel Web Crawler

A multithreaded web crawler built in **Java 17** that improves the performance of a legacy sequential crawler using the **Fork/Join Framework** and thread-safe concurrent programming techniques.



## Features

- 🚀 Parallel web crawling using `ForkJoinPool`
- 📄 JSON configuration loading with Jackson
- 📝 JSON crawl result generation
- 🔄 Automatic selection between sequential and parallel crawler
- ⚡ Thread-safe URL tracking and word counting
- 📊 Functional word count sorting using Java Streams
- ⏱️ Performance profiling with Dynamic Proxies
- 💉 Dependency Injection using Google Guice
- ✅ Comprehensive unit testing with JUnit 5

---

## Technologies Used

- Java 17
- Maven
- Jackson
- Google Guice
- ForkJoin Framework
- Java Streams API
- JUnit 5
- Jsoup

---

## Project Structure

```
src/
└── main/
    └── java/
        └── com/udacity/webcrawler/
            ├── ParallelWebCrawler.java
            ├── SequentialWebCrawler.java
            ├── WordCounts.java
            ├── json/
            │   ├── ConfigurationLoader.java
            │   └── CrawlResultWriter.java
            ├── profiler/
            │   ├── ProfilerImpl.java
            │   ├── ProfilingMethodInterceptor.java
            │   └── ProfilingState.java
            └── main/
                └── WebCrawlerMain.java
```

---

## My Implementation

I completed the following components:

- Implemented `ConfigurationLoader` using Jackson for JSON deserialization.
- Implemented `CrawlResultWriter` for JSON serialization.
- Developed `ParallelWebCrawler` using Java's `ForkJoinPool`.
- Implemented thread-safe crawling using concurrent collections.
- Implemented functional word sorting using the Java Stream API.
- Implemented a Dynamic Proxy–based performance profiler.
- Added crawl result and profiler output generation.
- Passed all provided unit tests.

---

## Building the Project

```bash
mvn package
```

---

## Running All Tests

```bash
mvn test
```

---

## Running Individual Tests

### Configuration Loader

```bash
mvn test -Dtest=ConfigurationLoaderTest
```

### Crawl Result Writer

```bash
mvn test -Dtest=CrawlResultWriterTest
```

### Parallel Web Crawler

```bash
mvn test -Dtest=WebCrawlerTest,ParallelWebCrawlerTest
```

### Word Counts

```bash
mvn test -Dtest=WordCountsTest
```

### Profiler

```bash
mvn test -Dtest=ProfilerImplTest
```

---

## Running the Sequential Crawler

```bash
java -classpath target/udacity-webcrawler-1.0.jar \
com.udacity.webcrawler.main.WebCrawlerMain \
src/main/config/sample_config_sequential.json
```

---

## Running the Parallel Crawler

```bash
mvn package

java -classpath target/udacity-webcrawler-1.0.jar \
com.udacity.webcrawler.main.WebCrawlerMain \
src/main/config/sample_config.json
```

---

## Example Configuration

```json
{
  "startPages": [
    "http://example.com"
  ],
  "parallelism": 4,
  "maxDepth": 10,
  "timeoutSeconds": 7,
  "popularWordCount": 10,
  "resultPath": "crawlResults.json"
}
```

---

## Learning Outcomes

This project strengthened my understanding of:

- Java Concurrency
- Fork/Join Framework
- Thread-safe Programming
- Executor Framework
- Functional Programming with Streams
- JSON Serialization & Deserialization
- Reflection and Dynamic Proxies
- Dependency Injection
- Performance Profiling
- Unit Testing

---

## Results

The parallel implementation significantly improves crawling throughput compared to the legacy sequential implementation by utilizing multiple CPU cores while maintaining thread safety and producing identical crawl results.

---

## Built With

- Java 17
- Maven
- Jackson
- Google Guice
- Jsoup
- JUnit 5

---

