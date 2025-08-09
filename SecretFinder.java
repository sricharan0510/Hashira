import java.io.FileReader;
import java.math.BigInteger;
import java.util.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class SecretFinder {

    public static void main(String[] args) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(new FileReader("c:/Users/srich/OneDrive/Desktop/roots.json"));

            JSONObject keys = (JSONObject) json.get("keys");
            int n = ((Long) keys.get("n")).intValue();
            int k = ((Long) keys.get("k")).intValue();

            List<BigInteger> xList = new ArrayList<>();
            List<BigInteger> yList = new ArrayList<>();

            for (int i = 1; i <= n; i++) {
                JSONObject point = (JSONObject) json.get(String.valueOf(i));
                int base = Integer.parseInt((String) point.get("base"));
                String value = (String) point.get("value");

                BigInteger y = new BigInteger(value, base);
                xList.add(BigInteger.valueOf(i));
                yList.add(y);
            }

            BigInteger secret = lagrangeInterpolation(BigInteger.ZERO, xList.subList(0, k), yList.subList(0, k));

            System.out.println("Secret (c) = " + secret);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static BigInteger lagrangeInterpolation(BigInteger x, List<BigInteger> xs, List<BigInteger> ys) {
        BigInteger result = BigInteger.ZERO;

        for (int i = 0; i < xs.size(); i++) {
            BigInteger term = ys.get(i);
            for (int j = 0; j < xs.size(); j++) {
                if (i != j) {
                    term = term.multiply(x.subtract(xs.get(j)))
                               .divide(xs.get(i).subtract(xs.get(j)));
                }
            }
            result = result.add(term);
        }
        return result;
    }
}
