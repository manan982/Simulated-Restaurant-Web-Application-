package uk.ac.warwick.cs126.stores;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;


import org.apache.commons.io.IOUtils;

import uk.ac.warwick.cs126.interfaces.IRestaurantStore;
import uk.ac.warwick.cs126.models.Cuisine;
import uk.ac.warwick.cs126.models.EstablishmentType;
import uk.ac.warwick.cs126.models.PriceRange;
import uk.ac.warwick.cs126.models.Restaurant;
import uk.ac.warwick.cs126.models.RestaurantDistance;
import uk.ac.warwick.cs126.structures.MyArrayList;
import uk.ac.warwick.cs126.util.ConvertToPlace;
import uk.ac.warwick.cs126.util.DataChecker;
import uk.ac.warwick.cs126.util.HaversineDistanceCalculator;
import uk.ac.warwick.cs126.util.StringFormatter;
import uk.ac.warwick.cs126.models.Place;

public class RestaurantStore implements IRestaurantStore {

    private MyArrayList<Restaurant> restaurantArray;
    private DataChecker dataChecker;
    private MyArrayList<Long> blacklist;
    private MyArrayList<Restaurant> restaurantArraybyWarwickStars;
    private StringFormatter stringFormatter;
    
    private ConvertToPlace convertToPlace; //creating a convertToPlace object

    public RestaurantStore() {
        // Initialise variables here
        restaurantArray = new MyArrayList<>();
        dataChecker = new DataChecker();
        blacklist = new MyArrayList<>();
        restaurantArraybyWarwickStars = new MyArrayList<>();
        stringFormatter = new StringFormatter();
        
        convertToPlace = new ConvertToPlace();

    }

    public Restaurant[] loadRestaurantDataToArray(InputStream resource) {
        Restaurant[] restaurantArray = new Restaurant[0];

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

            Restaurant[] loadedRestaurants = new Restaurant[lineCount - 1];

            BufferedReader csvReader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            String row;
            int restaurantCount = 0;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            csvReader.readLine();
            while ((row = csvReader.readLine()) != null) {
                if (!("".equals(row))) {
                    String[] data = row.split(",");

                    Restaurant restaurant = new Restaurant(
                            data[0],
                            data[1],
                            data[2],
                            data[3],
                            Cuisine.valueOf(data[4]),
                            EstablishmentType.valueOf(data[5]),
                            PriceRange.valueOf(data[6]),
                            formatter.parse(data[7]),
                            Float.parseFloat(data[8]),
                            Float.parseFloat(data[9]),
                            Boolean.parseBoolean(data[10]),
                            Boolean.parseBoolean(data[11]),
                            Boolean.parseBoolean(data[12]),
                            Boolean.parseBoolean(data[13]),
                            Boolean.parseBoolean(data[14]),
                            Boolean.parseBoolean(data[15]),
                            formatter.parse(data[16]),
                            Integer.parseInt(data[17]),
                            Integer.parseInt(data[18]));

                    loadedRestaurants[restaurantCount++] = restaurant;
                }
            }
            csvReader.close();

            restaurantArray = loadedRestaurants;

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return restaurantArray;
    }

/**
 * 
 * @param restaurants - The array to be inputted
 * @param l - Stores Left most integer value pointer
 * @param r - Stores Right most integer value pointer
 * @param requirement - A condition that determines how the array should be sorted, i.e different values will causes ordering by either ID, name, date, warwick stars, or rating
 */
    private void Sort(Restaurant restaurants[], int l, int r, String requirement) {
        
        if (r > l) {
            int m = (l + r) / 2 ;

            Sort(restaurants, l, m, requirement);
            Sort(restaurants, m + 1, r, requirement);  

            merge(restaurants,l,m,r, requirement);    
        }
    }

/**Code comments for merge sort included in CustomerStore
 * 
 * @param restaurants The array to be inputted
 * @param l Stores Left most integer value pointer
 * @param m Stores Right most integer value pointer
 * @param r Stores Midpoint integer value 
 * @param requirement A condition that determines how the array should be sorted, i.e different String values will causes ordering by either ID, name, date, warwick stars, or rating
 */
    private void merge(Restaurant restaurants[], int l, int m, int r, String requirement) {
        
        int firstArray = m - l + 1;
        Restaurant L[] = new Restaurant[firstArray];  
        for (int i = 0; i < firstArray; ++i) { 
            L[i] = restaurants[l + i];
        }
        
        int secondArray = r - m;
        Restaurant R[] = new Restaurant[secondArray];
        for (int j = 0; j < secondArray; ++j) {
            R[j] = restaurants[1 + j + m];
        }
 
        int i = 0, j = 0; // Initial indexes of first and second subarrays
        int k = l; // Initial index of merged subarry array
        
        while (i < firstArray && j < secondArray) {
            
            if(requirement == "ID") {
                if (L[i].getID() <= R[j].getID()) { //compares elements between 2 sub arrays
                    restaurants[k] = L[i]; 
                    i++; //increment depending on which element is greater.
                }
                else {
                    restaurants[k] = R[j];
                    j++;
                }
                k++;
            }

            //sort by Name, if parameter is equal to "Name"
            else if (requirement == "Name") {
                if (L[i].getName().compareToIgnoreCase(R[j].getName()) == 0) {
                    if (L[i].getID() <= R[j].getID()) {
                        restaurants[k] = L[i];
                        i++;
                    }
                    else {
                        restaurants[k] = R[j];
                        j++;
                    }
                }
                else if (L[i].getName().compareToIgnoreCase(R[j].getName()) < 0) {
                    restaurants[k] = L[i];
                    i++;
                }
                else {
                    restaurants[k] = R[j];
                    j++;
                }
                k++;
                
            }

            //this code is run for the getRestaurantsbyDate() method
            else if(requirement == "date") {
                if (L[i].getDateEstablished().equals(R[j].getDateEstablished())) { //if dates are equal
                    if (L[i].getName().compareToIgnoreCase(R[j].getName()) == 0) { //if names are also equal
                        if (L[i].getID() <= R[j].getID()) {  //sort by ID instead.
                            restaurants[k] = L[i];
                            i++;
                        }
                        else {
                            restaurants[k] = R[j];
                            j++;
                        }
                    }
                    else if (L[i].getName().compareToIgnoreCase(R[j].getName()) < 0) {
                        restaurants[k] = L[i];
                        i++;
                    }
                    else {
                        restaurants[k] = R[j];
                        j++;
                    }
                }

                else if (L[i].getDateEstablished().before(R[j].getDateEstablished())) {
                    restaurants[k] = L[i];
                    i++;
                }
                else {
                    restaurants[k] = R[j];
                    j++;
                }
            
                k++;
            }

            //this code is run for the getRestaurantsbyWarwickStars() method
            else if(requirement == "stars") {  

                if (L[i].getWarwickStars() == R[j].getWarwickStars() ) {
                    if (L[i].getName().compareToIgnoreCase(R[j].getName()) == 0) {
                        if (L[i].getID() <= R[j].getID()) {
                            restaurants[k] = L[i];
                            i++;    
                        }
                        else {
                            restaurants[k] = R[j];
                            j++;
                        }
                    }
                    else if (L[i].getName().compareToIgnoreCase(R[j].getName()) < 0) {
                        restaurants[k] = L[i];
                        i++;
                    }
                    else {
                        restaurants[k] = R[j];
                        j++;
                    }
                }
                
                else if ((L[i].getWarwickStars()) >= (R[j].getWarwickStars()) ) {
                    restaurants[k] = L[i];
                    i++;
                }
                else {
                    restaurants[k] = R[j];
                    j++;
                }
            
                k++;
            }   

            //this code is run for the getRestaurantsbyRating() method
            else if(requirement == "Rating") {  
                if (L[i].getCustomerRating() == R[j].getCustomerRating() ) {
                    if (L[i].getName().compareToIgnoreCase(R[j].getName()) == 0) {
                        if (L[i].getID() <= R[j].getID()) {
                            restaurants[k] = L[i];
                            i++;    
                        }
                        else {
                            restaurants[k] = R[j];
                            j++;
                        }
                    }
                    else if (L[i].getName().compareToIgnoreCase(R[j].getName()) < 0) {
                        restaurants[k] = L[i];
                        i++;
                    }
                    else {
                        restaurants[k] = R[j];
                        j++;
                    }
                }
                
                else if ((L[i].getCustomerRating()) >= (R[j].getCustomerRating()) ) {
                    restaurants[k] = L[i];
                    i++;
                }
                else {
                    restaurants[k] = R[j];
                    j++;
                }
            
                k++;
            }            
        }
 
        //If any remaining elemtns in L[], they are copied
        while (i < firstArray) {
            restaurants[k] = L[i];
            i++;
            k++;
        }
 
        //If any remaining elemtns in R[], they are copied
        while (j < secondArray) {
            restaurants[k] = R[j];
            j++;
            k++;
        }

    }

    /**
     * 
     * @param restaurant A object of type restaurant
     * @return either true if the restaurant to be added is valid, else returns false if the restaurant id is a duplicate or invalid

     */
    public boolean addRestaurant(Restaurant restaurant) {

        restaurant.setID(dataChecker.extractTrueID(restaurant.getRepeatedID()));
        //extracts true ID  

        //same as addCustomer(Customer customer), so no comments needed
        
        if(dataChecker.isValid(restaurant) == false) {
            return false;
        }
    
        for (int i = 0; i < restaurantArray.size(); i++) {
            Restaurant current = restaurantArray.get(i);
            if (current.getID().equals(restaurant.getID())) {
                restaurantArray.remove(current);
                blacklist.add(current.getID());
                return false;
           }
        }

        for (int i = 0; i < blacklist.size(); i++) {
            if (blacklist.get(i).equals(restaurant.getID())) {
                return false;
            }
        }

        restaurantArray.add(restaurant);
            return true;

    }

    /**
     * 
     * @param restaurants The array to be inputted. All restaurants in the store
     * @return A boolean variable, which holds either true or false.
     */
    public boolean addRestaurant(Restaurant[] restaurants) {

        Boolean additionOfRestaurant = true;
        for (int i = 0; i < restaurants.length; i++) {
            if(!addRestaurant(restaurants[i])) {
                additionOfRestaurant = false;
            }
        }
        return additionOfRestaurant;
    }

    /**
     * 
     * @param id The input id of the restaurant required to be found
     * @return Either restaurant found or null
     */
    public Restaurant getRestaurant(Long id) {
        
        for (int i = 0; i < restaurantArray.size(); i++) {
            Restaurant restaurant = restaurantArray.get(i);
            if(restaurant.getID().equals(id)){
                return restaurant;
            }
        }   
        
        //if restaurant not found, return null 
        return null;
    }

    /**
     * 
     * @return array of restaurants which is but is sorted by ID in ascending order by merge sort
     */
    public Restaurant[] getRestaurants() {
    
        if (restaurantArray == null) {
            Restaurant[] restaurants = new Restaurant[0];
        }
       
        Restaurant[] restaurants = ConvertingFromArrayList(restaurantArray);
        Sort(restaurants,0,restaurants.length - 1, "ID");
       
        return restaurants;
    }

    /**
     * 
     * @param restaurants The array to be inputted. All restaurants in the store
     * @return The same input array of restaurants, which is now sorted by ID in ascending order by merge sort
     */
    public Restaurant[] getRestaurants(Restaurant[] restaurants) {
        
        Sort(restaurants,0,restaurants.length - 1, "ID");

        return restaurants;
    }

    /**
     * 
     * @return  array of restaurants which is but is sorted by name alphabetically by merge sort
     */
    public Restaurant[] getRestaurantsByName() {
        
        Restaurant[] restaurants = ConvertingFromArrayList(restaurantArray);
        Sort(restaurants,0,restaurants.length - 1, "Name");
       
        return restaurants;
    }

    /**
     * 
     * @return array of restaurants which is but is sorted by date established by merge sort
     */
    public Restaurant[] getRestaurantsByDateEstablished() {
        
        Restaurant[] restaurants = ConvertingFromArrayList(restaurantArray);
        Sort(restaurants,0,restaurants.length - 1, "date");
       
        return restaurants;
    }

    /**
     * 
     * @param restaurants The array to be inputted. All restaurants in the store
     * @return same array of restaurants which is sorted by date established by merge sort
     */
    public Restaurant[] getRestaurantsByDateEstablished(Restaurant[] restaurants) {
        
        Sort(restaurants,0,restaurants.length - 1, "date");

        return restaurants;
    }

    
    /**
     * 
     * @return array of restaurants which is sorted by number of warwick stars, so long as they have more than 1 warwick star
     */
    public Restaurant[] getRestaurantsByWarwickStars() {
        
        
        Restaurant[] restaurants = ConvertingFromArrayList(restaurantArray);
        
        for (int i = 0; i < restaurants.length; i++) {
            if (restaurants[i].getWarwickStars() > 0) {
                restaurantArraybyWarwickStars.add(restaurants[i]);
           }
        }
        
        Restaurant[] WarwickStarsArray = ConvertingFromArrayList(restaurantArraybyWarwickStars);
        Sort(WarwickStarsArray,0,WarwickStarsArray.length - 1, "stars");

        return WarwickStarsArray;
    }

    /**
     * 
     * @param restaurants The array to be inputted. All restaurants in the store
     * @return sorted array of restaurants by rating, descending from highest to lowest
     */
    public Restaurant[] getRestaurantsByRating(Restaurant[] restaurants) {
        
        Sort(restaurants ,0,restaurants.length - 1, "Rating");
        
        return restaurants;
    }

    /**
     * 
     * @param latitude stores input coordinate of resturants latitude
     * @param longitude stores input coordinate of resturants longitude
     * @return sorted array of restaurants closed to the university
     */
    public RestaurantDistance[] getRestaurantsByDistanceFrom(float latitude, float longitude) {
        
        Restaurant[] restaurants = ConvertingFromArrayList(restaurantArray); //converting from array list to array
        RestaurantDistance[] restaurantDistanceArray = new RestaurantDistance[restaurants.length];
        //creates a new array of type RestaurantDistance. The size of this will be the same as the restaurant array

        for (int i = 0; i < restaurants.length; i++) {
            restaurantDistanceArray[i] = new RestaurantDistance(restaurants[i], HaversineDistanceCalculator.inKilometres(restaurants[i].getLatitude(), restaurants[i].getLongitude(), latitude, longitude));
        }
        // linear search, calling the HaversineDistance method to convert the resturants locations to kilometres, for each restaurant.

        SortDistance(restaurantDistanceArray,0,restaurantDistanceArray.length - 1, "distance");
        //sorts the distances by smallest first, so nclosest restaurants to Warwick appear first

        return restaurantDistanceArray;
    }

    /**
     * 
     * @param restaurants The array to be inputted. All restaurants in the store
     * @param latitude stores input coordinate of resturants latitude
     * @param longitude stores input coordinate of resturants longitude
     * @return sorted array of restaurants closed to the university
     */
    public RestaurantDistance[] getRestaurantsByDistanceFrom(Restaurant[] restaurants, float latitude, float longitude) {
        
        RestaurantDistance[] restaurantDistanceArray = new RestaurantDistance[restaurants.length];
        //creates a new array of type RestaurantDistance. The size of this will be the same as the restaurant array
        
        for (int i = 0; i < restaurants.length; i++) {
          restaurantDistanceArray[i] = new RestaurantDistance(restaurants[i], HaversineDistanceCalculator.inKilometres(restaurants[i].getLatitude(), restaurants[i].getLongitude(), latitude, longitude));
        }
        
        SortDistance(restaurantDistanceArray,0,restaurantDistanceArray.length - 1, "distance");
        return restaurantDistanceArray;
    }

    /**
     * 
     * @param searchTerm Holds the string value that the user enters in the search bar to search for the restaurant place, cuisine, or name
     * @return sortedArrayContaining -  Array of restaurants that satisfy the user's search term
     */
    public Restaurant[] getRestaurantsContaining(String searchTerm) {

        if (searchTerm == "") {
            return new Restaurant[0];
        }

        searchTerm = searchTerm.trim();
        
        //convertAccentsFaster passes run-t tests, however when calling method the search bar is stuck on loading, so had to use regular convertAccents instead
        String searchTermConverted = stringFormatter.convertAccents(searchTerm);
        searchTermConverted = searchTermConverted.toLowerCase();

        String[] term = searchTermConverted.split("\\s+");

        String searchTermConvertedNew = term[0];
        for (int i = 1; i < term.length; i++) {
            searchTermConvertedNew = searchTermConvertedNew + " " + term[i]; //term = words
        }

        MyArrayList<Restaurant> containingArray = new MyArrayList<>();
        
        Restaurant[] restaurants = getRestaurantsByName();
        
        for (int i = 0; i < restaurants.length; i++) {;
            
            String NameOfRestaurant = restaurants[i].getName();
            NameOfRestaurant = stringFormatter.convertAccents(NameOfRestaurant);
            NameOfRestaurant = NameOfRestaurant.toLowerCase();
            
            String CuisineOfRestaurant = restaurants[i].getCuisine().toString();
            CuisineOfRestaurant = stringFormatter.convertAccents(CuisineOfRestaurant);
            CuisineOfRestaurant = CuisineOfRestaurant.toLowerCase();
            
            
            String PlaceOfRestaurant = convertToPlace.convert(restaurantArray.get(i).getLatitude(),restaurantArray.get(i).getLongitude()).getName();
            //Created a convertToPlace object in order to access the convert method. 
            //Would normally need to create a variable of type Place, but used getName() at the end, which produces a string, so can be stored as a String.
            PlaceOfRestaurant = stringFormatter.convertAccents(PlaceOfRestaurant);
            PlaceOfRestaurant = PlaceOfRestaurant.toLowerCase();
            
            if ((NameOfRestaurant.contains(searchTermConvertedNew) ) || (CuisineOfRestaurant.contains(searchTermConvertedNew) )     || (PlaceOfRestaurant.contains(searchTermConvertedNew))   ) {
                containingArray.add(restaurants[i]);
            }

        }

        Restaurant[] sortedArrayContaining = ConvertingFromArrayList(containingArray);
        Sort(sortedArrayContaining,0,sortedArrayContaining.length - 1, "Name");

        return sortedArrayContaining;
    }

    /**
     * 
     * @param restaurants The arrayList to be inputted. All restaurants in the store
     * @return An array called restArr, which contains every element from the arraylist, but now in an array, which can be used as a parameter for merge sorting
     */
    public static Restaurant[] ConvertingFromArrayList(MyArrayList<Restaurant> restaurants) {
        Restaurant[] restArr = new Restaurant[restaurants.size()]; //initalised the array which is the size of the restaurant array list
        for (int i = 0; i < restaurants.size(); i++) {
            restArr[i] = restaurants.get(i); //copies every element from the ArrayList to the array, via an iterative for loop
        }
        return restArr;
    }



/**
 * 
 * @param restaurants The array to be inputted
 * @param l Stores Left most integer value 
 * @param r Stores Right most integer value
 * @param requirement A condition that determines how the array should be sorted, i.e by distance in this situation
 */
private void SortDistance(RestaurantDistance restaurants[], int l, int r, String requirement) {
        
    if (r > l) {
        int m = (r + l) / 2 ;

        SortDistance(restaurants, l, m, requirement);
        SortDistance(restaurants, m + 1, r, requirement);  

        mergeDistance(restaurants,l,m,r, requirement);    
    }
}

/**
 * 
 * @param restaurants The array to be inputted
 * @param l Stores Left most integer value 
 * @param m Stores Midpoint integer value 
 * @param r Stores Right most integer value
 * @param requirement A condition that determines how the array should be sorted, i.e by distance in this situation
     */

private void mergeDistance(RestaurantDistance restaurants[], int l, int m, int r, String requirement) {
        
    int firstArray = m - l + 1;
    RestaurantDistance L[] = new RestaurantDistance[firstArray];  // Create temp arrays
    for (int i = 0; i < firstArray; ++i) { // Copy data to temp arrays
        L[i] = restaurants[l + i];
    }
    
    int secondArray = r - m;
    RestaurantDistance R[] = new RestaurantDistance[secondArray];
    for (int j = 0; j < secondArray; ++j) {
        R[j] = restaurants[1 + j + m];
    }

    

    int i = 0, j = 0; // setting the first and second subarrays's initial indexs
    int k = l; // setting the merged subarry array's initial index
        
    while (i < firstArray && j < secondArray) {
        
        if(requirement == "distance") {
            if (L[i].getDistance() < R[j].getDistance()) {
                restaurants[k] = L[i];
                i++;
            }
            else if (L[i].getDistance() > R[j].getDistance()) {
                restaurants[k] = R[j];
                j++;
            }

            else {
                if (L[i].getRestaurant().getID() <= R[i].getRestaurant().getID()) {
                    restaurants[k] = L[i];
                    i++;
                }
                else {
                    restaurants[k] = R[j];
                    j++;
                }
            }
            k++;
        }
    }

    //If any remaining elemtns in L[], they are copied
    while (i < firstArray) {
        restaurants[k] = L[i];
        i++;
        k++;
    }

    //If any remaining elemtns in R[], they are copied
    while (j < secondArray) {
        restaurants[k] = R[j];
        j++;
        k++;
    }


}


    


}
