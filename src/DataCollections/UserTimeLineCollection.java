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
import java.util.ListIterator;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.api.TimelinesResources;

/**
 *
 * @author Srikanth
 */
public class UserTimeLineCollection {
    public Twitter twitter;
    public TimelinesResources timelinesres;
    public TweetHelper tweethelper;
    public Users_EdgesCollectionFromTweets useredgecollections;
    
    public UserTimeLineCollection() {
        twitter = TwitterFactory.getSingleton();
        timelinesres = twitter.timelines();
        tweethelper = new TweetHelper();
        useredgecollections = new Users_EdgesCollectionFromTweets();
    }
    
    public void collect_InsertTimeLineOfUser(User_dbo user) throws InterruptedException {
        
        Paging p = new Paging();
        int count = 20;
        p.setCount(count);
        long max_id,since_id;
        int totaltweets =0;
        long timestamp = -1;
        int nooftweets = 0;
        if(user.values[User_dbo.map.get("max_id")].used) {
            max_id = user.values[User_dbo.map.get("max_id")].lnumber;
            p.setMaxId(max_id);
        }
        else{
            max_id = -1;
        }
        if(user.values[User_dbo.map.get("since_id")].used) {
            since_id = user.values[User_dbo.map.get("since_id")].lnumber;
        }
        else{
            since_id = -1;
        }
        
        
        ResponseList<Status> statuses = null;
        boolean available = true;
        while(available) {
            
        try{
            LogPrinter.printLog("Retrieving some more tweets....");
            statuses = timelinesres.getUserTimeline(user.values[User_dbo.map.get("user_id")].lnumber, p);
        }
        catch(Exception e){
            e.printStackTrace();
            TwitterException te = (TwitterException)e;
            if(te.exceededRateLimitation()) {
                LogPrinter.printLog("Rate Limited Reached.. Sleeping.. for ms "+900*1000);
            Thread.sleep(900*1000+500);
            } else{
               
               return;
            }
            
            
        }
        totaltweets += statuses.size();
        if(statuses.isEmpty()||totaltweets>250){
            LogPrinter.printLog("All tweets are retrieved....");
                available = false;
                continue;
                       
            }
        ListIterator li = statuses.listIterator();
        while(li.hasNext()){
            Status s = (Status)li.next();
            if(since_id < 0 && nooftweets==0){
                since_id = s.getId();
            }
            Tweet_dbo tweet = tweethelper.convertStatusToTweet_dbo(s);
            Tweet_dbo[] currentweets = TweetsTable.select(" tweet_id = "+tweet.values[Tweet_dbo.map.get("tweet_id")].lnumber, 0, 2);
            if(currentweets.length==0){
                TweetsTable.insert(tweet);
                useredgecollections.extract_InsertUsers_EdgesFromTweet(s);
                nooftweets++;
                LogPrinter.printLog("Inserting a new tweet.."+nooftweets);
            } else {
                 LogPrinter.printLog("This tweet already exists in the database...");
            }
        }
        if(!statuses.isEmpty()){
        max_id = ((Status)statuses.get(statuses.size()-1)).getId();
        p.setMaxId(max_id);
        }
        }
        LogPrinter.printLog(String.valueOf(max_id));
        LogPrinter.printLog(String.valueOf(since_id));
        if(since_id>0 && max_id >0){
            boolean selected[] = new boolean[User_dbo.nooffields];
            selected[User_dbo.map.get("max_id")] = true;
            selected[User_dbo.map.get("since_id")] = true;
            selected[User_dbo.map.get("utimeline_processed")] = true;
            user.values[User_dbo.map.get("max_id")].setValue(String.valueOf(max_id)); 
            user.values[User_dbo.map.get("since_id")].setValue(String.valueOf(since_id));
            user.values[User_dbo.map.get("utimeline_processed")].setValue("true");
            UsersTable.update(user, selected, " user_id =  "+user.values[User_dbo.map.get("user_id")].lnumber);
        }
        
    }
    
    void updateUserInUsersTable(User_dbo user){
        User_dbo[] currentuser = UsersTable.select(" user_id = "+user.values[User_dbo.map.get("user_id")].lnumber, 0, 2);
        if(currentuser.length>0){
            if(currentuser[0].values[User_dbo.map.get("udetails_processed")].bool == false){
                UsersTable.delete(currentuser[0].values[User_dbo.map.get("user_id")].lnumber);
                user.values[User_dbo.map.get("udetails_processed")].setValue("true");
                
                
                UsersTable.insert(user);
            }
        }
    }
    
    public void startProcessForTimeLineCollectionOfUsers() throws InterruptedException{
        User_dbo[] users;
        int count = 100;
        long min_id = 0;
        boolean available = true;
        while(available) {
        users = UsersTable.select(" utimeline_processed = false ", min_id, count);
        if(users.length == 0) {
            available = false;
            continue;
        }
        min_id = users[users.length-1].values[User_dbo.map.get("id")].lnumber;
        for(User_dbo user: users) {
            LogPrinter.printLog("Selected an User & retrieving tweets from this user's timeline "+user.values[User_dbo.map.get("screename")].string);
            collect_InsertTimeLineOfUser(user);
 
        }
        
        
        }
        
         LogPrinter.printLog("Successfull All users selected were processed.");
        
        
    }
}
