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

  static String getPathname(String year, String day) {
    return "src/main/resources/aoc" + year + "/" + day.toLowerCase() + ".txt";
  }

  static String getPathname(String filename) {
    return filename.startsWith("src/main/resources")
           ? filename
           : "src/main/resources/" + filename;
  }

  static boolean hasInputFile(String year, String day) throws Exception {
    return new File(getPathname(year, day)).exists();
  }
  
  // https://adventofcode.com/2019/day/8/input
  static void getInputFile(String year, String day) throws Exception {
    
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
        .setHeader("User-Agent", "Java AoC lib")
        .setHeader("cookie", "session=" + sid)
        .build();

    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

    // print status code
    // System.out.println(response.statusCode());

    FileWriter fw = new FileWriter(getPathname(year, day));
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
