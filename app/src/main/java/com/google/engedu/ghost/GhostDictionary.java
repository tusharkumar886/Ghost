package com.google.engedu.ghost;

public interface GhostDictionary {
    int MIN_WORD_LENGTH = 4;
    boolean isWord(String word);
    String getAnyWordStartingWith(String prefix);
    String getGoodWordStartingWith(String prefix);
}
