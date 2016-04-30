/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataCollections;

import Logger.LogPrinter;
import datamanagement.User_dbo;
import datamanagement.UsersTable;

/**
 *
 * @author Srikanth
 */
public class ComputeGroundTruthUsersLocations {
 
    int noofprofilelocsassigned=0;
    int noofuserswithnogroundtrulocs=0;
    public double[] computeGroundTruthUserLocationsUsingProfileDescription(){
        double[] geocoor = null;
        
        return geocoor;
    }
    
    public double[] computeGroundTruthLocationsUsingPlaceField(){
        double[] geocoor = null;
        
        return geocoor;
    }
    
    public double[] computeGroundTruthLocationsFromTweets() {
        double[] geocoor = null;
        
        return geocoor;
    }
    
    public void setProfileBasedLocation(double[] profileloc,User_dbo user, boolean descloc){
        user.values[User_dbo.map.get("probased_geoinfo")].setValue(String.valueOf(!descloc));
        user.values[User_dbo.map.get("descbased_geoinfo")].setValue(String.valueOf(descloc));
        user.values[User_dbo.map.get("probased_lon")].setValue(String.valueOf(profileloc[0]));
        user.values[User_dbo.map.get("probased_lat")].setValue(String.valueOf(profileloc[1]));
        
    }
    
    public void setTweetBasedLocation(double[] tweetloc,User_dbo user) {
        user.values[User_dbo.map.get("tweetbased_geoinfo")].setValue("true");
        user.values[User_dbo.map.get("tweetbased_lon")].setValue(String.valueOf(tweetloc[0]));
        user.values[User_dbo.map.get("tweetbased_lat")].setValue(String.valueOf(tweetloc[1]));
        
    }
    
    public void updateGroundTruthLocationsOfUser(User_dbo user){
        
        double[] tweetloc = computeGroundTruthLocationsFromTweets();
        double[] profiledescloc = computeGroundTruthUserLocationsUsingProfileDescription();
        double[] profilelocfield = computeGroundTruthLocationsUsingPlaceField();
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
