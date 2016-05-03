/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twitterlocationsmining;

import Logger.LogPrinter;
import SyntheticData.Edge;
import SyntheticData.Graph;
import SyntheticData.GraphVisualizer;
import SyntheticData.GraphXVisualizer;
import SyntheticData.Node;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author Srikanth
 */
public class TwitterLocationsMiningUsingSyntheticData {
 
    
    public int nusers;
    public int nxpoints;
    public int nypoints;
    public int radius;
    public double density;
    public double auserarea;
    public double odlength;
    public Graph graph; 
    public double onediden;
    public int nusersod;
    public double avgdeviation;
    public int noofusershidden;
    public int noofestimatedlocs=0;
    public GraphVisualizer gvis;
    public double mindev;
    public double maxdev;
    public TwitterLocationsMiningUsingSyntheticData() {
        graph = new Graph();
    }
    
    public static void main(String[] args){
        double density = 0.001;
        int nxpoints = 1000;
        int nypoints = 1000;
        int radius = 25;
        double percent = 0.4;
        int seed = 5;
        TwitterLocationsMiningUsingSyntheticData sminer;
        for(int i=1;i<2; i++){
            LogPrinter.printLog("Iteration "+i);
            sminer = new TwitterLocationsMiningUsingSyntheticData();
            LogPrinter.printLog("Building Network");
            
            sminer.buildNetwork(nxpoints, nypoints, density*i, radius, seed);
            LogPrinter.printLog("Adding Edges");
            sminer.buildEdges(radius);
            LogPrinter.printLog("No of Edges: "+sminer.graph.edges.size());
            
            LogPrinter.printLog("Hiding Percent of Users Locations "+percent);
            sminer.hideSomePercent(percent, seed);
            sminer.estimateUnknownLocations();
            sminer.computeDeviation();
            
            LogPrinter.printLog("Density: "+density*i+" Radius: "+radius+" Deviation: "+sminer.avgDeviation()+ " No of Users Hidden: "+sminer.noofusershidden+" No of Users Estimated "
                    + sminer.noofestimatedlocs+" Percent Estimated: "+((double)sminer.noofestimatedlocs/(double)sminer.noofusershidden)+" No of Users Not Estimated: "+(sminer.noofusershidden
                            -sminer.noofestimatedlocs));
            sminer.gvis = new GraphVisualizer(sminer.graph, nxpoints, nypoints);
            sminer.gvis.run();
            sminer.gvis.saveImage();
        }
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
    
    public void buildNetwork(int nusers, int nxgpoints, int nygpoints, int radius , int seed){
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
        Random rand = new Random(seed);
        Node n;
        
        while(label<nusers) {
               
                int x=0;
                int y=0;
                boolean locass = false;
                while(!locass){
                    x = rand.nextInt(nxpoints);
                    y = rand.nextInt(nypoints);
                    int hashcode = (String.valueOf(x)+" "+String.valueOf(y)).hashCode();
                    if(!g.location_vertexmapping.containsKey(hashcode)){
                      locass = true;  
                      //LogPrinter.printLog(" Location assigned for Node "+label);
                      g.location_vertexmapping.put(hashcode, Integer.toString(label));
                      
                    }
                }
                n = new Node(Integer.toString(label));
                
                n.xcoor = x;
                n.ycoor = y;
               
                n.locationavailable = true;
                g.vertices.put(n.label, n);
                g.location_vertexmapping.put(n.hashCode(),n.label);
                label++;
               // LogPrinter.printLog("No of Vertices: "+g.vertices.size()+" Last Inserted Vertex: "+g.vertices.get(n.label).label+" Assigned Loc: x: "+g.vertices.get(n.label).xcoor
               // +" y: "+g.vertices.get(n.label).ycoor);
            }
        
        this.graph = g;
        
    }
    
    public void buildEdges(int radius) {
        int cx =0;
        int cy = 0;
        for(Node n: graph.vertices.values()) {
            
            cx = n.xcoor;
            cy = n.ycoor;
            
            ArrayList<Integer[]> points = getNearestPoints(cx, cy,radius);
           // LogPrinter.printLog(" Processing Node "+n.label+" x: "+cx+" y: "+cy+" No Of Nearest Points: "+points.size());
            for(Integer[] point: points){
                int hashcode = (String.valueOf(point[0])+" "+String.valueOf(point[1])).hashCode();
                if(graph.location_vertexmapping.containsKey(hashcode)){
                    double dis = distance(cx,point[0], cy, point[1]);
                    if(dis<=radius){
                    Node second = graph.vertices.get(graph.location_vertexmapping.get(hashcode));
                    Edge e = new Edge(n,second,assignWeight(dis));
                    graph.insertEdge(e);
                    n.addNeighbour(e);
                    second.addNeighbour(e);
                  //  LogPrinter.printLog("Processing Node: "+n.label+" Edge added: "+e.toString()+" Neighbour Location nx: "+e.getNeighbour(n).xcoor+" Neighbour Location ny: "+e.getNeighbour(n).ycoor);
                    }
                }
            }
        }
        
    }
    
    public ArrayList<Integer[]> getNearestPoints(int cx, int cy, int radius) {
        ArrayList<Integer[]> nearestpoints = new ArrayList<Integer[]>();
        int[] b = boundaries(cx,cy,radius);
        //LogPrinter.printLog("Printing boundaries: x1: "+b[0]+"y1: "+b[1]+" x2: "+b[2]+" y2: "+b[3]);
        for(int x = b[0]; x<=b[2]; x++) {
            for(int y = b[1]; y<=b[3]; y++) {
                if(cx!=x&&cy!=y){
                Integer[] coor = new Integer[2];
                coor[0] = x;
                coor[1] = y;
                nearestpoints.add(coor);
                }
            }
        }
        return nearestpoints;
    }
    
    public boolean outsideboundary(int x, int y){
        return (x>=0&&x<=nxpoints)&&(y>=0&&y<=nypoints);
    }
    
    public boolean outsideboundaryx(int x) {
     return (x>=0&&x<=nxpoints);
    }
    
    public boolean outsideboundaryy(int y) {
        return (y>=0&&y<=nypoints);
    }
    
    public int[] boundaries(int cx, int cy,int radius){
        int[] bs = new int[4];
        bs[0] = (cx-0)<radius?0:(cx-radius);
        bs[1] = (cy-0)<radius?0:(cy-radius);
        bs[2] = (nxpoints-cx)<radius?nxpoints:(cx+radius);
        bs[3] = (nypoints-cy)<radius?nypoints:(cy+radius);
        return bs;
    }
    
    public void buildNetwork(int nxgpoints,int nygpoints, double density, int radius,int seed){
        LogPrinter.printLog(" Area: "+(nxgpoints*nygpoints)+" Density: "+density);
        nusers = (int) Math.floor((nxgpoints*nygpoints)*density);
        LogPrinter.printLog(" No Of Users: "+nusers);
        buildNetwork(nusers, nxgpoints, nygpoints, radius,seed);
        
        
        
    }
    
    public void hideSomePercent(double percent,int seed){
        int totalnoofusers = graph.vertices.size();
        noofusershidden =  (int) Math.floor(percent*totalnoofusers);
        Random rand = new Random(seed);
        HashMap<Integer,ArrayList<Integer>> map = new HashMap<Integer,ArrayList<Integer>>();
        ArrayList<Integer> list;
        int index;
        int selected;
        int available = totalnoofusers;
              for(int i=noofusershidden; i>0; i--) {
                   selected = rand.nextInt(available);
                  if(map.containsKey(selected)){
                      list = map.get(selected);
                      index = list.get(list.size()-1);
                      if(selected == available-1){
                         list.add(available-1);
                      map.put(selected, list); 
                      } 
                      else 
                      {
                         if(map.containsKey(available-1)){
                            ArrayList<Integer> li = map.get(available-1);
                             list.add(li.get(li.size()-1));
                             map.put(selected, list); 
                              
                          }  else
                         {
                             list.add(available-1);
                      map.put(selected, list); 
                         }
                      }
                      
                  }
                  else{
                      index = selected;
                      if(selected!=available-1) {
                      list = new ArrayList<Integer>();
                      
                       if(map.containsKey(available-1)){
                            ArrayList<Integer> li = map.get(available-1);
                             list.add(li.get(li.size()-1));
                             map.put(selected, list); 
                              
                          }  else
                         {
                             list.add(available-1);
                      map.put(selected, list); 
                         }
                   
                      }
                      
                  }
               graph.vertices.get(Integer.toString(index)).hideloc = true;
               //LogPrinter.printLog(" Vertex "+index+" is hidden. Available: "+available);
               available--; 
              }  
              //LogPrinter.printLog(" Hidden Vertices: "+noofusershidden);
              
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
                else if(b.locationestimate) {
                xcoor+=b.xecoor;
                ycoor+=b.yecoor;
                count++;
                
                }
              
        }
        double[] loc = new double[2];
        if(count!=0){
        loc[0] = xcoor/count;
        loc[1] = ycoor/count;
        }
        else{
           return null;
        }
        return loc;
        
    }
    
    public void estimateUnknownLocations(){
        //LogPrinter.printLog(" No of Nodes: "+graph.vertices.size());
       
        for(Node n: graph.vertices.values()) {
            if(n.hideloc&&!n.locationestimate){
                
                double estloc[] = estimate(n);
                if(estloc!=null) {
                n.locationestimate = true;
                n.xecoor = estloc[0];
                n.yecoor = estloc[1];
                n.computeDeviation();
                //LogPrinter.printLog("Estimated a Location");
                noofestimatedlocs++;
                }
                else {
                    //LogPrinter.printLog("Can not estimate Location");
                }
            }
        }
       
    }
    
    public void computeDeviation() {
        double min = 1000;
        double max = -1;
        int count =0;
        double totaldev =0;
        for(Node n: graph.vertices.values()) {
            if(n.locationestimate){
                double dev = n.deviation;
             max = max<dev?dev:max;
             min = min>dev?dev:min;
             totaldev += n.deviation;
             count++;
            }
        }
        if(count!=0){
        avgdeviation = totaldev/count;
        maxdev = max;
        mindev = min;

        }
    }
    
    public double avgDeviation() {
        return avgdeviation;
    }
    
    public double minDeviation() {
        return mindev;
    }
    public double maxDeviation() {
        return maxdev;
    }
    
    
    
    
    
    
}
