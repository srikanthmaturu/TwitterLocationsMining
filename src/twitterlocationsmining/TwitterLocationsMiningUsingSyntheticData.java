/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twitterlocationsmining;

import SyntheticData.Edge;
import SyntheticData.Graph;
import SyntheticData.Node;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Srikanth
 */
public class TwitterLocationsMiningUsingSyntheticData {
 
    
    public int nusers;
    public int nxpoints;
    public int nypoints;
    public double radius;
    public double density;
    public double auserarea;
    public double odlength;
    public Graph graph; 
    public double onediden;
    public int nusersod;
    public TwitterLocationsMiningUsingSyntheticData() {
        graph = new Graph();
    }
    
    public static void main(String[] args){
        
    }
    
    public double distance(double x1, double x2, double y1, double y2) {
        return Math.sqrt((Math.pow((x1-x2), 2)+ Math.pow((y1-y2), 2)));
    }
    
    public boolean checkWithInRadius(double x1, double x2, double y1, double y2) {
        if(distance(x1,x2,y1,y2)<=radius){
            return true;
        }
            return false;
    }
    
    public int assignWeight(double distance, String function){
        int weight = 0;
        switch(function) {
            case "log":
                
                break;
            case "gaussian":
                
                break;
                
        }
        return weight;
        
    }
    
    public double assignWeight(double distance){
        return 1/distance;
    }
    
    public void buildNetwork(int nusers, int nxgpoints, int nygpoints, double radius ){
        nxpoints = nxgpoints;
        nypoints = nygpoints;
        this.nusers = nusers;
        int totalarea = nxgpoints*nygpoints;
        density = (double)nusers/(double)totalarea;
        auserarea = (double)totalarea/(double)nusers;
        odlength = (double)Math.sqrt(nusers)/(double)nxgpoints;
        nusersod = (int)Math.floor(Math.sqrt(nusers))+1;
        onediden = Math.sqrt(auserarea);
        Graph g = new Graph();
        int label = 0;
        Node n;
        for(int i=0; i < nusersod; i++){
            for(int j=0; j < nusersod; j++){
                n = new Node(Integer.toString(label));
                
                n.xcoor = i*onediden;
                n.ycoor = j*onediden;
                n.locationavailable = true;
                g.vertices.put(n.label, n);
                
                
            }
        }
    }
    
    public void buildNetwork(int nxgpoints,int nygpoints, double density, double radius){
        
        nusers = (int) Math.floor(nxgpoints*nygpoints*density);
        buildNetwork(nusers, nxgpoints, nygpoints, radius);
        
        
        
    }
    
    
    
    public double[] estimate(Node a) {
        ArrayList<Edge> neighbours = a.neighbours;
        int count=0;
        double xcoor=0;
        double ycoor=0;
        Node b;
        for(Edge e: neighbours){
            b = e.getNeighbour(a);
            if(!b.hideloc) {
                xcoor+=b.xcoor;
                ycoor+=b.ycoor;
                count++;
            }
        }
        double[] loc = new double[2];
        loc[0] = xcoor/count;
        loc[1] = ycoor/count;
        return loc;
        
    }
    
    public void estimateUnknownLocations(){
        ArrayList<Node> nodes = (ArrayList<Node>) graph.vertices.values();
        for(Node n: nodes) {
            if(n.hideloc){
                n.locationestimate = true;
                double estloc[] = estimate(n);
                n.xecoor = estloc[0];
                n.yecoor = estloc[1];
            }
        }
       
    }
    
    
    
    
    
}
