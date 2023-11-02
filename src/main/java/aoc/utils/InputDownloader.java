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

  public static boolean hasInputFile(String year, String day) throws Exception {
    return new File(getInputPath(year, day)).exists();
  }
  
  // https://adventofcode.com/2019/day/8/input
  public static void getInputFile(String year, String day) throws Exception {
    
    String d = day.toLowerCase()
        .replace("dec0", "")
        .replace("dec", "");

    String sid = getSessionCookie();

    HttpClient httpClient = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .build();


    URI uri = URI.create("https://adventofcode.com/" + year + "/day/" + d + "/input");
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

    FileWriter fw = new FileWriter(getInputPath(year, day));
    fw.write(response.body());
    fw.close();
  }

  static String getSessionCookie() throws IOException {
    return Files.lines(new File(".aocdlconfig").toPath())
        .map(s -> s.trim())
        .filter(s -> s.contains("session-cookie"))
        .map(s -> s.split(":")[1])
        .map(s -> s.trim())
        .map(s -> s.replaceAll("\"", ""))
        .findFirst()
        .get();
  }
}
