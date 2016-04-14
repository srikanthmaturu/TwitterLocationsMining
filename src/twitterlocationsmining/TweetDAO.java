package twitterlocationsmining;

import DataPreProcessingHelpers.Common;
import DataAccess.ConnectionFactory;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import twitter4j.GeoLocation;
import twitter4j.Status;

public class TweetDAO {
	
	private Connection conn = null;
	
	public TweetDAO()
	{
		conn = connect();
	}
	
	private Connection connect()
	{
		try {
			conn = ConnectionFactory.getConnection("root", "mysql", "localhost", "3306");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return conn;
	}
	
	public boolean addTweet(Status status, String searchTerm) throws SQLException
	{
		Double lat = 0.0;
		Double lon = 0.0;
		boolean addedNew = true;
		String hashtags = Common.hashtagsToString(status.getHashtagEntities());
		String mentions = Common.userMentionToString(status.getUserMentionEntities());
		
		java.sql.Timestamp created = new java.sql.Timestamp(status.getCreatedAt().getTime());
		
		if (status.getGeoLocation() != null)
		{
			GeoLocation gl = status.getGeoLocation();
			
			lat = gl.getLatitude();
			lon = gl.getLongitude();
		}
		
		PreparedStatement pstmt = conn.prepareStatement("INSERT INTO `tweethitter`.`tweets` VALUES " +
				"(NULL, ?, ?, NOW( ), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
		
                pstmt.setString(1, status.getText());
                
		pstmt.setTimestamp(2, created);
		pstmt.setString(3, searchTerm);
		pstmt.setInt(4, status.getFavoriteCount());
		pstmt.setInt(5, status.getRetweetCount());
		pstmt.setLong(6, status.getId());
		pstmt.setString(7, hashtags);
		pstmt.setString(8, mentions);
		pstmt.setLong(9, status.getInReplyToUserId());
		pstmt.setString(10, status.getInReplyToScreenName());
		pstmt.setLong(11, status.getInReplyToStatusId());
		pstmt.setBoolean(12, status.isRetweet());
		pstmt.setLong(13, status.getUser().getId());
		pstmt.setString(14, status.getUser().getScreenName());
		pstmt.setDouble(15, lat);
		pstmt.setDouble(16, lon);
		
		
		
		// Make sure that this isn't a duplicate
		if (numTweetsWithId(status.getId()) == 0)
		{
			//System.out.println("Found duplicate. Deleting old entry.");
			//deleteEntryWithId(status.getId());
			//addedNew = false;
                    pstmt.executeUpdate();
                    pstmt.close();
		} else {
                    addedNew = false;
                }

		return addedNew;		
	}
	
	private int numTweetsWithId(Long id) throws SQLException
	{
		Statement stmt = conn.createStatement();
                
		String query = "SELECT COUNT( * ) FROM `tweethitter`.`tweets` WHERE tweet_id = '" + id + "';";
		
		ResultSet rs = stmt.executeQuery(query);
		rs.first();
		return rs.getInt(1);
	}

	private int deleteEntryWithId(Long id) throws SQLException
	{
		Statement stmt = conn.createStatement();
		String query = "DELETE FROM `tweethitter`.`tweets` WHERE tweet_id = '" + id + "';";
		
		stmt.executeUpdate(query);
		
		return 0;
	}
	
	public void close() throws SQLException
	{
		conn.close();
	}
}
