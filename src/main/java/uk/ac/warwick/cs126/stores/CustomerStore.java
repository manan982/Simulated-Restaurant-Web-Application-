package uk.ac.warwick.cs126.stores;

import uk.ac.warwick.cs126.interfaces.ICustomerStore;
import uk.ac.warwick.cs126.models.Customer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.io.IOUtils;

import uk.ac.warwick.cs126.structures.MyArrayList;

import uk.ac.warwick.cs126.util.DataChecker;
import uk.ac.warwick.cs126.util.StringFormatter;

public class CustomerStore implements ICustomerStore {

    private MyArrayList<Customer> customerArray; 
    private DataChecker dataChecker;
    private MyArrayList<Long> blackListedArray; // ArrayList to store duplicate IDs which are blacklisted
    private StringFormatter stringFormatter; // Used in the getCustomersContaining() method
    

    public CustomerStore() {
        
        // Initialise variables here
        customerArray = new MyArrayList<>();
        blackListedArray = new MyArrayList<>();
        dataChecker = new DataChecker();
        stringFormatter = new StringFormatter();

    }


    //https://www.geeksforgeeks.org/merge-sort/

    /**
     * Function that carried out the sorting of 2 halves of the array inputed called customers
     * @param customers The array to be inputted
     * @param left Stores Left most integer value pointer
     * @param right Stores Right most integer value pointer
     * @param requirement A condition that determines how the array should be sorted, i.e different String values will causes ordering by either id or last name
     */
    private void Sort(Customer customers[], int left, int right, String requirement) {
        
        if (right > left) { 
            int mid = (left + right) / 2 ; //creating a midpoint value

            Sort(customers, left, mid, requirement); //recursively calling function, once between first item to midpoint, then midpoint to last element, to sort the 2 halves
            Sort(customers, mid + 1, right, requirement);  

            merge(customers,left,mid,right, requirement);  //calling merge function to merge the 2 halves
        }
    }

    //time complexitiy = 2T(n/2) - time to sort sub-arrays + O(n) - time to merge entire array, which comes out to O(n log n). Holds for worst, avg. and best case as always dividing array into 2 and merging
    //space complexity = O(n) as in every recursion temporary arrays are created

    /** Method that performs the merging of the sorted array in Sort
     * 
     *@param customers The array to be inputted
     * @param left Stores Left most integer value 
     * @param right Stores Right most integer value
     * @param mid Stores Midpoint integer value 
     * @param requirement A condition that determines how the array should be sorted, i.e different String values will causes ordering by either id or last name
     */
    private void merge(Customer customers[], int left, int mid, int right, String requirement){
        
        int firstArray = mid - left + 1; //finds the size of the first subarray required for merging

        Customer L[] = new Customer[firstArray];  // first temporary array is created
        for (int i = 0; i < firstArray; ++i) { 
            L[i] = customers[left + i]; // date is copied to first temporary array
        }
        
        int secondArray = right - mid; //finds the size of the second subarray required for merging
        Customer R[] = new Customer[secondArray]; // second temporary array is created
        for (int h = 0; h < secondArray; ++h) {
            R[h] = customers[1 + h + mid]; // date is copied to second   temporary array
        }
        
        //Merging occurs now
 
        int i = 0, h = 0; // setting the first and second subarrays's initial index
        int k = left; // setting the merged subarry array's initial index
        
        while (i < firstArray && h < secondArray) {
            
            if (requirement == "id") { //if paramater required for searching is id, then this code is run   
                if (L[i].getID() <= R[h].getID()) {
                    customers[k] = L[i];
                    i++; 
                }   //if equal to left array (L[i]), then increment i, if R[i], then increment h
                else {
                    customers[k] = R[h];
                    h++;
                }
                k++; //increment k at the end of each if statement 
                
            }

            else { //if parameter for searching is not id, and sorting by name is required, this code is run. 
                if (L[i].getLastName().compareToIgnoreCase(R[h].getLastName()) > 0) {
                    customers[k] = R[h];
                    h++;
                }
                
                else if (L[i].getLastName().compareToIgnoreCase(R[h].getLastName()) < 0) {
                    customers[k] = L[i];
                    i++;
                }   

                //Will sort alphabetically by last name, if last names are equal sort by first names

                else {

                    if (L[i].getFirstName().compareToIgnoreCase(R[h].getFirstName()) > 0) {
                        customers[k] = R[h];
                        h++;
                    }

                    else if(L[i].getFirstName().compareToIgnoreCase(R[h].getFirstName()) < 0) {
                        customers[k] = L[i];
                        i++;
                    }
                    
                   // Will sort alphabetically by first name, if first names are equal sort by ID
                    else {
                        if(L[i].getID() <= R[h].getID()) {
                            customers[k] = L[i];
                            i++;
                        }
                        else {
                            customers[k] = R[h];
                            h++;
                        }
                    }
                }
                k++;
            }

        }

        //If any remaining elemtns in L[], they are copied
        while (i < firstArray) {
            customers[k] = L[i];
            i++;
            k++;
        }
 
        //If any remaining elemtns in R[], they are copied
        while (h < secondArray) {
            customers[k] = R[h];
            h++;
            k++;
        }
    }



    public Customer[] loadCustomerDataToArray(InputStream resource) {
        Customer[] customerArray = new Customer[0];

        try {
            byte[] inputStreamBytes = IOUtils.toByteArray(resource);
            BufferedReader lineReader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            int lineCount = 0;
            String line;
            while ((line=lineReader.readLine()) != null) {
                if (!("".equals(line))) {
                    lineCount++;
                }
            }
            lineReader.close();

            Customer[] loadedCustomers = new Customer[lineCount - 1];

            BufferedReader csvReader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            int customerCount = 0;
            String row;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            csvReader.readLine();
            while ((row = csvReader.readLine()) != null) {
                if (!("".equals(row))) {
                    String[] data = row.split(",");

                    Customer customer = (new Customer(
                            Long.parseLong(data[0]),
                            data[1],
                            data[2],
                            formatter.parse(data[3]),
                            Float.parseFloat(data[4]),
                            Float.parseFloat(data[5])));

                    loadedCustomers[customerCount++] = customer;
                }
            }
            csvReader.close();

            customerArray = loadedCustomers;

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return customerArray;
    }



    /**Adds a customer to the store, provided the id is valid, and not a blacklisted duplicate
     * 
     * @param customer A customer object 
     * @return either true if the customer to be added is valid, else returns false if the customers id is a duplicate or invalid
     */
    public boolean addCustomer(Customer customer) { 

        if(dataChecker.isValid(customer) == false) { //Checks if customer is valid using isValid() from the DataChecker class
            return false;
        }

        for (int i = 0; i < customerArray.size(); i++) { //linear searching through the customers array
            Customer current = customerArray.get(i); //creating a object of type Customer called current, and this value will iterate every customer in the customerArray
            
            if (current.getID().equals(customer.getID())) { //Get the ID of the object we created, and compare it to the input customers ID
                customerArray.remove(current); //If there are equal, that means the ID is a duplicate, and remove it from the ArrayList
                blackListedArray.add(current.getID()); //Add this ID to a blackList ArrayList instead
                return false;
           }
        }

        for (int i = 0; i < blackListedArray.size(); i++) { //iterate through the blacklist to see if the input customer id has been blacklisted
            if (blackListedArray.get(i).equals(customer.getID())) { 
                return false; //return false if id has been blacklisted
            }
        }

        customerArray.add(customer); //Add the customer to the arraylist
            return true;
    }

/** Adds valid customer objects from array into the store, by looping through the customerArray and calling addCustomer(Customer customer) on each customer
 * 
 * @param customers The array to be inputted. All customers in the store
 * @return A boolean variable, which holds either true or false. 
 */
    public boolean addCustomer(Customer[] customers) {
        
        Boolean additionOfCustomer = true; //variable to store boolean true or false
        for (int i = 0; i < customers.length; i++) { //linear searching through the customers array
            if(!addCustomer(customers[i])) { //if any item in the customer array causes the addCustomer(Customer customer) to return false, then return false
                additionOfCustomer = false;
            }
        }

        return additionOfCustomer; //return either true or false
        
    }

    /**Method to return a customer if the id is found corresponding to a customer
     * 
     * @param id The input id of the customer required to be found
     * @return Either customer found or null
     */
    public Customer getCustomer(Long id) {

        for (int i = 0; i < customerArray.size(); i++) { //iterate through the customer Array 
            Customer customer = customerArray.get(i);
            if(customer.getID().equals(id)){  //if the customer object created has the same id as the input id, then return the customer
                return customer;
            }
    
        }   
        return null; // return null if customer not found, 
    } 

    /**
     * 
     * @return array of customers which is but is sorted by ID in ascending order by merge sort
     */
    public Customer[] getCustomers() { //sort by ID
        
        if (customerArray == null) {
            Customer[] customers = new Customer[0];
            return customers; //check if customerArray is  null, return empty Customer object if empty
        }

        Customer[] customers = new Customer[customerArray.size()]; //create a new customer object which is the size of the customerArray
        for (int i = 0; i < customers.length; i++) {
            customers[i] = customerArray.get(i); //converting from ArrayList to array
        }

        Sort(customers,0,customers.length - 1, "id"); //run the merge sort function, sort by "id", take customers array as a parameter
        return customers;
    }

    /**
     * 
     * @param customers The array to be inputted. All customers in the store
     * @return customers - Returns the same input array, but is sorted by ID in ascending order
     */
    public Customer[] getCustomers(Customer[] customers) {
        
        //no need to convert from ArrayList to array, as Customer[] customers given

        Sort(customers,0,customers.length - 1, "id");  //run the merge sort function, sort by "id", take customers array as a parameter

        return customers;
    }

    /** 
     * @return array of customers which is but is sorted by last name by merge sort. If no last name, then sorted by first name instead
     */
    public Customer[] getCustomersByName() {
        
        if (customerArray == null || customerArray.size() == 0){ //checking to see  customerArray is null or empty , return empty Customer object if empty
            Customer[] customers = new Customer[0];
            return customers;
        }

        
        Customer[] customers = new Customer[customerArray.size()]; //converting to array from arraylist, as customer array is not given as a parameter
        for (int i = 0; i < customers.length; i++) {
            customers[i] = customerArray.get(i);
        }

        Sort(customers,0,customers.length - 1, "LastName"); //run the merge sort function, sort by "last name ", take customers array as a parameter
        return customers;
    }

    /** 
     *  @param customers The array to be inputted. All customers in the store
     *  @return array of customers which is but is sorted by last name by merge sort.
     */
    public Customer[] getCustomersByName(Customer[] customers) {
        
        Sort(customers,0,customers.length - 1, "LastName"); //run the merge sort function, sort by "last name ", take customers array as a parameter
        return customers;
    }

    /**A method to return an array of all customers that match the the given entry in the search bar
     * 
     * @param searchTerm Holds the string value that the user enters in the search bar to search for the customer name
     * @return sortedArrayContaining -  Array of custormers that satisfy the user's search term
     */
    public Customer[] getCustomersContaining(String searchTerm) {
    
        if (searchTerm == "") { // if empty String is entered in search bar, no results returned. Return null array
            return new Customer[0];
        }

        searchTerm = searchTerm.trim(); //trim method used to remove whitespace from the start and the end of a string
        
        String searchTermConverted = stringFormatter.convertAccents(searchTerm); //using string formatter to implemrent the convert Accents method
        searchTermConverted = searchTermConverted.toLowerCase(); //making all characters lower case.

        String[] term = searchTermConverted.split("\\s+"); //.split() splits a string into multiple strings

        String searchTermConvertedNew = term[0]; //turning the string into an array of words
        for (int i = 1; i < term.length; i++) {
            searchTermConvertedNew = searchTermConvertedNew + " " + term[i]; 
        }

        MyArrayList<Customer> containingArray = new MyArrayList<>();
        //creating an ArrayList which exists in this method only, rather than being global, so it is cleared after each search.
        // This is initalised and will store the customers that match the search term criteria

        Customer[] customers = getCustomersByName(); //getting all customers by name by calling this method and storing them in an array called customerd
        
        for (int i = 0; i < customers.length; i++) { //for loop to linear search through customer array
            
            String customerFirstName = customers[i].getFirstName(); //create a string of customer first name, which gets every customer in the array's first name
            customerFirstName = stringFormatter.convertAccents(customerFirstName); //using string formatter to call convert accents method
            customerFirstName = customerFirstName.toLowerCase(); //makes all characters in first name lower case
            
            String customerLastName = customers[i].getLastName(); //create a string of customer last name, which gets every customer in the array's last name
            customerLastName = stringFormatter.convertAccents(customerLastName); //using string formatter to call convert accents method
            customerLastName = customerLastName.toLowerCase(); //makes all characters in last name lower case
            
            if ((customerFirstName.contains(searchTermConvertedNew) ) || (customerLastName.contains(searchTermConvertedNew) )) {
                containingArray.add(customers[i]);
            } //if statement, if either of the strings contain any of the search term, then add this to the ArrayList

        }

        Customer[] sortedArrayContaining = new Customer[containingArray.size()]; //converts the ArrayList containingArray into an array, which can be sorted
        for (int i = 0; i < containingArray.size(); i++) {
            sortedArrayContaining[i] = containingArray.get(i);
        }

        Sort(sortedArrayContaining,0,sortedArrayContaining.length - 1, "LastName"); //merge sort the array so they are sorted by last name

        return sortedArrayContaining; //returns the sorted array sorted by last name

    }

}
