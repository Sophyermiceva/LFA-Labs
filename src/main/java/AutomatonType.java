/**
 * Chomsky hierarchy classification for grammars.
 * Type 3 ⊂ Type 2 ⊂ Type 1 ⊂ Type 0
 */
public enum AutomatonType {
  /** Type 3: Regular grammar. Right-linear or left-linear productions. */
  TYPE_3_REGULAR,

  /** Type 2: Context-free grammar. A → α, |α| ≥ 1. */
  TYPE_2_CONTEXT_FREE,

  /** Type 1: Context-sensitive grammar. αAβ → αγβ, |γ| ≥ 1 (except S→ε). */
  TYPE_1_CONTEXT_SENSITIVE,

  /** Type 0: Unrestricted grammar. No restrictions. */
  TYPE_0_UNRESTRICTED
}
