package uk.ac.warwick.cs126.stores;

import uk.ac.warwick.cs126.interfaces.IReviewStore;
import uk.ac.warwick.cs126.models.Review;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.io.IOUtils;

import uk.ac.warwick.cs126.structures.MyArrayList;

import uk.ac.warwick.cs126.util.DataChecker;
import uk.ac.warwick.cs126.util.KeywordChecker;
import uk.ac.warwick.cs126.util.StringFormatter;

public class ReviewStore implements IReviewStore {

    private MyArrayList<Review> reviewArray;
    private DataChecker dataChecker;
    private MyArrayList<Long> blackListedArray;
    private MyArrayList<Review> replacedreviews;
    private StringFormatter stringFormatter;


    public ReviewStore() {
        // Initialise variables here
        reviewArray = new MyArrayList<>();
        dataChecker = new DataChecker();
        blackListedArray = new MyArrayList<>();
        replacedreviews = new MyArrayList<>();
        stringFormatter = new StringFormatter();
    }

    public Review[] loadReviewDataToArray(InputStream resource) {
        Review[] reviewArray = new Review[0];

        try {
            byte[] inputStreamBytes = IOUtils.toByteArray(resource);
            BufferedReader lineReader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            int lineCount = 0;
            String line;
            while ((line = lineReader.readLine()) != null) {
                if (!("".equals(line))) {
                    lineCount++;
                }
            }
            lineReader.close();

            Review[] loadedReviews = new Review[lineCount - 1];

            BufferedReader tsvReader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            int reviewCount = 0;
            String row;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            tsvReader.readLine();
            while ((row = tsvReader.readLine()) != null) {
                if (!("".equals(row))) {
                    String[] data = row.split("\t");
                    Review review = new Review(
                            Long.parseLong(data[0]),
                            Long.parseLong(data[1]),
                            Long.parseLong(data[2]),
                            formatter.parse(data[3]),
                            data[4],
                            Integer.parseInt(data[5]));
                    loadedReviews[reviewCount++] = review;
                }
            }
            tsvReader.close();

            reviewArray = loadedReviews;

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return reviewArray;
    }

    private void Sort(Review reviews[], int l, int r, String requirement) {
        
        if (r > l) {
            int m = (l + r) / 2 ;

            Sort(reviews, l, m, requirement);
            Sort(reviews, m + 1, r, requirement);  

            merge(reviews,l,m,r, requirement);    
        }
    }


    private void merge(Review reviews[], int l, int m, int r, String requirement) {
        
        int firstArray = m - l + 1;
        Review L[] = new Review[firstArray];  
        for (int i = 0; i < firstArray; ++i) { 
            L[i] = reviews[l + i];
        }
        
        int secondArray = r - m;
        Review R[] = new Review[secondArray];
        for (int j = 0; j < secondArray; ++j) {
            R[j] = reviews[1 + j + m];
        }
 
        /* Merge the temp arrays */
 
        int i = 0, j = 0; // Initial indexes of first and second subarrays
        int k = l; // Initial index of merged subarry array
        
        while (i < firstArray && j < secondArray) {
            

            if(requirement == "ID") {
                if (L[i].getID() <= R[j].getID()) {
                    reviews[k] = L[i];
                    i++;
                }
                else {
                    reviews[k] = R[j];
                    j++;
                }
                k++;
            }

            
            else if (requirement == "RestaurantID") {
                if (L[i].getDateReviewed().equals(R[j].getDateReviewed())) {
                    if (L[i].getID() <= R[j].getID()) {
                        reviews[k] = L[i];
                        i++;
                    }
                    else {
                        reviews[k] = R[j];
                        j++;
                    }
                }
                else if (L[i].getDateReviewed().after(R[j].getDateReviewed())) {
                    reviews[k] = L[i];
                    i++;
                }
                else {
                    reviews[k] = R[j];
                    j++;
                }
                k++;
                
            }
            else if (requirement == "CustomerID") {
                if (L[i].getDateReviewed().equals(R[j].getDateReviewed())) {
                    if (L[i].getID() <= R[j].getID()) {
                        reviews[k] = L[i];
                        i++;
                    }
                    else {
                        reviews[k] = R[j];
                        j++;
                    }
                }
                else if (L[i].getDateReviewed().after(R[j].getDateReviewed())) {
                    reviews[k] = L[i];
                    i++;
                }
                else {
                    reviews[k] = R[j];
                    j++;
                }
                k++;
                
            }

            else if(requirement == "Rating") {  
                if (L[i].getRating() == R[j].getRating() ) {
                    if (L[i].getDateReviewed().equals(R[j].getDateReviewed())) {
                        if (L[i].getID() <= R[j].getID()) {
                            reviews[k] = L[i];
                            i++;    
                        }
                        else {
                            reviews[k] = R[j];
                            j++;
                        }
                    }
                    else if (L[i].getDateReviewed().after(R[j].getDateReviewed())) {
                        reviews[k] = L[i];
                        i++;
                    }
                    else {
                        reviews[k] = R[j];
                        j++;
                    }
                }
                
                else if ((L[i].getRating()) >= (R[j].getRating()) ) {
                    reviews[k] = L[i];
                    i++;
                }
                else {
                    reviews[k] = R[j];
                    j++;
                }
            
                k++;
            } 

            
            
        }
 
        //If any remaining elemtns in L[], they are copied
        while (i < firstArray) {
            reviews[k] = L[i];
            i++;
            k++;
        }
 
        //If any remaining elemtns in R[], they are copied
        while (j < secondArray) {
            reviews[k] = R[j];
            j++;
            k++;
        }
    }

    public boolean addReview(Review review) {
        
        if(dataChecker.isValid(review) == false) {
            return false;
        } //Check if review is valid

        boolean outcome = false; //creating boolean variable to allow if statements using false
    
        
        for (int i = 0; i < reviewArray.size(); i++) { //linear search through reviewArray
            Review current = reviewArray.get(i); //creating object of type review
            if (current.getID().equals(review.getID())) { //checks for duplicate ID to be blacklisted
                blackListedArray.add(current.getID()); //adds Id to blacklist, which is an array list of type long
                return false;
            }    

            //Checking to see if a review has the same restaurantId and customerID, if it does, then keep whichever one was reviewd first and blacklist the newer one
            if (review.getRestaurantID().equals(current.getRestaurantID()) && review.getCustomerID().equals(current.getCustomerID())) {
                if (outcome == false) {
                    replacedreviews.add(review);
                } //if non valid ID, then add to this store, as we don't want it in the main review array

                if (outcome == false) {
                    if (current.getDateReviewed().before(review.getDateReviewed())) { // if the existing review is older than the inputted review
                        reviewArray.remove(review);
                        blackListedArray.add(current.getID()); //blacklist existing old review, will not be used in reviewArray again
                        reviewArray.remove(current);
                    } //else, current review is newer than inputted review, so no review is added.
                        return false;
                }
            }
            }
        
    reviewArray.add(review); //add review to store, return true
    return true;
         

    }

    public boolean addReview(Review[] reviews) {
        
        Boolean additionOfReview = true;
        for (int i = 0; i < reviews.length; i++) {
            if(!addReview(reviews[i])) {
                additionOfReview = false;
            }
        }

        return additionOfReview;
    }

    public Review getReview(Long id) {
        
        for (int i = 0; i < reviewArray.size(); i++) {
            if (reviewArray.get(i).getID().equals(id)) {
                return reviewArray.get(i);
            }
        }   
        
        //if restaurant not found, return null 
        return null;
    }

    public Review[] getReviews() {
        
        Review[] reviews = ConvertingFromArrayList(reviewArray);
        Sort(reviews,0,reviews.length - 1, "ID");
       
        return reviews;
    }

    public Review[] getReviewsByDate() {
        Review[] reviews = ConvertingFromArrayList(reviewArray);
        Sort(reviews,0,reviews.length - 1, "RestaurantID");
       
        return reviews;
    }

    public Review[] getReviewsByRating() {
        
        Review[] reviews = ConvertingFromArrayList(reviewArray);
        Sort(reviews,0,reviews.length - 1, "Rating");
       
        return reviews;
    }

    public Review[] getReviewsByCustomerID(Long id) {
        
        MyArrayList<Review> temporarayArrayList = new MyArrayList<>();

        for (int i = 0; i < reviewArray.size(); i++) {
            if (reviewArray.get(i).getCustomerID().equals(id)) {
                temporarayArrayList.add(reviewArray.get(i));
            }
        }  
        
        Review[] reviews = ConvertingFromArrayList(temporarayArrayList);
            Sort(reviews,0,reviews.length - 1, "CustomerID");
            return reviews;
    }

    public Review[] getReviewsByRestaurantID(Long id) {
        
        MyArrayList<Review> temporarayArrayList = new MyArrayList<>();

        for (int i = 0; i < reviewArray.size(); i++) {
            if (reviewArray.get(i).getRestaurantID().equals(id)) {
                temporarayArrayList.add(reviewArray.get(i));
            }    
        }  
        
        Review[] reviews = ConvertingFromArrayList(temporarayArrayList);
            Sort(reviews,0,reviews.length - 1, "RestaurantID");
            return reviews;
    }

    /**
     * 
     * @param id ID
     * @return average customer rating
     */
    public float getAverageCustomerReviewRating(Long id) {
        
        if (id == null) {
            return 0.0f; //return no rating if no id is found
        }

        double count = 0; //initalise count to store rating
        Review[] reviews = ConvertingFromArrayList(reviewArray);

        for (int i = 0; i < reviewArray.size(); i++) {
            if (reviewArray.get(i).getCustomerID().equals(id)) {
                count = count + reviews[i].getRating(); 
            }      //linear search and add each rating to the total count
        }

        if (reviews.length == 0){    //to avoid errors by dividing by 0
            return 0.0f;
        }

        float CustomerRatingAvg = (float)count/reviews.length; //calcualate average

        CustomerRatingAvg = Math.round(CustomerRatingAvg * 10)/10.0f; //round to one decimal place

        return CustomerRatingAvg;
    }

    public float getAverageRestaurantReviewRating(Long id) {
        if (id == null) {
            return 0.0f;
        }
        double count = 0;
        Review[] reviews = ConvertingFromArrayList(reviewArray);

        for (int i = 0; i < reviewArray.size(); i++) {
            if (reviewArray.get(i).getRestaurantID().equals(id)) {
                count = count + reviews[i].getRating(); 
            }      //add each rating to the count count
        }

        if (reviews.length == 0){           //avoid a divide by 0 zero
            return 0.0f;
        }
            //calculate and round the average to 1dp
        float RestaurantRatingAvg = (float)count/reviews.length;

        RestaurantRatingAvg = Math.round(RestaurantRatingAvg * 10)/10.0f;

        return RestaurantRatingAvg;
    }

    public int[] getCustomerReviewHistogramCount(Long id) {
        
        if (id == null) {
            return new int[5];
        }

        int[] HistoGram = {0,0,0,0,0}; //initialse histogram

        
        Review[] reviews = ConvertingFromArrayList(reviewArray);
        for (int i = 0; i < reviewArray.size(); i++) {
            if (reviewArray.get(i).getCustomerID().equals(id)) {
                HistoGram[reviewArray.get(i).getRating() - 1] ++;  //increment histogram counter if the customer id matches input id
            }
        }
        
        return HistoGram;
    }

    public int[] getRestaurantReviewHistogramCount(Long id) {
        
        if (id == null) {
            return new int[5];
        }

        int[] HistoGram = {0,0,0,0,0};

        Review[] reviews = ConvertingFromArrayList(reviewArray);
        for (int i = 0; i < reviewArray.size(); i++) {
            if (reviewArray.get(i).getRestaurantID().equals(id)) {
                HistoGram[reviewArray.get(i).getRating() - 1] ++;   //increment histogram counter if restaurant id matches input id
            }
        }
        
        return HistoGram;
    }

    public Long[] getTopCustomersByReviewCount() {
        // TODO
        return new Long[20];
    }

    public Long[] getTopRestaurantsByReviewCount() {
        // TODO
        return new Long[20];
    }

    public Long[] getTopRatedRestaurants() {
        // TODO
        return new Long[20];
    }

    public String[] getTopKeywordsForRestaurant(Long id) {
        // TODO
        return new String[5];
    }

    public Review[] getReviewsContaining(String searchTerm) {
        
        if (searchTerm == "") {
            return new Review[0];
        }

        searchTerm = searchTerm.trim();
        
        String searchTermConverted = stringFormatter.convertAccents(searchTerm);
        searchTermConverted = searchTermConverted.toLowerCase();

        String[] term = searchTermConverted.split("\\s+");

        String searchTermConvertedNew = term[0];
        for (int i = 1; i < term.length; i++) {
            searchTermConvertedNew = searchTermConvertedNew + " " + term[i]; //term = words
        }

        MyArrayList<Review> containingArray = new MyArrayList<>();

        
        Review[] reviews = ConvertingFromArrayList(reviewArray);
        
        for (Review review : reviews) { //creates review object, and for this object in the array, looks for the search term converted in the review text only
            if (review.getReview().toLowerCase().contains(searchTermConverted)) {
                containingArray.add(review);
            }
        }

        Review[] sortedArrayContaining = ConvertingFromArrayList(containingArray);
        Sort(sortedArrayContaining,0,sortedArrayContaining.length - 1, "CustomerID"); //sorts by Date reviewed. If dates are equal, then sort by ID

        return sortedArrayContaining;
    }

    private static Review[] ConvertingFromArrayList(MyArrayList<Review> reviews) {
        Review[] revArr = new Review[reviews.size()];
        for (int i = 0; i < reviews.size(); i++) {
            revArr[i] = reviews.get(i);
        }
        return revArr;
    }
}
