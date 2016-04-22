/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataCollections;

import DataAccess.ConnectionFactory;
import datamanagement.LocationsTable;
import datamanagement.Place_dbo;
import datamanagement.PlacesTable;
import datamanagement.TweetsTable;
import twitter4j.GeoLocation;
import twitter4j.Place;
import org.json.simple.*;

/**
 *
 * @author Srikanth
 */
public class PlaceHelper {
    
    public PlaceHelper() {
        if(PlacesTable.connection==null){
        PlacesTable.connection = ConnectionFactory.defaultConnect();
        }
    }
    
     public Place_dbo[] selectPlaces(boolean[] selected, String whereclause, int min_id, int count) {
         return PlacesTable.select(selected, whereclause, min_id, count) ;
         
     }
     
     public Place_dbo convertPlacetoPlace_dbo(Place place){
         Place_dbo placedbo = new Place_dbo();
         placedbo.values[Place_dbo.map.get("place_id")].setValue(place.getId());
         placedbo.values[Place_dbo.map.get("name")].setValue(place.getName());
         placedbo.values[Place_dbo.map.get("full_name")].setValue(place.getFullName());
         placedbo.values[Place_dbo.map.get("country")].setValue(place.getCountry());
         placedbo.values[Place_dbo.map.get("country_code")].setValue(place.getCountryCode());
         placedbo.values[Place_dbo.map.get("place_type")].setValue(place.getPlaceType());
         placedbo.values[Place_dbo.map.get("url")].setValue(place.getURL());
         placedbo.values[Place_dbo.map.get("contained_within_place")].setValue(getAllContainedWithIngPlaces(place.getContainedWithIn()));
         placedbo.values[Place_dbo.map.get("boundingbox_coord")].setValue(stringifyBoundingBoxCoordinates(place.getBoundingBoxCoordinates()));
         double[] centroid = computeCentroid(place.getBoundingBoxCoordinates());
         placedbo.values[Place_dbo.map.get("centroid_lon")].setValue(String.valueOf(centroid[0]));
         placedbo.values[Place_dbo.map.get("centroid_lat")].setValue(String.valueOf(centroid[1]));
      
         return placedbo;
     }
     
     public String getAllContainedWithIngPlaces(Place[] places) {
         
         JSONArray jsonarray = new JSONArray();
         
         for(Place p: places) {
             JSONObject jobj = new JSONObject();
             jobj.put("id", p.getId());
             jobj.put("n", p.getName());
             jobj.put("pt", p.getPlaceType());
             jsonarray.add(jobj);
         }
         return jsonarray.toJSONString();
         
     }
     
     public String stringifyBoundingBoxCoordinates(GeoLocation[][] boundingboxcoor) {
         JSONArray jsonarray = new JSONArray();
         
         for(GeoLocation[] gls:boundingboxcoor){
             for(GeoLocation g: gls) {
                 JSONArray coor = new JSONArray();
                 coor.add(g.getLongitude());
                 coor.add(g.getLatitude());
                 jsonarray.add(coor);
             }
         }
         
         return jsonarray.toJSONString();
     }
     
     public double[] computeCentroid(GeoLocation[][] boundingboxcoor) {
         double[] centroid= new double[2];
         int count=0;
         for(GeoLocation[] gls:boundingboxcoor){
             for(GeoLocation g: gls) {
              centroid[0]+=g.getLongitude();
              centroid[1]+=g.getLatitude();
              count++;
             }
         }
         
         centroid[0]/=count;
         centroid[1]/=count;
         return centroid;
     }
     
     
     
     
     
     
}
