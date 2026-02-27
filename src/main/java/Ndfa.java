import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Represents a Non-deterministic Finite Automaton (NDFA).
 * Transitions map (state, symbol) to a set of possible next states.
 */
public class Ndfa {

  private final Set<String> states;
  private final Set<Character> alphabet;
  private final Map<String, Map<Character, Set<String>>> delta;
  private final String startState;
  private final Set<String> finalStates;

  public Ndfa(Set<String> states,
              Set<Character> alphabet,
              Map<String, Map<Character, Set<String>>> delta,
              String startState,
              Set<String> finalStates) {
    this.states = Set.copyOf(states);
    this.alphabet = Set.copyOf(alphabet);
    this.delta = copyDelta(delta);
    this.startState = startState;
    this.finalStates = Set.copyOf(finalStates);
  }

  private static Map<String, Map<Character, Set<String>>> copyDelta(
      Map<String, Map<Character, Set<String>>> delta) {
    Map<String, Map<Character, Set<String>>> result = new HashMap<>();
    for (Map.Entry<String, Map<Character, Set<String>>> e : delta.entrySet()) {
      Map<Character, Set<String>> inner = new HashMap<>();
      if (e.getValue() != null) {
        for (Map.Entry<Character, Set<String>> ie : e.getValue().entrySet()) {
          if (ie.getValue() != null) {
            inner.put(ie.getKey(), Set.copyOf(ie.getValue()));
          }
        }
      }
      result.put(e.getKey(), inner);
    }
    return result;
  }

  public Set<String> getStates() {
    return Collections.unmodifiableSet(states);
  }

  public Set<Character> getAlphabet() {
    return Collections.unmodifiableSet(alphabet);
  }

  public Map<String, Map<Character, Set<String>>> getDelta() {
    return Collections.unmodifiableMap(delta);
  }

  public String getStartState() {
    return startState;
  }

  public Set<String> getFinalStates() {
    return Collections.unmodifiableSet(finalStates);
  }

  /**
   * Checks whether this FA is deterministic.
   * An FA is deterministic iff for every (state, symbol) there is at most one next state,
   * and there are no epsilon transitions.
   */
  public boolean isDeterministic() {
    for (Map.Entry<String, Map<Character, Set<String>>> stateEntry : delta.entrySet()) {
      Map<Character, Set<String>> symbolTransitions = stateEntry.getValue();
      if (symbolTransitions == null) {
        continue;
      }
      for (Set<String> targets : symbolTransitions.values()) {
        if (targets == null || targets.size() > 1) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Returns the set of states reachable from the given state set on the given symbol.
   * Returns empty set if no transition exists.
   */
  public Set<String> transition(Set<String> fromStates, char symbol) {
    Set<String> result = new HashSet<>();
    for (String state : fromStates) {
      Map<Character, Set<String>> stateTransitions = delta.get(state);
      if (stateTransitions != null) {
        Set<String> targets = stateTransitions.get(symbol);
        if (targets != null) {
          result.addAll(targets);
        }
      }
    }
    return result;
  }
}
