package com.udacity.webcrawler;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.PriorityQueue;
final class WordCounts {
    static Map<String, Integer> sort(Map<String, Integer> wordCounts, int popularWordCount) {
        PriorityQueue<Map.Entry<String, Integer>> sortedCounts =
                new PriorityQueue<>(wordCounts.size(), new WordCountComparator());
        sortedCounts.addAll(wordCounts.entrySet());
        Map<String, Integer> topCounts = new LinkedHashMap<>();
        for (int i = 0; i < Math.min(popularWordCount, wordCounts.size()); i++) {
            Map.Entry<String, Integer> entry = sortedCounts.poll();
            topCounts.put(entry.getKey(), entry.getValue());
        }
        return topCounts;
    }
    private static final class WordCountComparator implements Comparator<Map.Entry<String, Integer>> {
        @Override
        public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b) {
            if (!a.getValue().equals(b.getValue())) {
                return b.getValue() - a.getValue();
            }
            if (a.getKey().length() != b.getKey().length()) {
                return b.getKey().length() - a.getKey().length();
            }
            return a.getKey().compareTo(b.getKey());
        }
    }
    private WordCounts() {
    }
}