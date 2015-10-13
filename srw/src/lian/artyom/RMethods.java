package lian.artyom;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by artem on 27.09.15.
 */
public class RMethods
{
    public static final class Result
    {
        public int[] key, value;
    }

    ;

    public RMethods()
    {

    }


    public static void main(String[] args)
    {
        if (args.length == 5)
        {
            cropAndHandleImage(args[0], args[1], args[2], args[3], Integer.valueOf(args[4]));
        } else
        {
            System.out.println("Not enough arguments to launch.");
        }
    }

//    private static final Logger log = Logger.getLogger(RMethods.class);

    public static void testStatic(int[] valus)
    {
        System.out.println(new Random().nextGaussian());
    }

    public static String className(Object object)
    {
        System.out.println(object.getClass().getDeclaredMethods());
        for (Method m : object.getClass().getDeclaredMethods())
        {
            System.out.println(m.getName());
        }
        return object.getClass().getName();
    }

    /**
     * method merges two maps
     * written to support r
     */
    public static Result mergeMaps(int[] key1, int[] value1, int[] key2, int[] value2)
    {
//        for(int i = 0; i<key1.length; i++){
//            System.out.println(key1[i] + " " + value1[i] + " " + key2[i] + " " + value2[i]);
//        }


        Map<Integer, Integer> result = new HashMap<>(key1.length + 1);
        for (int i = 0; i < key1.length; i++)
        {
            if (result.containsKey(key1[i]))
            {// increment by key from key1
                result.put(key1[i], result.get(key1[i]) + value1[i]);
//                System.out.println("increment by key 1");
            } else
            {// add new key
//                System.out.println("insert key 1");
                result.put(key1[i], value1[i]);
            }
        }
        for (int i = 0; i < key2.length; i++)
        {
            if (result.containsKey(key2[i]))
            {// increment by key from key1
//                System.out.println("increment by key 2");
                result.put(key2[i], result.get(key2[i]) + value2[i]);
            } else
            {// add new key
//                System.out.println("insert key 2");
                result.put(key2[i], value2[i]);
            }
        }
        Result result1 = new Result();
        result1.key = new int[result.entrySet().size()];
        result1.value = new int[result.entrySet().size()];
//        System.out.println(result.entrySet().size());
        int i = 0;
        for (Map.Entry<Integer, Integer> e : result.entrySet())
        {
            result1.key[i] = e.getKey();
            result1.value[i++] = e.getValue();
        }


//        System.out.println("success");
//        DOMConfigurator.configure("/home/artem/IdeaProjects/Custom/config/log4j-config.xml");
//        log.debug("success");
//        log.trace("success");
//        result.putAll(m1);
//        result.putAll(m2);
//        return result;
        return result1;
    }

    public static String getName(int i, String dir, String name, String format, String delimiter)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(dir);
        sb.append("/");
        sb.append(name);
        sb.append(delimiter);
        sb.append(i);
        sb.append(".");
        sb.append(format);
        return sb.toString();
    }

    /**
     * method splits source image several rectangular parts
     *
     * @param img    source image root
     * @param dir    directory where output data will bewritten
     * @param name   name of output file chunk
     * @param format format of output chunk
     * @param length number of chunks to be craeted
     */
    public static void cropAndHandleImage(String img, String dir, String name, String format, int length)
    {
        try
        {
            int size = 0;
            BufferedImage image = ImageIO.read(new File(img));

            size = (int) image.getHeight() / length - 1;

            for (int i = 0; i < length; i++)
            {
//                ImageIO.write(image.getSubimage(0, i* size, image.getWidth(), size), "bmp",new File(getName(i)));
                BufferedImage img1 = image.getSubimage(0, i * size, image.getWidth(), size);
                long cnt = 0;
//                BufferedWriter writer = new BufferedWriter(new FileWriter(new File(getName(i + 1, "pic/output ", " .csv", "# "))));
                BufferedWriter writer = new BufferedWriter(new FileWriter(new File(getName(i + 1, dir, name, format, "# "))));
                writer.write("X" + (i + 1) + "\n");
                for (int k = 0; k < img1.getWidth(); k++)
                {
                    for (int l = 0; l < img1.getHeight(); l++)
                    {
                        StringBuilder sb = new StringBuilder();
                        sb.append("\"");
                        sb.append(cnt++);
                        sb.append("\"");
                        sb.append(" ");
                        sb.append(img1.getRGB(k, l));
                        sb.append("\n");
                        writer.write(sb.toString());
                    }
                }
                writer.flush();
                writer.close();
            }

//            image.getRGB(4,4);
//            System.out.println((image.getRGB(222,202)>>16)&255);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static Result mapBlock(int[] key)
    {
//        System.out.println("map start");
        Result result = new Result();
//        System.out.println("created output");
        HashMap<Integer, Integer> map = new HashMap<>();
//        System.out.println("created map");
        for (int i = 0; i < key.length; i++)
        {
            if (map.containsKey(key[i]))
            {
                map.put(key[i], map.get(key[i]) + 1);
            } else
            {
                map.put(key[i], 1);
            }
        }
//        System.out.println("mapping finished");
        int i = 0;
        result.key = new int[map.entrySet().size()];
        result.value = new int[map.entrySet().size()];
        for (Map.Entry<Integer, Integer> e : map.entrySet())
        {
            result.key[i] = e.getKey();
            result.value[i++] = e.getValue();
        }
//        System.out.println("rseult unloaded");
        return result;
    }

    public static void buildHist(int[] values, int[] dencities)
    {

    }

    public void test()
    {
        System.out.println("test");
    }

    public void testWithParam(String param)
    {
        System.out.println("param=" + param);
    }
}
