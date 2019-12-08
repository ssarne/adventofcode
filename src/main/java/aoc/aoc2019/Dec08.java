package aoc.aoc2019;

import static aoc.Utils.check;
import static aoc.Utils.getLines;

import java.util.LinkedList;
import java.util.List;

public class Dec08 {

  public static void main(String[] args) throws Exception {
    test();
    task1();
    task2();
  }

  public static void test() throws Exception {
    int result = doit("123456789012", 2, 3);
    check(result, 1);
  }

  public static void task1() throws Exception {
    String line = getLines().get(0);
    int result = doit(line, 25, 6);
    System.out.println("Result 1: " + result);
  }

  public static int doit(String input, int dx, int dy) throws Exception {

    List<String> layers = getLayers(input, dx, dy);
    String layer = findLayer(layers, '0');
    return findChars(layer, '1') * findChars(layer, '2');
  }

  private static List<String> getLayers(String input, int dx, int dy) {
    int pixels = dx * dy;
    List<String> layers = new LinkedList<>();
    for (int i = 0; i < input.length(); i += pixels) {
      layers.add(input.substring(i, i + pixels));
    }
    return layers;
  }

  private static int findChars(String layer1, char c) {
    int n = 0;
    for (int i = 0; i < layer1.length(); i++) {
      if (layer1.charAt(i) == c) {
        n++;
      }
    }
    return n;
  }

  private static String findLayer(List<String> layers, char c) {
    String layer = layers.get(0);
    for (String l : layers) {
      layer = findChars(layer, c) < findChars(l, c) ? layer : l;
    }
    return layer;
  }

  public static void task2() throws Exception {

    String line = getLines().get(0);
    List<String> layers = getLayers(line, 25, 6);
    char[] image = buildImage(layers, 25, 6);

    System.out.println("Result 2: ");
    print(image, 25, 6);
  }

  private static char[] buildImage(List<String> layers, int dx, int dy) {
    char[] image = new char[dx * dy];
    for (int i = 0; i < image.length; i++) {
      image[i] = '2';
    }

    // 0 black, 1 white, 2 transparent
    for (String layer : layers) {
      for (int i = 0; i < image.length; i++) {
        if (image[i] == '2') {
          image[i] = layer.charAt(i);
        }
      }
    }
    return image;
  }

  public static void print(char[] image, int dx, int dy) {
    for (int i = 0; i < image.length; i++) {
      if (i > 0 && i % dx == 0) {
        System.out.println();
      }
      if (image[i] == '1') {
        System.out.print('#');
      } else {
        System.out.print(' ');
      }
    }
  }
}
