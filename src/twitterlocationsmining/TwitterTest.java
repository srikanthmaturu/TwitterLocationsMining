package twitterlocationsmining;

import twitterlocationsmining.TweetDAO;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import twitter4j.Paging;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;

public class TwitterTest {
	
	public static void main(String[] args) throws SQLException, TwitterException
	{
		
			TweetDAO tdao = new TweetDAO();
			
			
			//ArrayList<String> searchVals = Common.loadSearchValues();
			
			
		    // The factory instance is re-useable and thread safe.
		    Twitter twitter = TwitterFactory.getSingleton();
		    //Query query = new Query("#Huskers");
                    Query query = new Query("#PacBradley");
                    boolean exit = true;
                    int i =0;
		   while(exit) {
                    query.setCount(100);
                    QueryResult result = twitter.search(query);
		    System.out.println("Count: " + result.getCount());
                    
		    for (Status status : result.getTweets()) {
		    	//tdao.addTweet(status, "#kimmel");
                       // System.out.println("index "+i+" @" + status.getUser().getScreenName() + ":" + status.getText()+"Inreply to status id: "+status.getInReplyToStatusId()                           );
                        System.out.println("index "+i+"Place: "+status.getPlace()+" GeoLocation: "+status.getGeoLocation());
                        i++;
                        
                        tdao.addTweet(status, "#LoveWhatYouHave");
		    	//System.out.println("Null: "+"text: "+status.getText()+
                          //      " TimeStamp: "+status.getCreatedAt().getTime()+" Now(): "+" SearchTerm: "+status.get+" ");
		      }
                    query.setMaxId(result.getTweets().get(result.getTweets().size()-1).getId());
		   
                }
                    tdao.close();
	}
}
