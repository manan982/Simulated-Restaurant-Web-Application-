package uk.ac.warwick.cs126.util;

import uk.ac.warwick.cs126.interfaces.IDataChecker;

import uk.ac.warwick.cs126.models.Customer;
import uk.ac.warwick.cs126.models.Restaurant;
import uk.ac.warwick.cs126.models.Favourite;
import uk.ac.warwick.cs126.models.Review;

import java.util.Date;

public class DataChecker implements IDataChecker {

    public DataChecker() {
        // Initialise things here
    }


    //returns the true id from an array of strings, the id that has been repeated
    public Long extractTrueID(String[] repeatedID) {
        
        long IDtrue;
        int IDLength = repeatedID.length; //finds how many numbers are in the ID

        if (repeatedID[0].equals(repeatedID[1]) || repeatedID[0].equals(repeatedID[2])) {
            IDtrue = Long.parseLong(repeatedID[0]);
        }
        //finding the id that appears at least twice, and returning it 
        else if (repeatedID[1].equals(repeatedID[2])) {
            IDtrue = Long.parseLong(repeatedID[1]);
        }

        else {
            return null; //return null if no common id found
        }

        if (IDLength != 3) { //if there aren't 3 elements in the array, return null
            return null;
        }

        return IDtrue;

    }

    public boolean isValid(Long inputID) {
        
        int track = 0;
        int digitNumberCount = String.valueOf(inputID).length(); //find number of digits in ID
        long duplicateID = inputID; // makes another copy of the input ID

        String condition = Long.toString(duplicateID);
        
        if (condition.contains("0")) { //do not accept Id with 0 anywhere
            return false;
        }
        
        if(digitNumberCount!=16) { //if no of digits is not 16 return false
            return false;
        }

        else { // find if any digit from 1-9 is appearing more than 3 times
            for (int i = 1; i < 10; i++) {
                int count = 0;
                while (duplicateID > 0) {
                    if (i == duplicateID % 10) {
                        count++; //increment each time thid happens 
                    }
                    duplicateID/=10;
                }

                if(count > 3) { //if it happens 3 times, set a variable meaning method returns false
                    track++;
                }
                duplicateID = inputID;
            }

            if (track == 0) { //if id has no single digit repeated more than 3 times and has 16 digits, it is considered valid
                return true;
            }
            else {
                return false;
            }
        }

    }

    public boolean isValid(Customer customer) {

        //if any of these criteria is null, return false
        if( (customer == null) || (customer.getID() == null) || (customer.getFirstName() == null) || (customer.getLastName() == null) || (customer.getDateJoined() == null) || (customer.getLatitude() == 0.0f) || (customer.getLongitude() == 0.0f)) {
            return false;
        }
        
        //Id should be valid
        if(!isValid(customer.getID())){
            return false;
        }

        return true;
    }

    public boolean isValid(Restaurant restaurant) {

        if( (restaurant == null) || (restaurant.getID() == null) )
        { return false;
    }
    
        if(!isValid(restaurant.getID())){
            return false;
        }
        
        return true;

    }

    public boolean isValid(Favourite favourite) {
        
        if (favourite == null || (favourite.getID() == null) || (favourite.getCustomerID() == null) || (favourite.getRestaurantID() == null) || (favourite.getDateFavourited() == null)) {
            return false;
        }
        
        if(!isValid(favourite.getID())){
            return false;
        }

        if(!isValid(favourite.getCustomerID())){
            return false;
        }

        if(!isValid(favourite.getRestaurantID())){
            return false;
        }
        
      




        
        return true;
    }

    public boolean isValid(Review review) {
        if ( review == null  || (review.getID() == null) || (review.getCustomerID() == null) || (review.getRestaurantID() == null) || (review.getDateReviewed() == null) || (review.getReview() == null)    ) {
            return false;
        }
        
        if(!isValid(review.getID())){
            return false;
        }

        if(!isValid(review.getCustomerID())){
            return false;
        }

        if(!isValid(review.getRestaurantID())){
            return false;
        }

        return true;
    }
}