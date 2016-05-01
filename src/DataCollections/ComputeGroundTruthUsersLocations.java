/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataCollections;

import Logger.LogPrinter;
import datamanagement.Tweet_dbo;
import datamanagement.TweetsTable;
import datamanagement.User_dbo;
import datamanagement.UsersTable;

/**
 *
 * @author Srikanth
 */
public class ComputeGroundTruthUsersLocations {
 
    int noofprofilelocsassigned=0;
    int noofuserswithnogroundtrulocs=0;
    public double[] computeGroundTruthUserLocationsUsingProfileDescription(User_dbo user){
        double[] geocoor = null;
        
        
        
        
        
        return geocoor;
    }
    
    public double[] computeGroundTruthLocationsUsingPlaceField(User_dbo user){
        double[] geocoor = null;
        
        
        
        
        
        return geocoor;
    }
    
    public double[] computeGroundTruthLocationsFromTweets(User_dbo user) {
        double[] geocoor = null;
        
        boolean available = true;
        int count = 100;
        long min_id = 0;
        Tweet_dbo tweets[];
        while(available) {
            tweets = TweetsTable.select(" user_id = "+user.values[User_dbo.map.get("user_id")].lnumber, min_id, count);
            if(tweets.length==0){
                available = false;
                continue;
            }
            for(Tweet_dbo t: tweets){
             if(t.values[Tweet_dbo.map.get("lon")].used&&t.values[Tweet_dbo.map.get("lat")].used){
                  
             }   
            }
        }
        
        
        
        return geocoor;
    }
    
    public void setProfileBasedLocation(double[] profileloc,User_dbo user, boolean descloc){
        user.values[User_dbo.map.get("probased_geoinfo")].setValue(String.valueOf(!descloc));
        user.values[User_dbo.map.get("descbased_geoinfo")].setValue(String.valueOf(descloc));
        user.values[User_dbo.map.get("probased_lon")].setValue(String.valueOf(profileloc[0]));
        user.values[User_dbo.map.get("probased_lat")].setValue(String.valueOf(profileloc[1]));
        boolean selected[] = new boolean[User_dbo.nooffields];
        selected[User_dbo.map.get("probased_geoinfo")] = true;
        selected[User_dbo.map.get("descbased_geoinfo")] = true;
        selected[User_dbo.map.get("probased_lon")] = true;
        selected[User_dbo.map.get("probased_lat")] = true;
        UsersTable.update(user, selected, " user_id = "+user.values[User_dbo.map.get("user_id")].lnumber);
    }
    
    public void setTweetBasedLocation(double[] tweetloc,User_dbo user) {
        
        user.values[User_dbo.map.get("tweetbased_geoinfo")].setValue("true");
        user.values[User_dbo.map.get("tweetbased_lon")].setValue(String.valueOf(tweetloc[0]));
        user.values[User_dbo.map.get("tweetbased_lat")].setValue(String.valueOf(tweetloc[1]));
        
        boolean selected[] = new boolean[User_dbo.nooffields];
        selected[User_dbo.map.get("tweetbased_geoinfo")] = true;
        selected[User_dbo.map.get("tweetbased_lon")] = true;
        selected[User_dbo.map.get("tweetbased_lat")] = true;
        UsersTable.update(user, selected, " user_id = "+user.values[User_dbo.map.get("user_id")].lnumber);
    }
    
    public void updateGroundTruthLocationsOfUser(User_dbo user){
        
        double[] tweetloc = computeGroundTruthLocationsFromTweets(user);
        double[] profiledescloc = computeGroundTruthUserLocationsUsingProfileDescription(user);
        double[] profilelocfield = computeGroundTruthLocationsUsingPlaceField(user);
        if(tweetloc==null){
            if(profilelocfield==null){
                if(profiledescloc!=null){
                    LogPrinter.printLog("User Location from Profile Description field is assigned "+profiledescloc.toString());
                    noofprofilelocsassigned++;
                    setProfileBasedLocation(profiledescloc,user,true);
                }
                else{
                    LogPrinter.printLog("No Profile Based Location is available for User. Skipping to Next User");
                    noofuserswithnogroundtrulocs++;
                }
                
            }else{
                LogPrinter.printLog("User Location from Profile Location field is assigned "+profilelocfield.toString());
                noofprofilelocsassigned++;
                setProfileBasedLocation(profilelocfield,user,false);
            }
        }
        else{
            LogPrinter.printLog("User Location from Tweets is assigned "+tweetloc.toString());
            setTweetBasedLocation(tweetloc,user);
            noofprofilelocsassigned++;
        }
        
    }
    
    public void updateGroundTruthLocationsOfAllUsers(){
        
        User_dbo[] users;
        int count = 100;
        long min_id = 0;
        boolean available = true;
        while(available) {
        users = UsersTable.select(null, min_id, count);
        if(users.length == 0) {
            available = false;
            continue;
        }
        min_id = users[users.length-1].values[User_dbo.map.get("id")].lnumber;
        for(User_dbo user: users) {
            LogPrinter.printLog("Selected an User & processing to caluclate ground truth location of the user "+user.values[User_dbo.map.get("screename")].string);
            updateGroundTruthLocationsOfUser(user);
 
        }
        LogPrinter.printLog("No Users with GroundTruth Locations: "+noofprofilelocsassigned+"No of Users with no GroundTruth Locations: "+noofuserswithnogroundtrulocs);
        
        }
        
         LogPrinter.printLog("Successfull All users selected were processed.");
        
        
    }
        
}
