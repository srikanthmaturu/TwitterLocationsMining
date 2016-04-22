/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataCollections;

import DataAccess.ConnectionFactory;
import Logger.LogPrinter;
import datamanagement.Datatype;
import datamanagement.LocationsTable;
import java.sql.SQLException;
import java.util.Arrays;
import twitter4j.Location;

import datamanagement.Location_dbo;
/**
 *
 * @author Srikanth
 */
public class LocationHelper {
    
    
    public LocationHelper() {
        if(LocationsTable.connection==null){
        LocationsTable.connection = ConnectionFactory.defaultConnect();
        }
        
    }
    
    public void closeDBConnection() throws SQLException{
        LocationsTable.connection.close();
    }
    
    
    public void insertLocations(Location[] l) {
        boolean[] selection = new boolean[Location_dbo.nooffields];
        Location_dbo loc;
        
        
        //LogPrinter.printLog("No of Locations to be Processed: "+l.length);
        
        
        for (Location l1 : l) {
            if (LocationsTable.select("woeid = " + l1.getWoeid(), 0, 1).length == 0) {
                LogPrinter.printLog("Processing a location " + l1.getName());
                loc = convertLocationToLocation_dbo(l1);
                for(int j=0; j<Location_dbo.nooffields;j++){
                 selection[j] = loc.values[j].used;
                }
                LocationsTable.insert(loc, selection);
            }
        }
    }
    
    public Location_dbo[] selectLocations(boolean[] selected, String whereclause, int min_id, int count) {
        return LocationsTable.select(selected, whereclause, min_id, count);
    }
    
    public Location_dbo[] select(String whereclause, int min_id, int count) {
       return LocationsTable.select(whereclause, min_id, count);
    }
    
    public Location_dbo convertLocationToLocation_dbo(Location l) {
        
        Location_dbo loc = new Location_dbo();
        //LogPrinter.printLog("Conversion of current location to database object is started");
        loc.values[Location_dbo.map.get("country_code")].setValue(l.getCountryCode());
        loc.values[Location_dbo.map.get("country_name")].setValue(l.getCountryName());
        loc.values[Location_dbo.map.get("name")].setValue(l.getName());
        loc.values[Location_dbo.map.get("place_code")].setValue(String.valueOf(l.getPlaceCode()));
        loc.values[Location_dbo.map.get("place_name")].setValue(l.getPlaceName());
        loc.values[Location_dbo.map.get("url")].setValue(l.getURL());
        loc.values[Location_dbo.map.get("woeid")].setValue(String.valueOf(l.getWoeid()));
        //LogPrinter.printLog("Conversion of current location to database object complete");
        return loc;
    }
    
    
    
    
    
}
