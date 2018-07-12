package Report;

import java.io.Serializable;

/**
 *
 * @author brunn
 */
public class SubReport implements Serializable {
    private String pos;
    private String realValue;
    private String bitmapValue;
    private String realNodePair;
    private String bitmapNodePair;

    public SubReport() {
    }

    public SubReport(String pos, String realValue, String realNodePair) {
        this.pos = pos;
        this.realValue = realValue;
        this.realNodePair = realNodePair;
    }

    public String getPos() {
        return pos;
    }

    public String getRealValue() {
        return realValue;
    }

    public String getBitmapValue() {
        return bitmapValue;
    }

    public void setBitmapValue(String bitmapValue) {
        this.bitmapValue = bitmapValue;
    }

    public String getRealNodePair() {
        return realNodePair;
    }

    public String getBitmapNodePair() {
        return bitmapNodePair;
    }

    public void setBitmapNodePair(String bitmapNodePair) {
        this.bitmapNodePair = bitmapNodePair;
    }
    
}
