/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataCollections;


import Logger.LogPrinter;
import java.sql.SQLException;

import java.util.ListIterator;
import twitter4j.Location;
import twitter4j.ResponseList;
import twitter4j.Trend;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.api.TrendsResources;

/**
 *
 * @author Srikanth
 */
public class TrendsCollections {
    
    Twitter twitter; 
    TrendsResources trendsresources;
    ResponseList<Location> locations;        
    Trends trends;
    LocationHelper lochelper;
    HashTagHelper htaghelper;
    
    public TrendsCollections() {
        twitter = TwitterFactory.getSingleton();
        trendsresources = twitter.trends();
        lochelper =new LocationHelper();
        htaghelper = new HashTagHelper();
    }
    
    
    public void collectCurrentTrendingLocations()  {
        try{
           
        locations = trendsresources.getAvailableTrends();
        
        LogPrinter.printLog("Locations for Trends are Received");
        
        Location[] locs = new Location[locations.size()];
        for(int i=0; i<locations.size(); i++){
            locs[i] = locations.get(i);
        }
        
        LogPrinter.printLog("Locations are splitted");
        
        lochelper.insertLocations(locs);
        
        LogPrinter.printLog("Locations are inserted into Database");
        
        
        collectLatestTrendingHashTags();
        }
        catch(TwitterException | InterruptedException e) {
            if(e instanceof TwitterException) {
            System.out.println("Exception Message: "+e.getMessage());
            try{
            lochelper.closeDBConnection();
            }
            catch(Exception sqlexception) {
            }
        }
        }
        
    }
    
    
    public void collectLatestTrendingHashTags () throws InterruptedException {
     
     ListIterator<Location> i = locations.listIterator();
    
     while(i.hasNext()) {
         Location l = i.next();
         
         try {
         trends = trendsresources.getPlaceTrends(l.getWoeid());
         }
         catch(TwitterException e) {
         Thread.sleep((e).getRetryAfter()*1000+5000);
         i.previous();
         continue;
         
         }
         
         System.out.println("\nLocation:- CountryCode: "+l.getCountryCode()+" Country Name: "+l.getCountryName()+" Name: "+l.getName()+" PlaceCode: "+l.getPlaceCode()
                 +" PlaceName: "+l.getPlaceName()+" URL: "+l.getURL()+" Woeid: "+l.getWoeid()+"\n");
         Trend[] ts = trends.getTrends();
         
         for(int t=0; t<ts.length;t++ ) 
         {
             System.out.println("Trend "+t+ts[t].getName()+" "+ts[t].toString());
         }
         htaghelper.insertTrends(ts);
     }  
    }
    
    
}
