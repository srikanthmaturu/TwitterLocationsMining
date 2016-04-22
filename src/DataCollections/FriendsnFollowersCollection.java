/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataCollections;

import DataAccess.TwitterResources;
import Logger.LogPrinter;
import datamanagement.User_dbo;
import datamanagement.UsersTable;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import twitter4j.PagableResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.api.FriendsFollowersResources;

/**
 *
 * @author Srikanth
 */
public class FriendsnFollowersCollection {
    Twitter twitter ;
    FriendsFollowersResources ffresources;
    UserHelper userhelper;
    
    public FriendsnFollowersCollection() {
        twitter =  TwitterResources.getTwitterSingleton();
        ffresources = twitter.friendsFollowers();
        userhelper = new UserHelper();
        
    }
    public ArrayList<User_dbo> collectfriendsfollowersofuser(User_dbo cuserdbo) throws InterruptedException{
        long cursor = -1;
        PagableResponseList<User> list;
        User user;
        User_dbo userdbo;
        ArrayList<User_dbo> userdbolist = new ArrayList<>();
        boolean available = true;
        Logger.LogPrinter.printLog("Retrieving friends of the user  "+cuserdbo.values[User_dbo.map.get("screename")].string);
        while(available){
        
        try{
        list = ffresources.getFriendsList(cuserdbo.values[User_dbo.map.get("user_id")].lnumber, cursor);
        ListIterator li = list.listIterator();
        while(li.hasNext()){
            user = (User)li.next();
            LogPrinter.printLog(""+user.getName());
            userdbo = userhelper.convertUserToUser_dbo(user);
            userdbolist.add(userdbo);
            //LogPrinter.printLog(" Friend retrieved"+userdbo.values[User_dbo.map.get("screename")].string);
        }
        if(list.hasNext()){
        cursor = list.getNextCursor();
        }
        else{
            available = false;
        }
        }
        catch(Exception e){
            e.printStackTrace();
            Logger.LogPrinter.printLog(" Sleeping... rate limit exceeded will try after "+900*1000);
           Thread.sleep(900*1000+500);
          
           LogPrinter.printLog(" Message "+e.getMessage());
        }
        }
        LogPrinter.printLog("No of friends retrieved: "+userdbolist.size());
        available = true;
        cursor = 0;
        Logger.LogPrinter.printLog("Retrieving followers of the user  "+cuserdbo.values[User_dbo.map.get("screename")].string);
        while(available){
        
        try{
        list = ffresources.getFollowersList(cuserdbo.values[User_dbo.map.get("user_id")].lnumber, cursor);
        ListIterator li = list.listIterator();
        while(li.hasNext()){
            user = (User)li.next();
            userdbo = userhelper.convertUserToUser_dbo(user);
            userdbolist.add(userdbo);
        }
        if(list.hasNext()){
        cursor = list.getNextCursor();
        }
        else{
            available = false;
        }
        }
        catch(Exception e){
            Logger.LogPrinter.printLog(" Sleeping... rate limit exceeded will try after "+900*1000);
            Thread.sleep(900*1000+500);
           
           LogPrinter.printLog(" Message "+e.getMessage());
        }
        }
        LogPrinter.printLog("No of friends/follwers retrieved: "+userdbolist.size());
        return userdbolist;
    }
        
    
    
    public void insertFriendsFollowers(ArrayList<User_dbo> userdbolist){
        if(!userdbolist.isEmpty()){
            LogPrinter.printLog("Insering Friends and Followers....No of users friends/followers"+userdbolist.size());
            ListIterator li = userdbolist.listIterator();
            while(li.hasNext()){
                User_dbo userdbo = (User_dbo)li.next();
                User_dbo[] users = UsersTable.select(" user_id = "+userdbo.values[User_dbo.map.get("user_id")].lnumber, 0, 2);
                if(users.length==0){
                    UsersTable.insert(userdbo);
                }
                else{
                    if(!users[0].values[User_dbo.map.get("udetails_processed")].bool){
                        UsersTable.delete(userdbo.values[User_dbo.map.get("user_id")].lnumber);
                        userdbo.values[User_dbo.map.get("udetails_processed")].setValue("true");
                        UsersTable.insert(userdbo);
                    }
                }
            }
        }
        LogPrinter.printLog("Insering Friends and Followers is complete");
    }
    
     
    
    public void updateDatabasewithFriends_followers() throws InterruptedException{
        User_dbo[] users;
        int count = 100;
        long min_id = 0;
        boolean available = true;
        while(available) {
        users = UsersTable.select("fri_fol_processed = false ", min_id, count);
        if(users.length == 0) {
            available = false;
            continue;
        }
        Logger.LogPrinter.printLog("Retrieving the Friends_Followers of all users....");
        min_id = users[users.length-1].values[User_dbo.map.get("id")].lnumber;
        for(User_dbo user: users) {
            Logger.LogPrinter.printLog("Processing friends/followers of the user  "+user.values[User_dbo.map.get("screename")].string);
            insertFriendsFollowers(collectfriendsfollowersofuser(user));   
            user.values[User_dbo.map.get("fri_fol_processed")].bool = true;
            long userid = user.values[User_dbo.map.get("user_id")].lnumber;
            UsersTable.update(user, " user_id =  "+userid);
        }
         Logger.LogPrinter.printLog("Friends and Followers of all the Users are processed..");
        }
    }
}
