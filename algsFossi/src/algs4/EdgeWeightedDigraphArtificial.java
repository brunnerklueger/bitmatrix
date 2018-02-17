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
 * @author Fernando Fossi Expected hash. user evaluated instead to
 * freemarker.template.SimpleScalar on line 13, column 6 in
 * Templates/Classes/Class.java.
 */
public class EdgeWeightedDigraphArtificial extends EdgeWeightedDigraph {

    /**
     * Initializes an edge-weighted digraph from an input stream. The format is:
     * A string COORD_X_Y followed the number of vertices <em>V</em>, for each
     * vertex the <em>X</em> and <em>Y</em> coordinates, A string
     * LINKS_SOURCE_DESTINATION_DISTANCE followed by followed by the number of
     * edges <em>E</em>, for each edge the pairs of vertices and edge weights,
     * with each entry separated by whitespace.
     *
     * @param in the input stream
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     */
    public EdgeWeightedDigraphArtificial(In in) throws IOException, InterruptedException {
        /* Reade the COORD_X_Y string */
        in.readString();
        /* Initialize the instance with the number of vertices */
        Initialize(in.readInt());
        /* Ingnores the coordinates */
        for (int i = 0; i < this.V; i++) {
            in.readDouble(); /* X coord */

            in.readDouble(); /* Y coord */

        }
        /* Read the LINKS_SOURCE_DESTINATION_DISTANCE string */
        in.readString();
        /* Read the number of Edges */
        int E = in.readInt();
        if (E < 0) {
            throw new IllegalArgumentException("Number of edges must be nonnegative");
        }

        /* Read each Edge */
        for (int i = 0; i < E; i++) {
            int v = in.readInt();
            int w = in.readInt();
            if (v < 0 || v >= V) {
                throw new IndexOutOfBoundsException("vertex " + v + " is not between 0 and " + (V - 1));
            }
            if (w < 0 || w >= V) {
                throw new IndexOutOfBoundsException("vertex " + w + " is not between 0 and " + (V - 1));
            }
            double weight = in.readInt();

            addEdge(new DirectedEdge(v, w, weight));
        }
    }

    public void Initialize(int V) {
        if (V < 0) {
            throw new IllegalArgumentException("Number of vertices in a Digraph must be nonnegative");
        }
        this.V = V;
        this.E = 0;
        adj = (Bag<DirectedEdge>[]) new Bag[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new Bag<DirectedEdge>();
        }
    }

}
