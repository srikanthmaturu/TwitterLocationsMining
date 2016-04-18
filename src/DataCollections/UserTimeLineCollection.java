/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataCollections;

import datamanagement.Tweet_dbo;
import datamanagement.TweetsTable;
import datamanagement.User_dbo;
import datamanagement.UsersTable;
import java.util.ListIterator;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
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
    
    public void collect_InsertTimeLineOfUser(User_dbo user) {
        
        Paging p = new Paging();
        int count = 20;
        p.setCount(count);
        long max_id,since_id;
        
        int nooftweets = 0;
        if(user.values[User_dbo.map.get("max_id")].used) {
            max_id = user.values[User_dbo.map.get("max_id")].lnumber;
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
        p.setMaxId(max_id);
        
        ResponseList<Status> statuses = null;
        boolean available = true;
        while(available) {
            
        try{
            statuses = timelinesres.getUserTimeline(user.values[User_dbo.map.get("user_id")].lnumber, p);
        }
        catch(Exception e){
            
        }
        if(statuses.size()==0){
                available = false;
                continue;
                       
            }
        ListIterator li = statuses.listIterator();
        while(li.hasNext()){
            Status s = (Status)li.next();
            if(since_id < 0){
                since_id = s.getId();
            }
            Tweet_dbo tweet = tweethelper.convertStatusToTweet_dbo(s);
            Tweet_dbo[] currentweets = TweetsTable.select(" tweet_id = "+tweet.values[Tweet_dbo.map.get("tweet_id")].lnumber, 0, 2);
            if(currentweets.length==0){
                TweetsTable.insert(tweet);
                useredgecollections.extract_InsertUsers_EdgesFromTweet(s);
                nooftweets++;
            }
        }
        if(statuses.size()>0){
        max_id = ((Status)statuses.get(statuses.size()-1)).getId();
        p.setMaxId(max_id);
        }
        }
        if(since_id>0 && max_id >0){
            boolean selected[] = new boolean[User_dbo.nooffields];
            selected[User_dbo.map.get("max_id")] = true;
            selected[User_dbo.map.get("since_id")] = true;
            user.values[User_dbo.map.get("max_id")].lnumber = max_id;
            user.values[User_dbo.map.get("since_id")].lnumber = since_id;
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
    
    public void startProcessForTimeLineCollectionOfUsers(){
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
        min_id = users[users.length-1].values[User_dbo.map.get("id")].lnumber;
        for(User_dbo user: users) {
            collect_InsertTimeLineOfUser(user);
 
        }
        
        }
        
        
    }
}
