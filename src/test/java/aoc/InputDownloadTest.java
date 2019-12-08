package aoc;

import aoc.InputDownloader;
import java.io.File;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;


public class InputDownloadTest {

  @Test
  public void hasInputFileTest() throws Exception {
    Assert.assertTrue(InputDownloader.hasInputFile("2019", "marker"));
    Assert.assertFalse(InputDownloader.hasInputFile("2019", "barker"));
  }

  @Ignore
  @Test
  public void getSessionCookie() throws Exception {
    File f = new File(".aocdlconfig");
    Assert.assertTrue("Cannot find file " + f.getAbsolutePath(), f.exists());
    Assert.assertTrue("Cannot find session cookie in file " + f.getAbsolutePath(),
        InputDownloader.getSessionCookie().length() > 2);
  }

  @Ignore
  @Test
  public void downloadInputFileTest() throws Exception {
    InputDownloader.getInputFile("2018", "Dec01");
  }
}
