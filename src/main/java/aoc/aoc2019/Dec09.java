package aoc.aoc2019;

import static aoc.utils.Utils.check;
import static aoc.utils.Utils.getLines;
import static aoc.utils.Utils.readAnswer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public class Dec09 {

  public static void main(String[] args) throws Exception {
    test();
    task1();
    task2();
  }

  public static void test() throws Exception {
    testit("109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99", "109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99");
    testit("1102,34915192,34915192,7,4,7,99,0", "1219070632396864");
    testit("104,1125899906842624,99", "1125899906842624");
  }

  public static void testit(String line, String expected) throws Exception {
    Program p = Program.create(line);
    IntCode.execute(p);
    String result = p.getOutputAsString();
    check(result, expected);
  }

  public static void task1() throws Exception {

    Program p = Program.create(getLines().get(0));
    p.input.add(1L);
    IntCode.execute(p);
    String result = p.getOutputAsString();
    System.out.println("Result: " + result);
    check(result, readAnswer(1));
  }

  public static void task2() throws Exception {

    Program p = Program.create(getLines().get(0));
    p.input.add(2L);
    IntCode.execute(p);
    String result = p.getOutputAsString();
    System.out.println("Result: " + result);
    check(result, readAnswer(2));
  }

  private static class Program {

    enum Status {
      HALT_OK,
      BLOCKED,
      ERROR,
      NA
    }

    Queue<Long> input;
    Queue<Long> output;
    Program.Status status;
    long pc;
    Memory mem;
    long offset = 0;

    Program(long[] memory) {
      this.pc = 0;
      this.mem = new Memory(memory);
      this.status = Program.Status.NA;
      this.input = new ConcurrentLinkedQueue();
      this.output = new ConcurrentLinkedQueue();
    }

    void print() {
      System.out.println("======================= pc=" + pc + "   input=" + input.toString());
      System.out.println("======================= memory=" + mem.toString());
    }

    public String getOutputAsString() {
      return output.stream().map(n -> String.valueOf(n)).collect(Collectors.joining(","));
    }

    static Program create(long[] memory) {
      return new Program(memory);
    }

    static Program create(long[] memory, long[] input) {
      Program p = new Program(memory);
      for (long i : input) {
        p.input.add(i);
      }
      return p;
    }

    static Program create(String program) {
      long[] memory = Arrays.stream(program.split(",")).mapToLong(Long::parseLong).toArray();
      return create(memory);
    }
  }

  private static class Memory {

    int SIZE = 1024;
    HashMap<Long, long[]> chunks = new HashMap<>();

    public Memory(long [] memory) {
      for (int i = 0; i < memory.length; i++) {
        store(i, memory[i]);
      }
    }

    long read(long addr) {
      long cid = addr / SIZE;
      int pos = (int) (addr % SIZE);
      if (pos < 0)
        throw new RuntimeException("CMH");
      return chunks.get(cid)[pos];
    }

    public void store(long addr, long value) {
      long cid = addr / SIZE;
      int pos = (int) (addr % SIZE);
      long [] chunk = chunks.get(cid);
      if (chunk == null) {
        chunk = new long[SIZE];
        chunks.put(cid, chunk);
      }
      chunk[pos] = value;
    }
  }

  private static class IntCode {

    static Program execute(Program p) {

      // p.print();

      while (true) {

        Memory mem = p.mem;
        final long pc = p.pc;
        final long offset = p.offset;

        int op = (int) read(pc, mem);
        long p1 = 0, p2 = 0, p3 = 0, v1 = 0, v2 = 0, v3 = 0;
        int m1 = 0, m2 = 0, m3 = 0;

        if (op >= 100) {
          int modes = op / 100;
          m1 = (modes / 1) % 10;
          m2 = (modes / 10) % 10;
          m3 = (modes / 100) % 10;
          op = op % 100;
        }

        // printOp(pc, mem);

        switch (op) {
          // Opcode 1 adds together numbers read from two positions and stores the result in a third position.
          case 1: // ADD
            p1 = read(pc + 1, mem);
            p2 = read(pc + 2, mem);
            p3 = read(pc + 3, mem);
            v1 = resolve(p1, m1, mem, offset);
            v2 = resolve(p2, m2, mem, offset);
            v3 = resolve(p3, m3, mem, offset, true);
            if (m3 == 1) throw new RuntimeException(("CMH"));
            store(v3, v1 + v2, mem);
            p.pc += 4;
            break;

          // Opcode 2 multiplies the two inputs and stores the result in a third position.
          case 2: // MUL
            p1 = read(pc + 1, mem);
            p2 = read(pc + 2, mem);
            p3 = read(pc + 3, mem);
            v1 = resolve(p1, m1, mem, offset);
            v2 = resolve(p2, m2, mem, offset);
            v3 = resolve(p3, m3, mem, offset, true);
            if (m3 == 1) throw new RuntimeException(("CMH"));
            store(v3, v1 * v2, mem);
            p.pc += 4;
            break;

          // Opcode 3 takes a single integer as input and saves it to the position given by its only parameter
          case 3: // INPUT
            if (p.input.isEmpty()) {
              p.status = Program.Status.BLOCKED;
              return p;
            }
            p1 = read(pc + 1, mem);
            v1 = resolve(p1, m1, mem, offset, true);
            v2 = p.input.poll();
            store(v1, v2, mem);
            p.pc += 2;
            break;

          // Opcode 4 outputs the value of its only parameter.
          case 4: // OUTPUT
            p1 = read(pc + 1, mem);
            v1 = resolve(p1, m1, mem, offset);
            p.output.add(v1);
            p.pc += 2;
            break;

          // Opcode 5 is jump-if-true: if the first parameter is non-zero, it sets the instruction
          // pointer to the value from the second parameter. Otherwise, it does nothing.
          case 5:
            p1 = read(pc + 1, mem);
            p2 = read(pc + 2, mem);
            v1 = resolve(p1, m1, mem, offset);
            v2 = resolve(p2, m2, mem, offset);
            p.pc = (v1 != 0 ? v2 : pc + 3);
            break;

          // Opcode 6 is jump-if-false: if the first parameter is zero, it sets the instruction
          // pointer to the value from the second parameter. Otherwise, it does nothing.
          case 6:
            p1 = read(pc + 1, mem);
            p2 = read(pc + 2, mem);
            v1 = resolve(p1, m1, mem, offset);
            v2 = resolve(p2, m2, mem, offset);
            p.pc = (v1 == 0 ? v2 : pc + 3);
            break;

          // Opcode 7 is less than: if the first parameter is less than the second parameter, it
          // stores 1 in the position given by the third parameter. Otherwise, it stores 0.
          case 7:
            p1 = read(pc + 1, mem);
            p2 = read(pc + 2, mem);
            p3 = read(pc + 3, mem);
            v1 = resolve(p1, m1, mem, offset);
            v2 = resolve(p2, m2, mem, offset);
            v3 = resolve(p3, m3, mem, offset, true);
            if (m3 == 1) throw new RuntimeException(("CMH"));
            store(v3, (v1 < v2 ? 1 : 0), mem);
            p.pc += 4;
            break;

          // Opcode 8 is equals: if the first parameter is equal to the second parameter, it
          // stores 1 in the position given by the third parameter. Otherwise, it stores 0.
          case 8:
            p1 = read(pc + 1, mem);
            p2 = read(pc + 2, mem);
            p3 = read(pc + 3, mem);
            v1 = resolve(p1, m1, mem, offset);
            v2 = resolve(p2, m2, mem, offset);
            v3 = resolve(p3, m3, mem, offset, true);
            if (m3 == 1) throw new RuntimeException(("CMH"));
            store(v3, (v1 == v2 ? 1 : 0), mem);
            p.pc += 4;
            break;

            // Opcode 9 adjusts the relative base by the value of its only parameter.
          case 9:
            p1 = read(pc + 1, mem);
            v1 = resolve(p1, m1, mem, offset);
            p.offset += v1;
            p.pc += 2;
            break;

          case 99:
            p.status = Program.Status.HALT_OK;
            return p;

          default:
            System.out.print("Error, pc=" + pc + " op=" + op);
            p.status = Program.Status.ERROR;
            return p;
        }
      }
    }

    private static void store(long addr, long value, Memory mem) {
      // mem[addr] = value;
      mem.store(addr, value);
    }

    private static long read(long addr, Memory mem) {
      // return mem[(int) addr];
      return mem.read(addr);
    }

    private static long resolve(long p, int m, Memory mem, long offset) {
      return resolve(p, m, mem, offset, false);
    }

    private static long resolve(long p, int m, Memory mem, long offset, boolean write) {
      // return (m1 == 0 ? mem[(int) program1] : program1);
     switch (m) {
        case 0:
          if (write) return p;
          return mem.read(p);
        case 1:
          if (write) throw new RuntimeException("CMH");
          return p;
        case 2:
          if (write) return p + offset;
          return mem.read(p + offset);
       default:
         throw new RuntimeException("CMH");
      }
    }

    static void printOp(long pc, Memory mem) {
      System.out.printf(
          "[%3d] %4s %4s %4s %4s   ||",
          pc, mem.read(pc), mem.read(pc+1), mem.read(pc+2), mem.read(pc+3));

      for (int i = 0; i < 16; i++) {
        System.out.printf(" %4s%s", mem.read(i), (i % 4 == 0 ? "  " : ""));
      }
      System.out.println();
    }
  }
}