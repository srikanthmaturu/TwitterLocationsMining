/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twitterlocationsmining;

import DataCollections.FriendsnFollowersCollection;
import DataCollections.TrendsCollections;
import DataCollections.TweetCollections_HashTags;
import DataCollections.UserTimeLineCollection;

/**
 *
 * @author Srikanth
 */
public class TwitterDataCollection {

    
public static void main(String[] args) throws InterruptedException{
        
            UserTimeLineCollection timelinecoll = new UserTimeLineCollection();
        timelinecoll.startProcessForTimeLineCollectionOfUsers();
    
          
        
}

public void otherSteps() throws InterruptedException{
    TrendsCollections tcht = new TrendsCollections();
        tcht.collectCurrentTrendingLocations();
        
        TweetCollections_HashTags tweetcol_fhtags = new TweetCollections_HashTags();
        try {
        tweetcol_fhtags.updateTweetsCollectedfromHashTags();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    
        FriendsnFollowersCollection friendscoll = new FriendsnFollowersCollection();
        friendscoll.updateDatabasewithFriends_followers();
        UserTimeLineCollection timelinecoll = new UserTimeLineCollection();
        timelinecoll.startProcessForTimeLineCollectionOfUsers();
    
}
    
}
