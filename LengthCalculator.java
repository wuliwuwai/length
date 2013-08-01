import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * @author wanggang
 * @date： 2013-8-1 email: 315331371@qq.com
 */
public class LengthCalculator {
    /**
     * 
     * @param args
     */
    public static void main(final String[] args) {

        Scanner scanner;
        try {
            scanner = new Scanner(new File("input.txt"));
            PrintWriter out = new PrintWriter("output.txt");
            Converter canverter = new Converter("m");
            out.println("315331371@qq.com");
            out.println();

            while (scanner.hasNextLine()) {
                String curLine = scanner.nextLine();
                if (curLine.contains("=")) {
                    String str[] = curLine.split(" ");
                    try {
                        canverter.putConverterSet(str[1],
                                Double.parseDouble(str[3]));
                    } catch (Exception e) {
                        System.out.println("输入的格式出错:" + curLine);

                    }
                } else {
                    if (curLine != null && !"".equals(curLine.trim())) {
                        String str[] = curLine.split(" ");
                        Double result = 0d;
                        Double value = 0d;
                        String operator = "+";
                        String key;
                        for (int i = 0; i < str.length; i++) {
                            if (Pattern.matches("[0-9]+.*[0-9]*+", str[i])) {
                                value = Double.parseDouble(str[i]);
                            } else if (Pattern.matches("[+]+", str[i])) {
                                operator = "+";
                            } else if (Pattern.matches("[-]+", str[i])) {
                                operator = "-";
                            } else if (Pattern.matches("[a-zA-Z]+", str[i])) {
                                key = str[i];
                                if ("+".equals(operator)) {
                                    BigDecimal d1 = new BigDecimal(result);
                                    BigDecimal d2 = new BigDecimal(
                                            canverter.convert(key, value));
                                    result = d1.add(d2).doubleValue();

                                } else if ("-".equals(operator)) {
                                    BigDecimal d1 = new BigDecimal(result);
                                    BigDecimal d2 = new BigDecimal(
                                            canverter.convert(key, value));
                                    result = d1.subtract(d2).doubleValue();
                                }
                            }
                        }
                        out.println(new BigDecimal(result).setScale(2,
                                BigDecimal.ROUND_HALF_UP)
                                + " "
                                + canverter.getUnit());
                    }
                }
            }
            out.close();
        } catch (FileNotFoundException e) {
            System.out
                    .println("请把 'input.txt'文件放在和 'LengthCalculator.java'文件相同的目录 ");
            e.printStackTrace();
        }
    }

}

/**
 * 转化器类.
 * 
 * @author wanggang
 * @date： 2013-8-1 email: 315331371@qq.com
 */
class Converter {
    /**
     * 标准单位
     */
    private String              unit         = "m";

    private Map<String, Double> converterSet = new HashMap<String, Double>(0);

    public Converter() {
        super();

    }

    public Converter(String unit) {
        super();
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Map<String, Double> getConverterSet() {
        return converterSet;
    }

    public void setConverterSet(Map<String, Double> converterSet) {
        this.converterSet = converterSet;
    }

    public void putConverterSet(String key, Double value) {
        this.converterSet.put(key, value);
    }

    /**
     * 转换到标准单位
     * 
     * @param key
     * @param value
     * @return
     */
    public Double convert(String key, Double value) {
        return new BigDecimal(converterSet.get(getRealKey(key))).multiply(
                new BigDecimal(value)).doubleValue();
    }

    /**
     * 返回匹配度对高的键.
     * 
     * @param key
     * @return
     */
    private String getRealKey(String key) {
        Iterator<String> iterator = this.converterSet.keySet().iterator();
        if (key.equals("feet")) {
            return "foot";
        }
        while (iterator.hasNext()) {
            String realKey = iterator.next();
            if (key.startsWith(realKey)) {
                return realKey;
            }
        }
        return key;

    }

}
