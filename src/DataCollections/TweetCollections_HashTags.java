/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataCollections;

import DataAccess.TwitterResources;
import Logger.LogPrinter;
import datamanagement.HashTagTable;
import datamanagement.Hashtag_dbo;
import datamanagement.Tweet_dbo;
import datamanagement.TweetsTable;
import java.util.ListIterator;
import twitter4j.Location;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.api.SearchResource;
import twitter4j.api.TrendsResources;

/**
 *
 * @author Srikanth
 */
public class TweetCollections_HashTags {
    
    Twitter twitter;
    SearchResource searchres;
    TweetHelper thelper;
    HashTagHelper hashtaghelper;
    Users_EdgesCollectionFromTweets users_edgescollections;
    UserHelper userhelper;
    
    public TweetCollections_HashTags() {
        twitter = TwitterResources.getTwitterSingleton();
        searchres = twitter.search();
        thelper = new TweetHelper();
        users_edgescollections = new Users_EdgesCollectionFromTweets();
        hashtaghelper = new HashTagHelper();
        userhelper = new UserHelper();
        
    }
    
    
    public void updateTweetsCollectedfromHashTags() throws InterruptedException{
        //LogPrinter.printLog("Collecting tweets from unprocessed hashtags");
        Hashtag_dbo[] hashtags;
        long min_id = 0;
        int count = 500;
        boolean available = true;
        while(available){
        hashtags = HashTagTable.select( " processed = false ", min_id, count);
        if(hashtags.length==0){
            available = false;
            continue;
        }
        for(int index=0; index<hashtags.length;index++){
            
            collectTweetsFromHashtag_popterm(hashtags[index]);
        }
        min_id = hashtags[hashtags.length-1].values[Hashtag_dbo.map.get("id")].lnumber;
        }
        
    }
    
    public void collectTweetsFromHashtag_popterm(Hashtag_dbo hashtag) throws InterruptedException{
        
        long max_id = 0;
        boolean midused = false, sidused = false;
        if(hashtag.values[Hashtag_dbo.map.get("max_id")].used){
        max_id = hashtag.values[Hashtag_dbo.map.get("")].lnumber;
        midused = true;
        }
        long since_id  = 0;
        if(hashtag.values[Hashtag_dbo.map.get("since_id")].used){
        since_id = hashtag.values[Hashtag_dbo.map.get("since_id")].lnumber;
        sidused = true;
        }
        Query q =  new Query(hashtag.values[Hashtag_dbo.map.get("hashtag_popterm")].string);
        //LogPrinter.printLog("Collection Tweets for hashtag_searchterm"+hashtag.values[Hashtag_dbo.map.get("hashtag_popterm")].string);
        q.setCount(100);
        if(midused){
            q.setMaxId(max_id);
        }
        if(sidused) {
            q.setSinceId(since_id);
        }
        
        QueryResult result = null;
        try {
        result = searchres.search(q);}
        catch(Exception e){   
            LogPrinter.printLog("Tweet Search Resources Rate Limit reached ");
            if(e instanceof TwitterException) {
            
            }
            if(e instanceof InterruptedException) {
                //Thread.sleep(((TwitterException)e).getRetryAfter()*1000+5000);
            }
        }
        int count = 0 ;
        for(Status s: result.getTweets()) {
            Tweet_dbo tweet = thelper.convertStatusToTweet_dbo(s);
            String whereclause = "tweet_id = "+Long.toString(tweet.values[Tweet_dbo.map.get("tweet_id")].lnumber);
            tweet.values[Tweet_dbo.map.get("processed")].setValue("true");
            tweet.values[Tweet_dbo.map.get("f_search")].setValue("true");
            tweet.values[Tweet_dbo.map.get("searchterm")].setValue(hashtag.values[Hashtag_dbo.map.get("hashtag_popterm")].string);
            if(TweetsTable.select(whereclause, 0, 2).length==0){
                //LogPrinter.printLog(" Inserting tweet "+count+tweet.values[Tweet_dbo.map.get("tweet_id")].lnumber);
            TweetsTable.insert(tweet);
            users_edgescollections.extract_InsertUsers_EdgesFromTweet(s);
            count++;
            }
        }
    }
    
}
