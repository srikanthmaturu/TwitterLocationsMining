/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataCollections;

import DataAccess.ConnectionFactory;
import Logger.LogPrinter;
import datamanagement.HashTagTable;
import datamanagement.Hashtag_dbo;
import datamanagement.Location_dbo;
import datamanagement.LocationsTable;
import java.sql.SQLException;
import java.util.Arrays;
import twitter4j.Location;
import twitter4j.Trend;

/**
 *
 * @author Srikanth
 */
public class HashTagHelper {
        
    
    public HashTagHelper() {
        HashTagTable.connection = ConnectionFactory.defaultConnect();
    }
    
    public void closeDBConnection() throws SQLException{
        HashTagTable.connection.close();
    }
    
    
    public void insertTrends(Trend[] trends) {
        boolean[] selection = new boolean[Hashtag_dbo.nooffields];
        Hashtag_dbo hashtag;
        
        for (Trend t : trends) {
            LogPrinter.printLog("Processing a trend"+t.getName());
            
            if (HashTagTable.select("hashtag = \"" + t.getName()+"\"", 0, 1).length == 0) {
                hashtag = convertTrendToHashTag_dbo(t);
                for(int j=0; j<Hashtag_dbo.nooffields;j++){
                 selection[j] = hashtag.values[j].used;
                }
                HashTagTable.insert(hashtag, selection);
                LogPrinter.printLog(selection.toString());
            }
        }
    }
    
   
    
    public Hashtag_dbo convertTrendToHashTag_dbo(Trend trend) {
        Hashtag_dbo hashtag = new Hashtag_dbo();
        hashtag.values[Hashtag_dbo.map.get("hashtag")].setValue(trend.getName());
        hashtag.values[Hashtag_dbo.map.get("ftrendsres")].setValue("true");
        return hashtag;
    }
    
    
}
