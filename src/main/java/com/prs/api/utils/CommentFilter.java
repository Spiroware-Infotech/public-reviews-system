package com.prs.api.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CommentFilter {

	// Bad words dictionary
    private static final Set<String> BAD_WORDS = new HashSet<>(Arrays.asList(
        "bad", "awful", "terrible", "horrible", "shit", "fuck", "damn", 
        "crap", "suck", "worthless", "garbage", "idiot", "stupid","rude"
    ));
    
    // Negative sentiment words
    private static final Set<String> NEGATIVE_WORDS = new HashSet<>(Arrays.asList(
        "hate", "dislike", "angry", "annoying", "frustrating", "disappointing",
        "poor", "broken", "fail", "problem", "issue", "complaint"
    ));

    public static boolean containsBadWords(String comment) {
        if (comment == null || comment.isBlank()) return false;

        // Normalize and split comment
        String[] words = comment.toLowerCase().replaceAll("[^a-z ]", "").split("\\s+");

        for (String word : words) {
            if (BAD_WORDS.contains(word)) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsNegativeKeyword(String comment) {
        String lowerCaseComment = comment.toLowerCase();
        for (String word : NEGATIVE_WORDS) {
            if (lowerCaseComment.contains(word)) {
                return true;
            }
        }
        return false;
    }
    
    public static List<String> getMatchedBadWords(String comment) {
        if (comment == null || comment.isBlank()) return List.of();

        String[] words = comment.toLowerCase().replaceAll("[^a-z ]", "").split("\\s+");
        return Arrays.stream(words)
                .filter(BAD_WORDS::contains)
                .distinct()
                .collect(Collectors.toList());
    }
}