import java.util.*;

public class Grammar {

  private Set<String> nonTerminals; // Vn
  private Set<String> terminals; // Vt
  private Map<String, List<String>> productions; // P
  private String startSymbol; // S

  public Grammar() {
    this.nonTerminals = Set.of("S", "B", "D");
    this.terminals = Set.of("a", "b", "c");
    this.startSymbol = "S";

    this.productions = new HashMap<>();
    this.productions.put("S", List.of("aB", "bB"));
    this.productions.put("B", List.of("bD", "cB", "aS"));
    this.productions.put("D", List.of("b", "aD"));
  }

  public String generateString() {
    Random rnd = new Random();
    String current = startSymbol;

    int maxSteps = 100;
    int steps = 0;

    while (containsNonTerminal(current) && steps < maxSteps) {
      steps++;

      for (int i = 0; i < current.length(); i++) {
        String sym = String.valueOf(current.charAt(i));

        if (nonTerminals.contains(sym)) {
          List<String> rules = productions.get(sym);
          String chosen = rules.get(rnd.nextInt(rules.size()));

          current = current.substring(0, i) + chosen + current.substring(i + 1);
          break;
        }
      }
    }

    if (containsNonTerminal(current)) {
      return generateString();
    }

    return current;
  }

  public List<String> generateStrings(int n) {
    List<String> res = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      res.add(generateString());
    }
    return res;
  }

  private boolean containsNonTerminal(String s) {
    for (int i = 0; i < s.length(); i++) {
      if (nonTerminals.contains(String.valueOf(s.charAt(i)))) {
        return true;
      }
    }
    return false;
  }

  public FiniteAutomaton toFiniteAutomaton() {

    String finalState = "F";

    Set<String> states = new HashSet<>(nonTerminals);
    states.add(finalState);

    Set<Character> alphabet = new HashSet<>();
    for (String t : terminals) {
      alphabet.add(t.charAt(0));
    }

    Map<String, Map<Character, String>> delta = new HashMap<>();

    for (Map.Entry<String, List<String>> entry : productions.entrySet()) {
      String left = entry.getKey();
      for (String right : entry.getValue()) {
        char terminal = right.charAt(0);

        String next;
        if (right.length() == 1) {
          // A → a
          next = finalState;
        } else {
          // A → aB
          next = String.valueOf(right.charAt(1));
        }

        delta.computeIfAbsent(left, k -> new HashMap<>()).put(terminal, next);
      }
    }

    String startState = startSymbol;
    Set<String> finalStates = Set.of(finalState);

    return new FiniteAutomaton(states, alphabet, delta, startState, finalStates);
  }
}



/*
Variant 11:
VN={S, B, D},
VT={a, b, c},
P={
    S → aB
    S → bB
    B → bD
    D → b
    D → aD
    B → cB
    B → aS
}
 */