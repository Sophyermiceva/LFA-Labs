import java.util.Map;
import java.util.Set;

public class FiniteAutomaton {

  private final Set<String> states; // Q
  private final Set<Character> alphabet; // Sigma
  private final Map<String, Map<Character, String>> delta;  // delta =  переходы
  private final String startState; // q0 = S
  private final Set<String> finalStates; // F

  public FiniteAutomaton(Set<String> states, Set<Character> alphabet,
                         Map<String, Map<Character, String>> delta,
                         String startState, Set<String> finalStates) {
    this.states = states;
    this.alphabet = alphabet;
    this.delta = delta;
    this.startState = startState;
    this.finalStates = finalStates;
  }

  public Set<String> getStates() {
    return states;
  }

  public Set<Character> getAlphabet() {
    return alphabet;
  }

  public Map<String, Map<Character, String>> getDelta() {
    return delta;
  }

  public String getStartState() {
    return startState;
  }

  public Set<String> getFinalStates() {
    return finalStates;
  }

  /**
   * Checks whether this FA is deterministic.
   * A DFA has at most one next state per (state, symbol).
   * This structure enforces single transitions, so it is always deterministic.
   */
  public boolean isDeterministic() {
    return true;
  }

  public boolean stringBelongToLanguage(final String inputString) {
    String current = startState;

    for (int i = 0; i < inputString.length(); i++) {
      char ch = inputString.charAt(i);

      if (!alphabet.contains(ch)) {
        return false;
      }

      Map<Character, String> transitions = delta.get(current);
      if (transitions == null) {
        return false;
      }

      String next = transitions.get(ch);
      if (next == null) {
        return false;
      }

      current = next;
    }

    return finalStates.contains(current);
  }
}
