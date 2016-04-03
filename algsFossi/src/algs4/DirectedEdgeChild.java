/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algs4;

/**
 *
 * @author Lenovo
 */
public class DirectedEdgeChild extends DirectedEdge {

    private boolean color;

    public DirectedEdgeChild(int v, int w, double weight,boolean color) {
        super(v, w, weight);
        this.color=color;
    }

    public boolean isColor() {
        return color;
    }

    public void setColor(boolean color) {
        this.color = color;
    }

    @Override
    public String toString() {
        if (this.isColor() == false) {
            return this.v + "->" + this.w;
        } else {
            this.setColor(false);
            return this.v + "->" + this.w +" [color=blue]"+"\n"; 
        }
    }

}
