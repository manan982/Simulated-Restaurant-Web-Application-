package uk.ac.warwick.cs126.util;

public class HaversineDistanceCalculator {

    private final static float R = 6372.8f;
    private final static float kilometresInAMile = 1.609344f;

    public static float inKilometres(float lat1, float lon1, float lat2, float lon2) {
    
        //convert floats to radians, so can be used by trigonometric functions
        double FirstLatitude = Math.toRadians(lat1); 
        double FirstLongitude = Math.toRadians(lon1);
        double SecondLatitude = Math.toRadians(lat2);
        double SecondLongitude = Math.toRadians(lon2);

       
        double a = ( (Math.cos(SecondLatitude)) * (Math.cos(FirstLatitude)) * (Math.pow(Math.sin((SecondLongitude - FirstLongitude)/2),2)) ) + Math.pow(Math.sin((SecondLatitude - FirstLatitude)/2),2);
        
        double c = 2 * Math.asin(Math.sqrt(a));

        double d = c * R;

        d = Math.round(d * 10)/10.0f; //rounds to one decimal place

        return (float)d; //return float of distance
        
    }

    public static float inMiles(float lat1, float lon1, float lat2, float lon2) {
        
        double FirstLatitude = Math.toRadians(lat1); //use same code as above
        double FirstLongitude = Math.toRadians(lon1);
        double SecondLatitude = Math.toRadians(lat2);
        double SecondLongitude = Math.toRadians(lon2);

       
        double a = Math.pow(Math.sin((SecondLatitude - FirstLatitude)/2),2) + ( (Math.cos(SecondLatitude)) * (Math.cos(FirstLatitude)) * (Math.pow(Math.sin((SecondLongitude - FirstLongitude)/2),2)) );             
        
        double c = 2 * Math.asin(Math.sqrt(a));

        double d = c * R;

        double convertedD = d/(kilometresInAMile); //convert km to mile by dividing by value given
        
        convertedD = Math.round(convertedD * 10)/10.0f; //round to one d.p again

        return (float)convertedD;

    }

}