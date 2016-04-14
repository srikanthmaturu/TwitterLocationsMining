package DataPreProcessingHelpers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import twitter4j.HashtagEntity;
import twitter4j.UserMentionEntity;

public class Common {

	public static String hashtagsToString(HashtagEntity[] ht)
	{
		StringBuilder sb = new StringBuilder();
		int i = 0;
		
		for (HashtagEntity he : ht)
		{
			if (i != 0)
			{
				sb.append(",");
			}
				
			sb.append(he.getText());
			
			i++;
		}
		return sb.toString();
	}
	
	public static String userMentionToString(UserMentionEntity[] ue)
	{
		StringBuilder sb = new StringBuilder();
		int i = 0;
		
		for (UserMentionEntity um : ue)
		{
			if (i != 0)
			{
				sb.append(",");
			}
				
			sb.append(um.getId());
			sb.append(":");
			sb.append(um.getName());
			sb.append(":");
			sb.append(um.getScreenName());
			
			i++;
		}
		return sb.toString();
	}
	
	public static ArrayList<String> loadSearchValues()
	{
		ArrayList<String> searchVals = new ArrayList<String>();
		BufferedReader br;
		try {
			
			br = new BufferedReader(new FileReader("D:\\Masters Program\\Assignments\\CSCE 874\\Final Project\\20companies.txt"));
			String line = null;  
			String[] tokens = null;
			while ((line = br.readLine()) != null)  
			{  
				tokens = line.split("\\|");
				
				if(!tokens[0].equals("Symbol"))
				{
					searchVals.add(tokens[0]);
				}
			}
			br.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
		return searchVals;

	}
}
