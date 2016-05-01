/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SyntheticData;

/**
 *
 * @author Srikanth
 */
public class Edge {
       public Node one;
       public Node two;
       public double weight;
    
    public double getWeight(){
        return weight;
    }
    
    public Node getOne() {
        return one;
    }
    
    public Node getTwo() {
        return two;
    }
    
    public void setWeght(double weight) {
        this.weight = weight;
    }
    
    public Edge(Node first, Node second, double weight){
        one = (first.label.compareTo(second.label)<=0)?first: second;
        two = (one==first) ? second: first;
        this.weight = weight;
    }
    
    public Node getNeighbour(Node a){
        if(!(one.equals(a)||two.equals(a))) {
            return null;
        }
        return (one.equals(a))?one:two;
    }
    
    public int getHashCode( ){
        
        return (one.label+two.label).hashCode();
    }
    
    public boolean containsNode(Node n){
      if(n.label==one.label||n.label==two.label){
          return true;
      }   else
      {
          return false;
      }
    }
    
    public boolean equals(Object e) {
        if(!(e instanceof Edge)) {
            return false;
        }
        
        Edge edge = (Edge)e;
        return (edge.one.equals(edge.one)&&edge.two.equals(edge.two));
    }
    
    
    
}
