package aoc.aoc2018;

import static org.junit.Assert.*;

import java.util.List;
import org.junit.Assert;
import org.junit.Test;

import aoc.aoc2018.AdventOfCode20.Path;
import aoc.aoc2018.AdventOfCode20.Castle;
import aoc.aoc2018.AdventOfCode20.Pos;

public class AdventOfCode20Test {

  @Test
  public void testParsePathExpr() {

    Assert.assertEquals(
        List.of("WNE"), AdventOfCode20.flatten(AdventOfCode20.parsePathExpr("WNE", 0)));

    Assert.assertEquals(
        List.of("SSEEE", "SSEN"),
        AdventOfCode20.flatten(AdventOfCode20.parsePathExpr("SSE(EE|N)", 0)));

    Assert.assertEquals(
        List.of("ENWWWNEEE", "ENWWWSSEEE", "ENWWWSSEN"),
        AdventOfCode20.flatten(AdventOfCode20.parsePathExpr("ENWWW(NEEE|SSE(EE|N))", 0)));

    Assert.assertEquals(
        List.of("EESSWNSESSS", "EESSSSS"),
        AdventOfCode20.flatten(AdventOfCode20.parsePathExpr("EESS(WNSE|)SSS", 0)));

    Assert.assertEquals(
        List.of("AC", "AD", "BC", "BD"), //
        AdventOfCode20.flatten(AdventOfCode20.parsePathExpr("(A|B)(C|D)", 0)));
    Assert.assertEquals(
        List.of("1A2C3", "1A2D3", "1B2C3", "1B2D3"), //
        AdventOfCode20.flatten(AdventOfCode20.parsePathExpr("1(A|B)2(C|D)3", 0)));
    Assert.assertEquals(
        List.of("AC", "AD", "A", "BC", "BD", "B", "C", "D", ""), //
        AdventOfCode20.flatten(AdventOfCode20.parsePathExpr("(A|B|)(C|D|)", 0)));
    Assert.assertEquals(
        List.of("A", "B", "C", "D", "E"), //
        AdventOfCode20.flatten(AdventOfCode20.parsePathExpr("(A|B|(C|(D|E)))", 0)));

    Assert.assertEquals(
        List.of("DEF", "DF"), //
        AdventOfCode20.flatten(AdventOfCode20.parsePathExpr("D(E|)F", 0)));

    Assert.assertEquals(
        List.of("DESF", "DSF", "G", "H"), //
        AdventOfCode20.flatten(AdventOfCode20.parsePathExpr("(D(E|)SF|(G|H))", 0)));

    Assert.assertEquals(
        List.of("DEF", "DF", "G", "H"), //
        AdventOfCode20.flatten(AdventOfCode20.parsePathExpr("(D(E|)F|(G|H))", 0)));

    Assert.assertEquals(
        List.of("CDEF", "CDF", "CG", "CH"), //
        AdventOfCode20.flatten(AdventOfCode20.parsePathExpr("C(D(E|)F|(G|H))", 0)));

    Assert.assertEquals(
        List.of("AB", "ACDEF", "ACDF", "ACG", "ACH"), //
        AdventOfCode20.flatten(AdventOfCode20.parsePathExpr("A(B|C(D(E|)F|(G|H)))", 0)));

    Assert.assertEquals(
        List.of(
            "ESSWWNE",
            "ESSWWNNNENNEESSWNSESSS",
            "ESSWWNNNENNEESSSSS",
            "ESSWWNNNENNWWWSSSSESW",
            "ESSWWNNNENNWWWSSSSENNNE"),
        AdventOfCode20.flatten(
            AdventOfCode20.parsePathExpr("ESSWWN(E|NNENN(EESS(WNSE|)SSS|WWWSSSSE(SW|NNNE)))", 0)));
  }

  @Test
  public void testDoors() {

    String input = "^ENNWSWW(NEWS|)SSSEEN(WNSE|)EE(SWEN|)NNN$";
    Path paths = AdventOfCode20.parsePathExpr(input);
    Castle castle = AdventOfCode20.buildCastle(input, paths);
    castle.cement();
    castle.print();

    assertEquals(1, castle.doors(new Pos(0, 0, 0)));
    assertEquals(1, castle.doors(new Pos(-2, 0, 0)));
    assertEquals(2, castle.doors(new Pos(2, 0, 0)));
    assertEquals(3, castle.doors(new Pos(0, 2, 0)));
    assertEquals(1, castle.doors(new Pos(4, -4, 0)));
  }
}
