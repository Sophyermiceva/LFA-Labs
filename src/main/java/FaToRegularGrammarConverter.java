import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Converts a Deterministic Finite Automaton (DFA) to an equivalent
 * right-linear Regular Grammar.
 */
public class FaToRegularGrammarConverter {

  /**
   * Converts the given DFA to a right-linear grammar.
   * States become non-terminals, alphabet symbols become terminals.
   * For δ(q, a) = q': add q → aq' (or q → a if q' is final).
   */
  public Grammar convert(FiniteAutomaton fa) {
    Set<String> nonTerminals = fa.getStates();
    Set<String> terminals = new HashSet<>();
    for (Character c : fa.getAlphabet()) {
      terminals.add(String.valueOf(c));
    }
    String startSymbol = fa.getStartState();
    Set<String> finalStates = fa.getFinalStates();

    Map<String, List<String>> productions = new HashMap<>();

    for (String state : fa.getStates()) {
      Map<Character, String> transitions = fa.getDelta().get(state);
      if (transitions == null) {
        transitions = Map.of();
      }

      List<String> rhs = new ArrayList<>();
      for (Map.Entry<Character, String> t : transitions.entrySet()) {
        char symbol = t.getKey();
        String nextState = t.getValue();
        if (finalStates.contains(nextState)) {
          rhs.add(String.valueOf(symbol));
        }
        rhs.add(String.valueOf(symbol) + nextState);
      }

      productions.put(state, rhs);
    }

    for (String finalState : finalStates) {
      productions.computeIfAbsent(finalState, k -> new ArrayList<>()).add("ε");
    }

    return new Grammar(nonTerminals, terminals, productions, startSymbol);
  }
}
