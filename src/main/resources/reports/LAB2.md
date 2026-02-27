# DETERMINISM IN FINITE AUTOMATA
## NDFA TO DFA CONVERSION & CHOMSKY HIERARCHY
### Variant 11

Course: Formal Languages & Finite Automata  
Author: Sofia Ermicev  
Lab: 2

---

## OBJECTIVES

1. Implement conversion from Non-deterministic Finite Automaton (NDFA) to Deterministic Finite Automaton (DFA) using the subset construction method.
2. Implement conversion from Finite Automaton to Regular Grammar.
3. Add grammar classification according to the Chomsky hierarchy.
4. Implement a method to check whether a finite automaton is deterministic.
5. Demonstrate all components using Variant 11.

---

## THEORY

### Determinism in Finite Automata

A **Deterministic Finite Automaton (DFA)** has the property that for every state and every input symbol, there is exactly one next state. Formally:

- For all q ∈ Q and a ∈ Σ: |δ(q, a)| = 1

A **Non-deterministic Finite Automaton (NDFA)** allows:
- Multiple next states for the same (state, symbol) pair: δ(q, a) = {q₁, q₂, …}
- Optionally, ε-transitions (epsilon moves)

### NDFA vs DFA

| Property       | DFA                    | NDFA                         |
|----------------|------------------------|------------------------------|
| Transitions    | δ: Q × Σ → Q           | δ: Q × Σ → P(Q)              |
| Next states    | Exactly one            | Set of states (possibly ∅)   |
| Expressiveness | Same class of languages| Same class of languages      |

Both recognize exactly the **regular languages**.

### Subset Construction (NDFA → DFA)

The subset construction builds a DFA whose states are **subsets** of the NDFA states:

1. **Initial state**: {q₀} (the NDFA start state)
2. **Transition**: For state set S and symbol a:
   \[
   \delta_{DFA}(S, a) = \bigcup_{q \in S} \delta_{NDFA}(q, a)
   \]
3. **Final states**: Any DFA state S such that S ∩ F ≠ ∅ (S contains at least one NDFA final state)
4. **Iteration**: Process all reachable subsets until no new states are found.

### Chomsky Hierarchy

| Type | Name             | Production form              | Example           |
|------|------------------|------------------------------|-------------------|
| 3    | Regular          | A → aB, A → a, A → ε         | A → aB \| b       |
| 2    | Context-free     | A → α                        | A → aBb \| ε      |
| 1    | Context-sensitive| αAβ → αγβ, \|γ\| ≥ 1         | aAb → aabb        |
| 0    | Unrestricted     | No restrictions              | α → β             |

---

## IMPLEMENTATION OVERVIEW

### New Classes

| Class                     | Purpose                                              |
|---------------------------|------------------------------------------------------|
| `Ndfa`                    | Represents NDFA with Set-based transitions           |
| `NfaToDfaConverter`       | Subset construction: NDFA → DFA                      |
| `FaToRegularGrammarConverter` | Converts DFA to right-linear Regular Grammar     |
| `AutomatonType`           | Enum for Chomsky types (TYPE_3, TYPE_2, TYPE_1, TYPE_0) |

### Modified/Extended Classes

| Class              | Changes                                                                 |
|--------------------|-------------------------------------------------------------------------|
| `Grammar`          | Added `classifyGrammar()`, new constructor, ε handling, getters        |
| `FiniteAutomaton`  | Added getters, `isDeterministic()` (always true for DFA structure)      |

---

## VARIANT 11 EXPLANATION

### Definition

- **Q** = {q0, q1, q2, q3}
- **Σ** = {a, b, c}
- **F** = {q3}
- **q0** = start state

### Transitions

| δ    | a   | b   | c      |
|------|-----|-----|--------|
| q0   | q1  | q2  | —      |
| q1   | q3  | q2  | —      |
| q2   | —   | —   | q0, q3 |
| q3   | —   | —   | —      |

### Non-determinism

The automaton is **non-deterministic** because:

\[
\delta(q_2, c) = \{q_0, q_3\}
\]

From state q2 on symbol c, there are two possible next states (q0 and q3). A DFA requires at most one next state per (state, symbol).

---

## NDFA TRANSITION TABLE

| State | a   | b   | c      |
|-------|-----|-----|--------|
| q0    | q1  | q2  | —      |
| q1    | q3  | q2  | —      |
| q2    | —   | —   | q0, q3 |
| q3    | —   | —   | —      |

*Start: q0 | Final: q3*

---

## DFA (AFTER SUBSET CONSTRUCTION)

DFA states are labeled S0..S4, each representing a subset of NDFA states:

| DFA State | Subset  | Final? |
|-----------|---------|--------|
| S0        | {q1}    | No     |
| S1        | {q2}    | No     |
| S2        | {q0, q3}| Yes    |
| S3        | {q3}    | Yes    |
| S4        | {q0}    | No (start) |

### DFA Transition Table

| State | a   | b   | c   |
|-------|-----|-----|-----|
| S0    | S3  | S1  | —   |
| S1    | —   | —   | S2  |
| S2    | S0  | S1  | —   |
| S3    | —   | —   | —   |
| S4    | S0  | S1  | —   |

*Start: S4 ({q0}) | Final: S2, S3*

---

## REGULAR GRAMMAR RESULT

The DFA is converted to a right-linear grammar:

**Vn** = {S0, S1, S2, S3, S4}  
**Vt** = {a, b, c}  
**S** = S4

**Productions:**

| Left | Right            |
|------|------------------|
| S0   | a \| aS3 \| bS1  |
| S1   | c \| cS2         |
| S2   | aS0 \| bS1 \| ε  |
| S3   | ε                |
| S4   | aS0 \| bS1       |

---

## DETERMINISM EXPLANATION

**Method:** `Ndfa.isDeterministic()`

The method checks that for every state q and symbol a, the transition set δ(q, a) contains **at most one** state. If any transition maps to 2 or more states, the automaton is non-deterministic.

For Variant 11: δ(q2, c) = {q0, q3} → **not deterministic**.

For the converted DFA (`FiniteAutomaton`): the structure enforces single transitions per (state, symbol), so it is always deterministic.

---

## CONCLUSION

- **NDFA** was created for Variant 11 with the non-deterministic transition δ(q2, c) → {q0, q3}.
- **Subset construction** successfully produced an equivalent **DFA** with 5 states (subsets of the original 4).
- **FA → Regular Grammar** conversion yielded a right-linear grammar with ε productions for final states.
- **Chomsky classification** correctly identifies the resulting grammar as **Type 3 (Regular)**.
- The implementation is generic and can handle other variants by constructing the appropriate `Ndfa` and using the same converters.
