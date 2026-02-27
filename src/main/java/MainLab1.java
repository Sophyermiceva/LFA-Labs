import java.util.List;

public class MainLab1 {

  public static void main(String[] args) {
    Grammar grammar = new Grammar();

    System.out.println("Generated strings (Variant 11):");
    List<String> words = grammar.generateStrings(5);
    for (String w : words) {
      System.out.println(" - " + w);
    }

    FiniteAutomaton fa = grammar.toFiniteAutomaton();

    System.out.println("\nCheck generated strings:");
    for (String w : words) {
      System.out.println(w + " -> " + fa.stringBelongToLanguage(w));
    }

    System.out.println("\nCheck custom strings:");
    String[] tests = {"", "b", "ab", "aaaab", "ccc", "ac", "bbbb", "aab"};
    for (String t : tests) {
      System.out.println("\"" + t + "\" -> " + fa.stringBelongToLanguage(t));
    }
  }
}
