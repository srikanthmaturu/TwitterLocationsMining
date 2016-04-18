/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataCollections;

import java.util.List;
import org.geonames.Toponym;
import org.geonames.ToponymSearchCriteria;
import org.geonames.ToponymSearchResult;
import org.geonames.WebService;

/**
 *
 * @author Srikanth
 */
public class GeoNamesHelper {
    
    public GeoNamesHelper() {
        
    }
    
    public double[] searchGeoLocCoor(String lockeyword) {
         WebService.setUserName("srikanthmaturu"); // add your username here
         double[] location = null;
        ToponymSearchCriteria searchCriteria = new ToponymSearchCriteria();
        searchCriteria.setQ(lockeyword);
        ToponymSearchResult searchResult;
        try {
        searchResult = WebService.search(searchCriteria);
        for (Toponym toponym : searchResult.getToponyms()) {
         System.out.println(toponym.getName()+" "+ toponym.getCountryName());
         }
        List<Toponym> list = searchResult.getToponyms();
        Toponym[] toponyms = (Toponym[])list.toArray();
        location = new double[2];
        location[0] = toponyms[0].getLongitude();
        location[1] = toponyms[1].getLongitude();
        
        }
        catch(Exception e) {
            
        }
        return location;

    }
}
