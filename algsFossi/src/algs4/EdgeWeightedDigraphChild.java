/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package algs4;

import java.io.IOException;
import java.io.PrintWriter;
import stdlib.In;

/**
 *
 * @author Lenovo
 */
public class EdgeWeightedDigraphChild extends EdgeWeightedDigraph{
    
    public EdgeWeightedDigraphChild(int V) {
        super(V);
    }
    
    /**  
     * Initializes an edge-weighted digraph from an input stream.
     * The format is the number of vertices <em>V</em>,
     * followed by the number of edges <em>E</em>,
     * followed by <em>E</em> pairs of vertices and edge weights,
     * with each entry separated by whitespace.
     * @param in the input stream
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     */
    public EdgeWeightedDigraphChild(In in) throws IOException, InterruptedException {
        this(in.readInt());
        int E = in.readInt();
        if (E < 0) throw new IllegalArgumentException("Number of edges must be nonnegative");
        PrintWriter writer = new PrintWriter("grafo.dot", "UTF-8");
        writer.println("digraph 1 {");
        writer.println("graph[fontname=\"CourierNew\";rankdir=\"LR\";pad=\"0.25\"]\n" +
        "node[fontname=\"CourierNew\" target=\"_parent\"]\n" +
        "edge[fontname=\"CourierNew\"]\n"+
        "concentrate=true");
        for (int i = 0; i < E; i++) {
            int v = in.readInt() - 1;
            int w = in.readInt() - 1;
            if (v < 0 || v >= V) throw new IndexOutOfBoundsException("vertex " + v + " is not between 0 and " + (V-1));
            if (w < 0 || w >= V) throw new IndexOutOfBoundsException("vertex " + w + " is not between 0 and " + (V-1));
            double weight = in.readDouble();
            writer.println(v+"->"+w+"\n");
            
            addEdge(new DirectedEdgeChild(v, w, weight,false));
        }
        writer.println("}");
        writer.close();
    }

    

    /**
     * Returns a string representation of the edge-weighted digraph.
     * This method takes time proportional to <em>E</em> + <em>V</em>.
     * @return the number of vertices <em>V</em>, followed by the number of edges <em>E</em>,
     *   followed by the <em>V</em> adjacency lists of edges
     */
    @Override
    public String toString() {
        String NEWLINE = System.getProperty("line.separator");
        StringBuilder s = new StringBuilder();
       //s.append(V + " " + E + NEWLINE);
        for (int v = 0; v < V; v++) {
            //s.append(v + ": ");
            for (DirectedEdge e : adj[v]) {
                DirectedEdgeChild a = (DirectedEdgeChild) e;
                s.append(e);
                if (a.isColor() ==true){
                    s.append("[color=blue]");
                    a.setColor(false);
                }
                s.append("\n");

            }
            //s.append(NEWLINE);
        }
        return s.toString();
    }

}
