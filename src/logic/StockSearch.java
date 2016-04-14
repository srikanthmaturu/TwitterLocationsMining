package logic;

import twitterlocationsmining.TweetDAO;
import DataPreProcessingHelpers.Common;
import java.util.ArrayList;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;

public class StockSearch {
	
	public static void main(String[] args)
	{
		try {
			int numTotalAdded = 0;
			int numTweets = 0;
			int numNew = 0;
			TweetDAO tdao = new TweetDAO();
			
			ArrayList<String> searchVals = Common.loadSearchValues();
			
			for(String searchTerm : searchVals)
			{
				System.out.print("Now mining for stock symbol '" + searchTerm + "'");
				
			    // The factory instance is re-useable and thread safe.
			    Twitter twitter = TwitterFactory.getSingleton();
			    Query query = new Query(searchTerm + " lang:eng");
			    query.setCount(100);
			    
			    QueryResult result = twitter.search(query);
			    
			    for (Status status : result.getTweets()) 
			    {
			    	numTweets++;
			    	System.out.print(".");
			    	
			    	if (tdao.addTweet(status, searchTerm) == true)
			    	{
			    		numNew++;
			    	}
			    }
			    
			    System.out.println("\nSaw " + numTweets + " tweets, " + 
			    					(numTweets - numNew) + " were duplicates. " + numNew + " new entries.");
			    numTweets = 0;
			    numTotalAdded += numNew;
			    numNew = 0;
			    
			    System.out.println("...");
			}
			System.out.println("Total Number of Tweets Added: " + numTotalAdded);
			tdao.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
