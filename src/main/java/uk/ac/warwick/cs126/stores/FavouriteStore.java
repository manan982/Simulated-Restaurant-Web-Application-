package uk.ac.warwick.cs126.stores;

import uk.ac.warwick.cs126.interfaces.IFavouriteStore;
import uk.ac.warwick.cs126.models.Favourite;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.io.IOUtils;

import uk.ac.warwick.cs126.structures.MyArrayList;
import uk.ac.warwick.cs126.structures.HashMap;

import uk.ac.warwick.cs126.util.DataChecker;

public class FavouriteStore implements IFavouriteStore {

    private MyArrayList<Favourite> favouriteArray;
    
    private DataChecker dataChecker;
    private MyArrayList<Long> blackListedArray;
    private MyArrayList<Favourite> replacedFavourites;
    private MyArrayList<Favourite> ArrayOfFavs1;
    private MyArrayList<Favourite> ArrayOfFavs2;
    private MyArrayList<Favourite> CommonFavArray;
    
    private MyArrayList<Long> CommonFavArrayID;
    private MyArrayList<Long> MissingFavArrayID;
    private MyArrayList<Long> NonCommonFavArrayID;
    
    private MyArrayList<Favourite> CommonNonFaveArray;

    private MyArrayList<Favourite> MissingFaveArray;

    public FavouriteStore() {
        // Initialise variables here
        favouriteArray = new MyArrayList<>();
        dataChecker = new DataChecker();
        blackListedArray = new MyArrayList<>();
        replacedFavourites = new MyArrayList<>();
        
        ArrayOfFavs1 = new MyArrayList<>();
        ArrayOfFavs2 = new MyArrayList<>();

        CommonFavArray = new MyArrayList<>();
        MissingFaveArray = new MyArrayList<>();
        CommonNonFaveArray = new MyArrayList<>();
        
        CommonFavArrayID = new MyArrayList<>();
        MissingFavArrayID = new MyArrayList<>();
        NonCommonFavArrayID = new MyArrayList<>();

        CommonNonFaveArray = new MyArrayList<>();
        MissingFaveArray = new MyArrayList<>();
    }

    public Favourite[] loadFavouriteDataToArray(InputStream resource) {
        Favourite[] favouriteArray = new Favourite[0];

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

            Favourite[] loadedFavourites = new Favourite[lineCount - 1];

            BufferedReader csvReader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            int favouriteCount = 0;
            String row;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            csvReader.readLine();
            while ((row = csvReader.readLine()) != null) {
                if (!("".equals(row))) {
                    String[] data = row.split(",");
                    Favourite favourite = new Favourite(
                            Long.parseLong(data[0]),
                            Long.parseLong(data[1]),
                            Long.parseLong(data[2]),
                            formatter.parse(data[3]));
                    loadedFavourites[favouriteCount++] = favourite;
                }
            }
            csvReader.close();

            favouriteArray = loadedFavourites;

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return favouriteArray;
    }
    
    
    /**
     * 
     * @param favourites The array to be inputted
     * @param l Stores Left most integer value pointer
     * @param r Stores Right most integer value pointer
     * @param requirement  A condition that determines how the array should be sorted, i.e different String values will causes ordering by either Date Favourited or ID, for each Restaurant and CustomerID
     */
    private void Sort(Favourite favourites[], int l, int r, String requirement) {
        
        if (r > l) {
            int m = (l + r) / 2 ;

            Sort(favourites, l, m, requirement);
            Sort(favourites, m + 1, r, requirement);  

            merge(favourites,l,m,r, requirement);    
        }
    }

/**
 * 
 * @param favourites The array to be inputted
 * @param l Stores Left most integer value pointer
 * @param m Stores Right most integer value pointer
 * @param r Stores Right most integer value pointer
 * @param requirement A condition that determines how the array should be sorted, i.e different String values will causes ordering by either Date Favourited or ID, for each Restaurant and CustomerID
 */
    private void merge(Favourite favourites[], int l, int m, int r, String requirement) {
        
        int firstArray = m - l + 1;
        Favourite L[] = new Favourite[firstArray];  // Create temp arrays
        for (int i = 0; i < firstArray; ++i) { // Copy data to temp arrays
            L[i] = favourites[l + i];
        }
        
        int secondArray = r - m;
        Favourite R[] = new Favourite[secondArray];
        for (int j = 0; j < secondArray; ++j) {
            R[j] = favourites[1 + j + m];
        }
 
        /* Merge the temp arrays */
 
        int i = 0, j = 0; // Initial indexes of first and second subarrays
        int k = l; // Initial index of merged subarry array
        
        while (i < firstArray && j < secondArray) {
            
            if(requirement == "ID") {
                if (L[i].getID() <= R[j].getID()) {
                    favourites[k] = L[i];
                    i++;
                }
                else {
                    favourites[k] = R[j];
                    j++;
                }
                k++;
            }

            else if (requirement == "RestaurantID") {
                if (L[i].getDateFavourited().equals(R[j].getDateFavourited())) {
                    if (L[i].getID() <= R[j].getID()) {
                        favourites[k] = L[i];
                        i++;
                    }
                    else {
                        favourites[k] = R[j];
                        j++;
                    }
                }
                else if (L[i].getDateFavourited().after(R[j].getDateFavourited())) {
                    favourites[k] = L[i];
                    i++;
                }
                else {
                    favourites[k] = R[j];
                    j++;
                }
                k++;
                
            }

            else if (requirement == "CustomerID") {
                if (L[i].getDateFavourited().equals(R[j].getDateFavourited())) {
                    if (L[i].getID() <= R[j].getID()) {
                        favourites[k] = L[i];
                        i++;
                    }
                    else {
                        favourites[k] = R[j];
                        j++;
                    }
                }
                else if (L[i].getDateFavourited().after(R[j].getDateFavourited())) {
                    favourites[k] = L[i];
                    i++;
                }
                else {
                    favourites[k] = R[j];
                    j++;
                }
                k++;
                
            }
            
        }
 
        //If any remaining elemtns in L[], they are copied
        while (i < firstArray) {
            favourites[k] = L[i];
            i++;
            k++;
        }
 
        //If any remaining elemtns in R[], they are copied
        while (j < secondArray) {
            favourites[k] = R[j];
            j++;
            k++;
        }
    }

    
    
    /**
     * 
     * @param favourite
     * @return
     */
    public boolean addFavourite(Favourite favourite) {
                
        if(dataChecker.isValid(favourite) == false) {
            return false;
        } //Check if favourite is valid
        

        boolean outcome = false; //creating boolean variable to allow if statements using false
    
        
        for (int i = 0; i < favouriteArray.size(); i++) { //linear search through favouriteArray
            Favourite current = favouriteArray.get(i); //creating object of type Favourite
            if (current.getID().equals(favourite.getID())) { //checks for duplicate ID to be blacklisted
                blackListedArray.add(current.getID()); //adds Id to blacklist, which is an array list of type long
                return false;
            }    

            //Checking to see if a favourite has the same restaurantId and customerID, if it does, then keep whichever one was favourited first and blacklist the newer one
            if (favourite.getRestaurantID().equals(current.getRestaurantID()) && favourite.getCustomerID().equals(current.getCustomerID())) {
                if (outcome == false) {
                    replacedFavourites.add(favourite);
                } //if non valid ID, then add to this store, as we don't want it in the main favourite array

                if (outcome == false) {
                    if (current.getDateFavourited().after(favourite.getDateFavourited())) { // if the existing favourite is newer than the inputted favourite
                        favouriteArray.remove(favourite);
                        blackListedArray.add(current.getID()); //blacklist existing old favourite, will not be used in favouriteArray again
                        favouriteArray.remove(current);
                    } //else, current favourite is older than inputted favourite, so no favourite is added.
                        return false;
                }
                    
                else {
                    //if there are multiple duplicates in the store, this method will deal with the edge cases of them
                    Favourite earliest = replacedFavourites.get(0); // getting 0 position from the duplicated faves, which is the oldest faveourite
                    for (int x = 0; x < replacedFavourites.size(); x++) { 
                            Favourite identicalFavourite = replacedFavourites.get(x); //identical favourite is the current duplicate

                        if (identicalFavourite.getCustomerID().equals(favourite.getCustomerID()) && favourite.getRestaurantID().equals(identicalFavourite.getRestaurantID()) ) {
                            if (favourite.getDateFavourited().after(identicalFavourite.getDateFavourited())) {
                                identicalFavourite = earliest; //The oldest duplicate is allocated to the oldest favourite.
                            }
                        }
                    }
                    favouriteArray.add(earliest); //adds the oldest favourite
                    }
                }
            }
        
     
        favouriteArray.add(favourite);
        return true;

    }

    /**
     * 
     * @param favourites
     * @return
     */
    public boolean addFavourite(Favourite[] favourites) {
       
        Boolean additionOfFavourite = true;
        for (int i = 0; i < favourites.length; i++) {
            if(!addFavourite(favourites[i])) {
                additionOfFavourite = false;
            }
        }

        return additionOfFavourite;
    }

    /**
     * 
     * @param id
     * @return
     */
    public Favourite getFavourite(Long id) {
        
        for (int i = 0; i < favouriteArray.size(); i++) {
            if (favouriteArray.get(i).getID().equals(id)) {
                return favouriteArray.get(i);
            }
        }   
        
        //if restaurant not found, return null 
        return null;
    }

    /**
     * 
     * @return
     */
    public Favourite[] getFavourites() {
        
        Favourite[] favourites = ConvertingFromArrayList(favouriteArray);
        Sort(favourites,0,favourites.length - 1, "ID");
       
        return favourites;
    }

    /**
     * 
     * @param id
     * @return
     */
    public Favourite[] getFavouritesByCustomerID(Long id) {
        
        MyArrayList<Favourite> temporarayArrayList = new MyArrayList<>();
        //creating a temporary arraylist, that only exists in this method. Will hold all favourites that have id for that particular customer ID

        for (int i = 0; i < favouriteArray.size(); i++) {
            if (favouriteArray.get(i).getCustomerID().equals(id)) { //linear searching through the favouriteArray, if the id's match then add to the temporary arraylist
                temporarayArrayList.add(favouriteArray.get(i));
            }
        }  
        
        Favourite[] favourites = ConvertingFromArrayList(temporarayArrayList); //convert the array list to an array
        //merge sort this array, as it only contains the specific favourites that each customer had favourited, corresponding to thier ID

            Sort(favourites,0,favourites.length - 1, "CustomerID");
            return favourites;
        
    
    }

    /**
     * 
     * @param id
     * @return
     */
    public Favourite[] getFavouritesByRestaurantID(Long id) {
        
        MyArrayList<Favourite> temporarayArrayList = new MyArrayList<>(); //new temporary arraylist created, has no correlation to the temp arraylist created in the above method

        for (int i = 0; i < favouriteArray.size(); i++) {
            if (favouriteArray.get(i).getRestaurantID().equals(id)) { //same thing as getFavouritesByCustomerID(), but this time getRestaurant id instead of customer
                temporarayArrayList.add(favouriteArray.get(i)); //add to a  temporary arraylist
            }
        }  
        
        Favourite[] favourites = ConvertingFromArrayList(temporarayArrayList);
            Sort(favourites,0,favourites.length - 1, "RestaurantID");
            return favourites;
    }

    /**
     * 
     * @param customer1ID input Id of first customer
     * @param customer2ID input Id of second customer
     * @return
     */
    public Long[] getCommonFavouriteRestaurants(Long customer1ID, Long customer2ID) {
        
        if (customer1ID == (null) || customer2ID == (null)) {
            return new Long[0];
        }
        
        //linear searching through the favouriteArray, and if the input id of the customers match the id's of any customers in the store, then add to a arraylist, for each customer
        for (int x = 0; x < favouriteArray.size(); x++) {
            if (customer1ID.equals(favouriteArray.get(x).getCustomerID())) {
                ArrayOfFavs1.add(favouriteArray.get(x)); //add customer 1's faves to array 1
            }
            if (customer2ID.equals(favouriteArray.get(x).getCustomerID())) {
                ArrayOfFavs2.add(favouriteArray.get(x)); //adding customer 2's faves to array 2
            }
        }
        
        //initalising for while loop
        int i = 0;
        int j = 0;

        while (i < ArrayOfFavs1.size() && j < ArrayOfFavs2.size()) {

            if (ArrayOfFavs1.get(i).getRestaurantID().equals(ArrayOfFavs2.get(j).getRestaurantID())) {
                if (ArrayOfFavs1.get(i).getDateFavourited().before(ArrayOfFavs1.get(j).getDateFavourited())) { //adds the latest favourite for each matching favourite, if restaurant ids are common
                    CommonFavArray.add(ArrayOfFavs2.get(j));
                    j++;
                }
                else if (ArrayOfFavs1.get(i).getDateFavourited().after(ArrayOfFavs1.get(j).getDateFavourited())) {
                    CommonFavArray.add(ArrayOfFavs2.get(i)); //add to the common fav arraylist created at the start
                    i++;
                }
                
                else {
                    CommonFavArray.add(ArrayOfFavs1.get(i)); //if the dates favourited are equal, increment both i and j
                    i++;
                    j++; //think of CommonFavArray as A N B
                }
            }  
               
        }

        Favourite[] commonFavouriteRestaurantss = ConvertingFromArrayList(CommonFavArray); //convert the common array list to an array

        Sort(commonFavouriteRestaurantss, 0, commonFavouriteRestaurantss.length, "RestaurantID");
        //merge by date favourited
        for (int k = 0; k < commonFavouriteRestaurantss.length; k++) {
            CommonFavArrayID.add(commonFavouriteRestaurantss[i].getRestaurantID());
        } // takes the id of every element in the array, and takes these ids and adds them to a new array list of type long, consisting of ids only
        
        Long[] commonIDfavourites = ConvertingFromArrayListLong(CommonFavArrayID); //converts this to an array of type long. no need to merge sort, as already sorted by restaurant id

        return commonIDfavourites;

    }

    /**
     * 
     * @param customer1ID
     * @param customer2ID
     * @return
     */
    public Long[] getMissingFavouriteRestaurants(Long customer1ID, Long customer2ID) {
        
        if (customer1ID == (null) || customer2ID == (null)) {
            return new Long[0];
        }
        
        for (int x = 0; x < favouriteArray.size(); x++) {
            if (customer1ID.equals(favouriteArray.get(x).getCustomerID())) {
                ArrayOfFavs1.add(favouriteArray.get(x));
            }
            if (customer2ID.equals(favouriteArray.get(x).getCustomerID())) {
                ArrayOfFavs2.add(favouriteArray.get(x));
            }
        }

        //This increments count every time the ids are equal, if no Ids are equal then it is classed as missing

        for(int i = 0; i < ArrayOfFavs1.size(); i++) {
            int counter = 0;
            for(int j = 0; j < ArrayOfFavs2.size(); j++) {
                if(ArrayOfFavs1.get(i).getRestaurantID().equals(ArrayOfFavs2.get(j).getRestaurantID()) && ArrayOfFavs1.get(i) != null && ArrayOfFavs2.get(i) != null){
                    counter++; 
                }
            }
            if(counter == 0){
                MissingFaveArray.add(ArrayOfFavs1.get(i)); //adds to missing array. In set notation, think of this as (A' N B) or (A N B')
            }
        }

        int z = 0;

        Favourite[] missingFavouriteRestaurants = ConvertingFromArrayList(MissingFaveArray);

        Sort(missingFavouriteRestaurants, 0, missingFavouriteRestaurants.length, "RestaurantID");
        //merge by date favourited
        for (int k = 0; k < missingFavouriteRestaurants.length; k++) {
            MissingFavArrayID.add(missingFavouriteRestaurants[z].getRestaurantID());
        }
        
        Long[] missingIDfavourites = ConvertingFromArrayListLong(MissingFavArrayID);

        return missingIDfavourites;
    }

    /**
     * 
     * @param customer1ID
     * @param customer2ID
     * @return
     */
    public Long[] getNotCommonFavouriteRestaurants(Long customer1ID, Long customer2ID) {
        
        if (customer1ID == (null) || customer2ID == (null)) {
            return new Long[0];
        }
        
        for (int x = 0; x < favouriteArray.size(); x++) {
            if (customer1ID.equals(favouriteArray.get(x).getCustomerID())) {
                ArrayOfFavs1.add(favouriteArray.get(x));
            }
            if (customer2ID.equals(favouriteArray.get(x).getCustomerID())) {
                ArrayOfFavs2.add(favouriteArray.get(x));
            }
        }

        for(int i = 0; i < ArrayOfFavs1.size( ); i++) {
            int counter = 0;
            for(int j = 0; j < ArrayOfFavs2.size(); j++) {
                if(ArrayOfFavs1.get(i).getRestaurantID().equals(ArrayOfFavs2.get(j).getRestaurantID()) && ArrayOfFavs1.get(i) != null && ArrayOfFavs2.get(i) != null){
                    counter++;
                }
            }
            if (counter == 0) {
                CommonNonFaveArray.add(ArrayOfFavs1.get(i)); //think of this as (A U B)' in set notation form
            }
        }

        //same for loop repeated above, this way every id is checked to see if they are not common. Adds to the same arraylist for both this time, combining both missing sides to give all non common faves
        for(int i = 0; i < ArrayOfFavs2.size(); i++){
            int counter = 0;
            for(int j = 0; j < ArrayOfFavs1.size(); j++){
                if(ArrayOfFavs2.get(i).getRestaurantID().equals(ArrayOfFavs1.get(j).getRestaurantID())){
                    counter++;
                }
            }
            if(counter == 0){
                CommonNonFaveArray.add(ArrayOfFavs2.get(i));
            }
        }

        int z = 0;
        
        Favourite[] unionFavouriteRestaurants = ConvertingFromArrayList(CommonNonFaveArray);

        Sort(unionFavouriteRestaurants, 0, unionFavouriteRestaurants.length, "RestaurantID");

        for (int k = 0; k < unionFavouriteRestaurants.length; k++) {
            NonCommonFavArrayID.add(unionFavouriteRestaurants[z].getRestaurantID());
        }
        
        Long[] commonIDfavourites = ConvertingFromArrayListLong(CommonFavArrayID);

        return commonIDfavourites;
    }

    /**
     * 
     * @return
     */
    public Long[] getTopCustomersByFavouriteCount() {
        
        return new Long[20];
    }

    /**
     * 
     * @return
     */
    public Long[] getTopRestaurantsByFavouriteCount() {
        
        return new Long[20];
    }

    /**
     * 
     * @param favourites
     * @return
     */
    private static Favourite[] ConvertingFromArrayList(MyArrayList<Favourite> favourites) {
        Favourite[] favArr = new Favourite[favourites.size()];
        for (int i = 0; i < favourites.size(); i++) {
            favArr[i] = favourites.get(i);
        }
        return favArr;
    }

    /**new conversion of array created, used to convert from arraylist to array, but instead of type Favourite/Customer etc, it is a type long, so it used for IDs exclusively
     * 
     * @param longs Takes in Array list that store IDs
     * @return
     */
    private static Long[] ConvertingFromArrayListLong(MyArrayList<Long> longs) {
        Long[] longArr = new Long[longs.size()];
        for (int i = 0; i < longs.size(); i++) {
            longArr[i] = longs.get(i);
        }
        return longArr;
    }

}
