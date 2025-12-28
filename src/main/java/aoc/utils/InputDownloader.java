package aoc.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;

public class InputDownloader {

  static String getInputPath(String year, String day) {
    return "input/aoc" + year + "/" + day.toLowerCase() + ".txt";
  }

  static String getExamplePath(String year, String day, String index) {
    return "src/main/resources/aoc" + year + "/" + day.toLowerCase() + "_test" + index + ".txt";
  }

  public static boolean hasInputFile(String year, String day) throws Exception {
    return new File(getInputPath(year, day)).exists();
  }

  public static boolean hasExampleFile(String year, String day, String index) throws Exception {
    return new File(getExamplePath(year, day, index)).exists();
  }

  // https://adventofcode.com/2016/day/15
  public static void getExampleFiles(String year, String day) throws Exception {

    String d = day.toLowerCase()
        .replace("dec0", "")
        .replace("dec", "");

    URI uri = URI.create("https://adventofcode.com/" + year + "/day/" + d);
    String body = getAdventOfCodeURL(uri);

    int start = body.indexOf("<pre><code>");
    for (int i = 1 ; start > 0; i++) {
      int end = body.indexOf("</code></pre>", start);
      String example = body.substring(start + "<pre><code>".length(), end);
      FileWriter fw = new FileWriter(getExamplePath(year, day, "" + i));
      fw.write(example);
      fw.close();
      start = body.indexOf("<pre><code>", start + 1);
    }
  }

  // https://adventofcode.com/2019/day/8/input
  public static void getInputFile(String year, String day) throws Exception {

    String d = day.toLowerCase()
        .replace("dec0", "")
        .replace("dec", "");

    URI uri = URI.create("https://adventofcode.com/" + year + "/day/" + d + "/input");
    String body = getAdventOfCodeURL(uri);

    FileWriter fw = new FileWriter(getInputPath(year, day));
    fw.write(body);
    fw.close();
  }

  public static String getAdventOfCodeURL(URI uri) throws Exception {

    String sid = getSessionCookie();

    HttpClient httpClient = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .build();

    HttpRequest request = HttpRequest.newBuilder()
        .GET()
        .uri(uri)
        .setHeader("User-Agent", "github.com/ssarne/adventofcode")
        .setHeader("cookie", "session=" + sid)
        .build();

    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() != 200) {
      throw new IOException("Failed request for input file '" + uri + "'. status=" + response.statusCode() + " " + response.body());
    }

    return response.body();
  }

  static String getSessionCookie() throws IOException {
    return Files.lines(new File(".aocdlconfig").toPath())
        .map(s -> s.trim())
        .filter(s -> s.contains("session"))
        .map(s -> s.split("=")[1])
        .map(s -> s.trim())
        .map(s -> s.replaceAll("\"", ""))
        .findFirst()
        .get();
  }
}
