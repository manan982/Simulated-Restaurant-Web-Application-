# CS126 WAFFLES Coursework Report [1234567]
<!-- This document gives a brief overview about your solution.  -->
<!-- You should change number in the title to your university ID number.  -->
<!-- You should delete these comments  -->
<!-- And for the latter sections should delete and write your explanations in them. -->
<!-- # <-- Indicates heading, ## <-- Indicates subheading, and so on -->

## CustomerStore
### Overview
<!-- A short description about what structures/algorithms you have implemented and why you used them. For example: -->
<!-- The template is only a guide, you are free to make any changes, add any bullets points, re-word it entirely, etc. -->
<!-- * <- is a bullet point, you can also use - minuses or + pluses instead -->
<!-- And this is *italic* and this is **bold** -->
<!-- Words in the grave accents, or in programming terms backticks, formats it as code: `put code here` -->
* I have used an `ArrayList` structure to store and process customers because it was easy to implement, and it got the job done
* I decided to use Merge sort to sort my customers by last name and ID, as I wanted my program to be efficient

### Space Complexity
<!-- Write here what you think the overall store space complexity is and gives a brief reason why. -->
<!-- <br> gives a line break -->
<!-- In tables, you don't really need the spaces, it only makes it look nice in text form -->
<!-- You can just do "CustomerStore|O(n)|I have used a single `ArrayList` to store customers." -->
Store         | Worst Case | Description
------------- | ---------- | -----------
CustomerStore | O(n)       | I have used a single `ArrayList` to store customers. <br>Where `n` is total customers added.
                            I have also use another single 'ArrayList' to store blacklisted customer Ids, which is a type long

### Time Complexity
<!-- Tell us the time complexity of each method and give a very short description. -->
<!-- These examples may or may not be correct, these examples have not taken account for other requirements like duplicate IDs and such.  -->
<!-- Again, the template is only a guide, you are free to make any changes. -->
<!-- If you did not do a method, enter a dash "-" -->
<!-- Technically, for the getCustomersContaining(s) average case, you are suppose to do it relative to s -->
<!-- Note, that this is using the original convertToAccents() method -->
<!-- So O(a*s + n*(a*t + t)) -->
<!-- Where a is the amount of accents -->
<!-- Where s is the length of the search term String  -->
<!-- Where t is the average length of a name -->
<!-- Where n is the total number of customers -->
<!-- But you can keep it simple -->

Method                           | Average Case     | Description
-------------------------------- | ---------------- | -----------
addCustomer(Customer c)          | O(1)             | Array add is constant time. Checking whether the customer 'c' meets certain critera before adding it to the customer store
addCustomer(Customer[] c)        | O(n)             | Add all customers <br>`n` is the length of the input array. So O(n) as time complexity is dependant on the size of the array
getCustomer(Long id)             | O(n)             | Linear search <br>`n` to get customers based on input id
getCustomers()                   | O(n + n*log n)          | Merge sort, n is total customers. O(n) for getting customers using linear search. Merge sort is O nlog n, so addition of these gives O(n + n log n)
getCustomers(Customer[] c)       |  O(n + n*log n)        | Merge sort <br>`n` is the length of the input array
getCustomersByName()             | O(n + n*log n)           | MergeSort <br>`n` is total customers in the store. Arraylist converted to array, and customers retrived O(n), and then merge sort of O (n log )
getCustomersByName(Customer[] c) | O(n + n*log n)| Merge sort on the customers in the array 'c'
getCustomersContaining(String s) | O(a + n*(a + b)) | Accents are converted from s, and searches all customers <br>`a` is the average time it takes to convert accents <br>`n` is total customers <br>`b` is average string search time 

<!-- Don't delete these <div>s! -->
<!-- And note the spacing, do not change -->
<!-- This is used to get a page break when we convert your report to PDF to read when marking -->
<!-- It is not the end of the world if you do remove, it just makes it harder to read if you do -->
<!-- On things you can remove though, you should remember to remove these comments -->
<div style="page-break-after: always;"></div>

## FavouriteStore
### Overview
* I have used `ArrayList` to store favourites ... 
* I used `mergeSort` to sort by date favourited and by ID 
* To get favourites by Customer and Restaurant ID, I created temporary arraylist for each method, and for matching customer IDs, I added them to the temp arraylist
* To get common, not common and missing faves, I used linear searches to get each customers favourites. Then I used loops that meant the restaurants were added to an array
* To get top ... 

### Space Complexity
Store          | Worst Case | Description
-------------- | ---------- | -----------
FavouriteStore | O(n)     | I have used `ArrayList` ... <br>Where `...` is ...

### Time Complexity
Method                                                          | Average Case     | Description
--------------------------------------------------------------- | ---------------- | -----------
addFavourite(Favourite f)                                       | O(1)           | Description <br>`...` is ...
addFavourite(Favourite[] f)                                     | O(n)           | Description <br>`...` is ...
getFavourite(Long id)                                           | O(n)           | Description <br>`...` is ...
getFavourites()                                                 | O(n + n* log n)           | Description <br>`...` is ...
getFavourites(Favourite[] f)                                    | O(n+ n*log n)           | Description <br>`...` is ...
getFavouritesByCustomerID(Long id)                              | O(n + n* log n)           | Description <br>`...` is ...
getFavouritesByRestaurantID(Long id)                            | O(n + n* log n)           | Description <br>`...` is ...
getCommonFavouriteRestaurants(<br>&emsp; Long id1, Long id2)    | O(a+b+ c logc)           | a is time taken to get faves of customer 1. b is the time taken to get faves of customer 2. C is the number of common faves between both customers. Gets all favourites of each customerID
getMissingFavouriteRestaurants(<br>&emsp; Long id1, Long id2)   | O(a+b+ c log)           | Description <br>`...` is ...
getNotCommonFavouriteRestaurants(<br>&emsp; Long id1, Long id2) | O(a+b+ c log)           | Description <br>`...` is ...
getTopCustomersByFavouriteCount()                               | O(...)           | Description <br>`...` is ...
getTopRestaurantsByFavouriteCount()                             | O(...)           | Description <br>`...` is ...

<div style="page-break-after: always;"></div>

## RestaurantStore
### Overview
* I have used `ArrayList` to store restaurants as it was a simple efficient way to get the program working with a relatively low boot time 
* I used `merge sort` to sort due to effiency and speed, in order to sort by ID, name, date, warwick stars, or rating

### Space Complexity
Store           | Worst Case | Description
--------------- | ---------- | -----------
RestaurantStore | O(n)     | I have used `...` ... <br>Where `...` is ...

### Time Complexity
Method                                                                        | Average Case     | Description
----------------------------------------------------------------------------- | ---------------- | -----------
addRestaurant(Restaurant r)                                                   | O(1)           | Constant time to check if restaurant is valid and add to blacklist
addRestaurant(Restaurant[] r)                                                 | O(n)           | n is length of array, 'r' is added to restaurant store
getRestaurant(Long id)                                                        | O(n)           | Linear seach to get restaurant based on input id
getRestaurants()                                                              | O(n + n*log n)           | n is time taken to set each restaurants validity. Then merge sort is carried out
getRestaurants(Restaurant[] r)                                                | O(n + n*log n)           | 
getRestaurantsByName()                                                        | O(n + n*log n)           | 
getRestaurantsByDateEstablished()                                             | O(n + n*log n)           | 
getRestaurantsByDateEstablished(<br>&emsp; Restaurant[] r)                    | O(n + n*log n)           | getRestaurantsByWarwickStars()                                                | O(n + n*log n)           | linear search to retrieve all restaurants with more than one star, then a merge sort of (O log n)
getRestaurantsByRating(Restaurant[] r)                                        | O(n + n*log n)           | 
getRestaurantsByDistanceFrom(<br>&emsp; float lat, float lon)                 | O(n + n*log n)           |n is time taken to get restaurant distance array calculated from haversine calc, then merge sort carried out 
getRestaurantsByDistanceFrom(<br>&emsp; Restaurant[] r, float lat, float lon) | O(n + n*log n)           | tRestaurantsContaining(String s)                                            |  O(a + n*(a + b))           | Accents are converted from s, and searches all restaurants <br>`a` is the average time it takes to convert accents <br>`n` is total restaurants <br>`b` is average string search time 


<div style="page-break-after: always;"></div>

## ReviewStore
### Overview
* I have used `ArrayList` to store reviews ... 
* I used `mergesort` to sort by rating, date reviewed, and ID 
* To get ... 
* To get top ... 

### Space Complexity
Store           | Worst Case | Description
--------------- | ---------- | -----------
ReviewStore     | O(n)     | I have used `...` ... <br>Where `...` is ...

### Time Complexity
Method                                     | Average Case     | Description
------------------------------------------ | ---------------- | -----------
addReview(Review r)                        | O(1)           | Description <br>`...` is ...
addReview(Review[] r)                      | O(n)           | Description <br>`...` is ...
getReview(Long id)                         | O(n)           | Description <br>`...` is ...
getReviews()                               | O(n + n*log n)           | Description <br>`...` is ...
getReviews(Review[] r)                     | O(n + n*log n)           | Description <br>`...` is ...
getReviewsByDate()                         | O(n + n*log n)           | Description <br>`...` is ...
getReviewsByRating()                       | O(n + n*log n)           | Description <br>`...` is ...
getReviewsByRestaurantID(Long id)          | O(n + n*log n)           | Description <br>`...` is ...
getReviewsByCustomerID(Long id)            | O(n + n*log n)           | Description <br>`...` is ...
getAverageCustomerReviewRating(Long id)    | O(a + n)           | Description <br>`a` is time taken to get all customer ratings. n is number of customer reviews
getAverageRestaurantReviewRating(Long id)  | O(a + n)           | Description <br>`a` is time taken to get all restaurant ratings. n is number of restaurant reviews
getCustomerReviewHistogramCount(Long id)   | O(a + n)           | Description <br>`a.` is time taken to calculate histogram bins, n is number of customer reviews.
getRestaurantReviewHistogramCount(Long id) | O(a + n)           | Description <br>`a` is time taken to calculate histogram bins, n is number of restaurant reviews
getTopCustomersByReviewCount()             | O(...)           | Description <br>`...` is ...
getTopRestaurantsByReviewCount()           | O(...)           | Description <br>`...` is ...
getTopRatedRestaurants()                   | O(...)           | Description <br>`...` is ...
getTopKeywordsForRestaurant(Long id)       | O(...)           | Description <br>`...` is ...
getReviewsContaining(String s)             | O(a + n*(a + b))          | Accents are converted from s, and searches all reviews <br>`a` is the average time it takes to convert accents <br>`n` is total reviews <br>`b` is average string search time 

<div style="page-break-after: always;"></div>

## Util
### Overview
* **ConvertToPlace** 
    * ...
* **DataChecker**
    * ...
* **HaversineDistanceCalculator (HaversineDC)**
    * ...
* **KeywordChecker**
    * ...
* **StringFormatter**
    * ...

### Space Complexity
Util               | Worst Case | Description
-------------------| ---------- | -----------
ConvertToPlace     | O(n)     | ...
DataChecker        | O(n)     | ...
HaversineDC        | O(1)     | ... 
KeywordChecker     | O(n)     | ...
StringFormatter    | O(n)     | ...

### Time Complexity
Util              | Method                                                                             | Average Case     | Description
----------------- | ---------------------------------------------------------------------------------- | ---------------- | -----------
ConvertToPlace    | convert(float lat, float lon)                                                      | O(1)           | ... 
DataChecker       | extractTrueID(String[] repeatedID)                                                 | O(1)           | ...
DataChecker       | isValid(Long id)                                                                   | O(n +(log n) + c)           | ...
DataChecker       | isValid(Customer customer)                                                         | O(1)           | series of checks performed to ensure customer fulfills certain requirements
DataChecker       | isValid(Favourite favourite)                                                       | O(1)           | series of checks performed to ensure favourite fulfills certain requirements
DataChecker       | isValid(Restaurant restaurant)                                                     | O(1)           | series of checks performed to ensure restaurant fulfills certain requirements
DataChecker       | isValid(Review review)                                                             | O(1)           | series of checks performed to ensure review fulfills certain requirements
HaversineDC       | inKilometres(<br>&emsp; float lat1, float lon1, <br>&emsp; float lat2, float lon2) | O(1)           | calculations done to calculate distance between two points in kilometres
HaversineDC       | inMiles(<br>&emsp; float lat1, float lon1, <br>&emsp; float lat2, float lon2)      | O()           | calculations done to calculate distance between two points in miles
KeywordChecker    | isAKeyword(String s)                                                               | O(log n)           | length of the keyword array is n, then a binary search used to find string
StringFormatter   | convertAccentsFaster(String s)                                                     | O(n)           | Hashmap is looped through, and every latter is converted like an accented string. n is the length of the string
