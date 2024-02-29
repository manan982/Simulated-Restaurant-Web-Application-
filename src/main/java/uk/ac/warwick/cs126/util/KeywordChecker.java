package uk.ac.warwick.cs126.util;

import uk.ac.warwick.cs126.interfaces.IKeywordChecker;

public class KeywordChecker implements IKeywordChecker {
    private static final String[] keywords = {
            "0",
            "agreeable",
            "air-headed",
            "apocalypse",
            "appetizing",
            "average",
            "awesome",
            "biohazard",
            "bland",
            "bleh",
            "burnt",
            "charming",
            "clueless",
            "cockroach",
            "cold",
            "crap",
            "dancing",
            "dead",
            "decadent",
            "decent",
            "dirty",
            "disgusting",
            "dreadful",
            "droppings",
            "dry",
            "dumpy",
            "excellent",
            "favourite",
            "feel-good",
            "flavourful",
            "frozen",
            "gem",
            "gross",
            "heart",
            "heavenly",
            "horrendous",
            "horrible",
            "incredible",
            "interesting",
            "lame",
            "lousy",
            "mediocre",
            "meh",
            "mess",
            "microwaved",
            "mouth-watering",
            "nightmares",
            "ok",
            "okay",
            "overcooked",
            "overhyped",
            "perfection",
            "polite",
            "prompt",
            "quality",
            "rude",
            "satisfaction",
            "savoury",
            "sewer",
            "singing",
            "slow",
            "so-so",
            "spongy",
            "sticky",
            "sublime",
            "succulent",
            "sucked",
            "surprised",
            "terrible",
            "tingling",
            "tired",
            "toxic",
            "traumatizing",
            "uncomfortable",
            "under-seasoned",
            "undercooked",
            "unique",
            "unprofessional",
            "waste",
            "worst",
            "yuck",
            "yummy"
    };

    private int firstWord;
    private int finalWord;

    public KeywordChecker() {
        // Initialise things here

        finalWord = (keywords.length) - 1;
        firstWord = 0;
    }

    public boolean binarySearch(String[] words, int firstWord, int finalWord, String word) {
        
        if (firstWord <= finalWord) { 
            
            int midPoint = ((finalWord - firstWord) / 2 );
            midPoint = firstWord + midPoint ; // midpoint integer value is created

            if(word.compareTo(words[midPoint]) < 0) {
                return binarySearch(words, firstWord, midPoint - 1, word);   

                //recursively calling function,between first item to midpoint to sort upper half
            }
            
            if(word.equals(words[midPoint])) { //If the midPoint word matches, then return true
                return true;
            }
            
            return binarySearch(words, midPoint + 1, finalWord, word); //recursively calling function,between midpoint to last word to sort lower half
        }   

        return false;  //return false if word not located
    }

    public boolean isAKeyword(String word) {
        if (word == null) {
            return false; //returns false if there is no word entered
        }
        else  //else call binary search sort
            return binarySearch(keywords, firstWord, finalWord, word.toLowerCase()); 
    }
}