import java.util.*;

public class Grammar {

  private Set<String> nonTerminals; // Vn
  private Set<String> terminals; // Vt
  private Map<String, List<String>> productions; // P
  private String startSymbol; // S

  /**
   * Constructor for programmatic creation (e.g. from FA-to-Grammar conversion).
   */
  public Grammar(Set<String> nonTerminals, Set<String> terminals,
                 Map<String, List<String>> productions, String startSymbol) {
    this.nonTerminals = new HashSet<>(nonTerminals);
    this.terminals = new HashSet<>(terminals);
    this.productions = new HashMap<>();
    for (Map.Entry<String, List<String>> e : productions.entrySet()) {
      this.productions.put(e.getKey(), new ArrayList<>(e.getValue()));
    }
    this.startSymbol = startSymbol;
  }

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

        if (nonTerminals.contains(sym)) { // первый нетерминал
          List<String> rules = productions.get(sym);
          String chosen = rules.get(rnd.nextInt(rules.size())); //рандом правило
          if ("ε".equals(chosen)) {
            chosen = "";
          }

          current = current.substring(0, i) + chosen + current.substring(i + 1);
          break;
        }
      }
    }

    if (containsNonTerminal(current)) {
      return generateString(); // если шагов не хватило тто все сначала
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

  /**
   * Classifies this grammar according to the Chomsky hierarchy.
   * Type 3 (Regular) ⊂ Type 2 (Context-free) ⊂ Type 1 (Context-sensitive) ⊂ Type 0 (Unrestricted)
   */
  public AutomatonType classifyGrammar() {
    Boolean linearity = null; // true=right-linear, false=left-linear, null=undetermined
    boolean allType3 = true;
    boolean allType2 = true;
    boolean allType1 = true;

    for (Map.Entry<String, List<String>> entry : productions.entrySet()) {
      String left = entry.getKey();
      for (String right : entry.getValue()) {
        Type3Form form = getType3Form(left, right);
        if (form == Type3Form.NONE) {
          allType3 = false;
        } else if (form == Type3Form.RIGHT_LINEAR) {
          if (linearity != null && !linearity) {
            allType3 = false;
          }
          linearity = true;
        } else if (form == Type3Form.LEFT_LINEAR) {
          if (linearity != null && linearity) {
            allType3 = false;
          }
          linearity = false;
        }
        if (!isType2Production(left, right)) {
          allType2 = false;
        }
        if (!isType1Production(left, right)) {
          allType1 = false;
        }
      }
    }

    if (allType3) {
      return AutomatonType.TYPE_3_REGULAR;
    }
    if (allType2) {
      return AutomatonType.TYPE_2_CONTEXT_FREE;
    }
    if (allType1) {
      return AutomatonType.TYPE_1_CONTEXT_SENSITIVE;
    }
    return AutomatonType.TYPE_0_UNRESTRICTED;
  }

  private enum Type3Form { RIGHT_LINEAR, LEFT_LINEAR, TERMINAL_OR_EPSILON, NONE }

  private Type3Form getType3Form(String left, String right) {
    if (right.isEmpty()) {
      return left.equals(startSymbol) ? Type3Form.TERMINAL_OR_EPSILON : Type3Form.NONE;
    }
    if ("ε".equals(right)) {
      return Type3Form.TERMINAL_OR_EPSILON;
    }
    if (right.length() == 1) {
      return terminals.contains(right) ? Type3Form.TERMINAL_OR_EPSILON : Type3Form.NONE;
    }
    if (right.length() >= 2) {
      String first = String.valueOf(right.charAt(0));
      String rest = right.substring(1);
      if (terminals.contains(first) && nonTerminals.contains(rest)) {
        return Type3Form.RIGHT_LINEAR;
      }
      if (nonTerminals.contains(first) && terminals.contains(rest)) {
        return Type3Form.LEFT_LINEAR;
      }
    }
    return Type3Form.NONE;
  }

  /** Type 2: A → α, single non-terminal on left, |α| ≥ 1. */
  private boolean isType2Production(String left, String right) {
    if (left.length() != 1 || !nonTerminals.contains(left)) {
      return false;
    }
    if (right.isEmpty()) {
      return left.equals(startSymbol);
    }
    return true;
  }

  /** Type 1: αAβ → αγβ, |γ| ≥ 1, or S → ε. Context-sensitive. */
  private boolean isType1Production(String left, String right) {
    if (right.isEmpty()) {
      return left.equals(startSymbol);
    }
    if (left.length() > right.length()) {
      return false; // Context-sensitive: |αAβ| ≤ |αγβ|
    }
    return true;
  }

  private boolean containsNonTerminal(String s) { //есть ли заглавные нетерменалы
    for (int i = 0; i < s.length(); i++) {
      if (nonTerminals.contains(String.valueOf(s.charAt(i)))) {
        return true;
      }
    }
    return false;
  }

  public FiniteAutomaton toFiniteAutomaton() {

    String finalState = "F"; //финальное состояние

    Set<String> states = new HashSet<>(nonTerminals);
    states.add(finalState);// все состояния

    Set<Character> alphabet = new HashSet<>();
    for (String t : terminals) {
      alphabet.add(t.charAt(0));
    }

    Map<String, Map<Character, String>> delta = new HashMap<>();

    for (Map.Entry<String, List<String>> entry : productions.entrySet()) { //ищем  где final state
      String left = entry.getKey();
      for (String right : entry.getValue()) {
        if ("ε".equals(right)) {
          continue;
        }
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

  public Set<String> getNonTerminals() {
    return Set.copyOf(nonTerminals);
  }

  public Set<String> getTerminals() {
    return Set.copyOf(terminals);
  }

  public Map<String, List<String>> getProductions() {
    Map<String, List<String>> result = new HashMap<>();
    for (Map.Entry<String, List<String>> e : productions.entrySet()) {
      result.put(e.getKey(), new ArrayList<>(e.getValue()));
    }
    return result;
  }

  public String getStartSymbol() {
    return startSymbol;
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