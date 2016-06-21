package Network;

/**
 * Created by rodolfovillaca on 22/09/14.
 */
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Hash {

    public static final int MOD = 2147483647;
    public static final int MOD32 = 0xFFFFFFFF;
    public static final int HL = 31;

    private long[] a, b;
    Random r = new Random();

    public int initializeVectorHash(int size) {

        a = new long[size];
        b = new long[size];

        for (int i = 0; i < size; i++) {
            a[i] = r.nextLong();
            //System.out.println(a[i]);
            b[i] = r.nextLong();
            //System.out.println(b[i]);
        }

        return 0;
    }

    public int hash(long a, long b, long x) {

        long result;

        result = (a * x) + b;
        result = ((result >> HL) + result) & MOD;

        return (int) result;
    }

    public int vectorHashing(DataPacket pkt) {

        int result = 0;

        //i starts in 1 to skip timestamp in hashing
        //for (int i = 1; i < Instance.getNumberOfPacketFields() && i < a.length; i++) {
        //numero de pacotes fixado em 6
        for (int i = 1; i < 6 && i < a.length; i++) {
            result = result + hash(a[i], b[i], pkt.getByNumber(i));
        }
        result = result & MOD;

        //System.out.println(result);
        return (int) result;
    }

    //Função para criar hash da senha informada
    public Integer md5(DataPacket pkt) {
        String senha = "";
        senha += lpad(Long.toBinaryString(pkt.getByNumber(1)),32);
        senha += lpad(Long.toBinaryString(pkt.getByNumber(2)),32);
        senha += lpad(Long.toBinaryString(pkt.getByNumber(3)),16);
        senha += lpad(Long.toBinaryString(pkt.getByNumber(4)),16);
        senha += lpad(Long.toBinaryString(pkt.getByNumber(5)),8);
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        BigInteger max = new BigInteger(Integer.toBinaryString(Integer.MAX_VALUE), 2);
        BigInteger hash = new BigInteger(1, md.digest(senha.getBytes()));
        return hash.divideAndRemainder(max)[1].intValue();
    }
    
    private String lpad(String str, int size){
        String res = str;
        while(res.length() < size){
            res = "0" + res;
        }
        return res;
    }

}
