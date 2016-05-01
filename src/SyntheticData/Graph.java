/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SyntheticData;

import java.util.HashMap;

/**
 *
 * @author Srikanth
 */
public class Graph {
    
   public HashMap<String,Node> vertices = new HashMap<String,Node>(); 
   public HashMap<Integer,Edge> edges = new HashMap<Integer,Edge>();
   
   public boolean insertEdge(Edge e){
       edges.put(e.getHashCode(), e);
       return true;
   }
   
   public boolean addEdge(Node a, Node b, double weight){
      if(a.equals(b)){
         return false; 
      }
      Edge e = new Edge(a,b,weight); 
      
      if(edges.containsKey(e.hashCode())) {
         return false;
     
      }
      else if(a.isNeighbour(e)||b.isNeighbour(e)) {
          return false;
      }
      
           edges.put(e.hashCode(), e);
           a.neighbours.add(e);
           b.neighbours.add(e);
      return true;
      
   }
      
    public boolean containsEdge(Edge e){
        return edges.containsKey(e.hashCode());
    }
    
    
    
    
           
}
