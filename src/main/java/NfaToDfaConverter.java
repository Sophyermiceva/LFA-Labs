import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

/**
 * Converts a Non-deterministic Finite Automaton (NDFA) to a Deterministic
 * Finite Automaton (DFA) using the subset construction method.
 */
public class NfaToDfaConverter {

  /**
   * Converts the given NDFA to an equivalent DFA.
   * DFA states are sets of NDFA states (subset construction).
   */
  public FiniteAutomaton convert(Ndfa ndfa) {
    Set<Character> alphabet = ndfa.getAlphabet();
    Set<String> ndfaFinalStates = ndfa.getFinalStates();

    Set<Set<String>> dfaStates = new HashSet<>();
    Map<Set<String>, Map<Character, Set<String>>> dfaDelta = new HashMap<>();
    Queue<Set<String>> toProcess = new ArrayDeque<>();

    Set<String> initial = Set.of(ndfa.getStartState());
    dfaStates.add(initial);
    toProcess.add(initial);

    while (!toProcess.isEmpty()) {
      Set<String> current = toProcess.poll();

      for (char symbol : alphabet) {
        Set<String> next = ndfa.transition(current, symbol);
        if (next.isEmpty()) {
          continue;
        }
        next = canonicalSet(next);

        dfaDelta
            .computeIfAbsent(current, k -> new HashMap<>())
            .put(symbol, next);

        if (dfaStates.add(next)) {
          toProcess.add(next);
        }
      }
    }

    Map<String, Map<Character, String>> dfaDeltaSingle = new HashMap<>();
    Map<Set<String>, String> stateToName = new HashMap<>();
    int id = 0;
    for (Set<String> s : dfaStates) {
      stateToName.put(s, "S" + id);
      id++;
    }

    for (Map.Entry<Set<String>, Map<Character, Set<String>>> e : dfaDelta.entrySet()) {
      String fromName = stateToName.get(e.getKey());
      Map<Character, String> transitions = new HashMap<>();
      for (Map.Entry<Character, Set<String>> te : e.getValue().entrySet()) {
        transitions.put(te.getKey(), stateToName.get(te.getValue()));
      }
      dfaDeltaSingle.put(fromName, transitions);
    }

    Set<String> dfaStatesNamed = new HashSet<>(stateToName.values());
    String dfaStartState = stateToName.get(initial);

    Set<String> dfaFinalStates = new HashSet<>();
    for (Set<String> s : dfaStates) {
      if (containsAny(s, ndfaFinalStates)) {
        dfaFinalStates.add(stateToName.get(s));
      }
    }

    return new FiniteAutomaton(dfaStatesNamed, alphabet, dfaDeltaSingle, dfaStartState, dfaFinalStates);
  }

  private static Set<String> canonicalSet(Set<String> set) {
    return new TreeSet<>(set);
  }

  private static boolean containsAny(Set<String> container, Set<String> targets) {
    for (String t : targets) {
      if (container.contains(t)) {
        return true;
      }
    }
    return false;
  }
}
