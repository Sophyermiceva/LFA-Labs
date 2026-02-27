import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Lab 2: Determinism in Finite Automata.
 * NDFA to DFA conversion, Chomsky hierarchy, FA to Regular Grammar.
 * Variant 11 demonstration.
 */
public class MainLab2 {

  public static void main(String[] args) {
    System.out.println("=== Lab 2: Determinism, NDFA→DFA, Chomsky, FA→Grammar ===\n");

    Ndfa ndfa = createVariant11Ndfa();

    System.out.println("1) Variant 11 NDFA created.");
    System.out.println("   States: " + ndfa.getStates());
    System.out.println("   Alphabet: " + ndfa.getAlphabet());
    System.out.println("   Final states: " + ndfa.getFinalStates());
    System.out.println();

    System.out.println("2) Is the FA deterministic? " + ndfa.isDeterministic());
    System.out.println("   (NDFA: δ(q2,c) leads to both q0 and q3)\n");

    NfaToDfaConverter ndfaConverter = new NfaToDfaConverter();
    FiniteAutomaton dfa = ndfaConverter.convert(ndfa);

    System.out.println("3) Converted to DFA (subset construction):");
    printDfaTable(dfa);
    System.out.println();

    FaToRegularGrammarConverter grammarConverter = new FaToRegularGrammarConverter();
    Grammar regularGrammar = grammarConverter.convert(dfa);

    System.out.println("4) Converted DFA to Regular Grammar:");
    printGrammar(regularGrammar);
    System.out.println();

    System.out.println("5) Grammar classification (Chomsky): " + regularGrammar.classifyGrammar());
    System.out.println();

    Grammar lab1Grammar = new Grammar();
    System.out.println("6) Lab 1 grammar classification: " + lab1Grammar.classifyGrammar());
  }

  /** Creates Variant 11 NDFA. δ(q2,c) = {q0, q3} makes it non-deterministic. */
  private static Ndfa createVariant11Ndfa() {
    Set<String> states = Set.of("q0", "q1", "q2", "q3");
    Set<Character> alphabet = Set.of('a', 'b', 'c');
    Set<String> finalStates = Set.of("q3");

    Map<String, Map<Character, Set<String>>> delta = new HashMap<>();

    putTransition(delta, "q0", 'a', "q1");
    putTransition(delta, "q0", 'b', "q2");
    putTransition(delta, "q1", 'a', "q3");
    putTransition(delta, "q1", 'b', "q2");
    putTransition(delta, "q2", 'c', "q0");
    putTransition(delta, "q2", 'c', "q3"); // NDFA: two targets for (q2,c)

    return new Ndfa(states, alphabet, delta, "q0", finalStates);
  }

  private static void putTransition(Map<String, Map<Character, Set<String>>> delta,
                                    String from, char symbol, String to) {
    delta
        .computeIfAbsent(from, k -> new HashMap<>())
        .computeIfAbsent(symbol, k -> new HashSet<>())
        .add(to);
  }

  private static void printDfaTable(FiniteAutomaton dfa) {
    System.out.println("   States: " + dfa.getStates());
    System.out.println("   Start: " + dfa.getStartState());
    System.out.println("   Final: " + dfa.getFinalStates());
    System.out.println("   Transitions:");
    var delta = dfa.getDelta();
    delta.entrySet().stream()
        .sorted(Map.Entry.comparingByKey())
        .forEach(e -> e.getValue().forEach((sym, target) ->
            System.out.println("      δ(" + e.getKey() + ", " + sym + ") = " + target)));
  }

  private static void printGrammar(Grammar g) {
    System.out.println("   Vn = " + g.getNonTerminals());
    System.out.println("   Vt = " + g.getTerminals());
    System.out.println("   S = " + g.getStartSymbol());
    System.out.println("   Productions:");
    g.getProductions().entrySet().stream()
        .sorted(Map.Entry.comparingByKey())
        .forEach(e -> {
          String left = e.getKey();
          for (String right : e.getValue()) {
            System.out.println("      " + left + " → " + right);
          }
        });
  }
}
