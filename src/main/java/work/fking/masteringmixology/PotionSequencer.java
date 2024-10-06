package work.fking.masteringmixology;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class PotionSequencer {
    private static boolean isGoodPotion(PotionOrder o) {
        PotionComponent[] comps = o.potionType().components();
        return comps[0] != comps[1] || comps[0] != comps[2];
    }

    private static int badTiebreakScore1(PotionOrder o) {
        PotionComponent c = o.potionType().components()[0];
        if (c == PotionComponent.MOX) {
            return 2;
        } else if (c == PotionComponent.AGA) {
            return 1;
        } else if (c == PotionComponent.LYE) {
            return 0;
        } else {
            return -1; // oops?
        }
    }

    private static int badTiebreakScore2(PotionOrder o) {
        PotionModifier m = o.potionModifier();
        if (m == PotionModifier.CRYSTALISED) {
            return 2;
        } else if (m == PotionModifier.HOMOGENOUS) {
            return 1;
        } else if (m == PotionModifier.CONCENTRATED) {
            return 0;
        } else {
            return -1; // oops?
        }
    }

    public static List<PotionOrder> evaluateSequence(List<PotionOrder> input) {
        // remove AAA, MMM, LLL
        List<PotionOrder> seq1 = new ArrayList<>();
        for (PotionOrder o : input) {
            if (isGoodPotion(o)) {
                seq1.add(o);
            }
        }

        // ...unless they're the only option
        if (seq1.size() == 0) {
            // prefer MMM then AAA then LLL
            // if tied, prefer agitator -> retort -> alembic
            PotionOrder best = null;
            for (PotionOrder o : input) {
                if (best == null) {
                    best = o;
                } else {
                    int old_s = badTiebreakScore1(best);
                    int new_s = badTiebreakScore1(o);
                    if (old_s < new_s) {
                        best = o;
                    } else if (old_s == new_s) {
                        int old_s2 = badTiebreakScore2(best);
                        int new_s2 = badTiebreakScore2(o);
                        if (old_s2 < new_s2) {
                            best = o;
                        }
                    }
                }
            }
            seq1.add(best);
        }

        // sort
        // alembic -> agitator -> retort
        // crystalised -> homogenous -> concentrated
        List<PotionOrder> seq2 = new ArrayList<>();
        for (PotionOrder o : seq1) {
            if (o.potionModifier() == PotionModifier.CRYSTALISED) {
                seq2.add(o);
            }
        }
        for (PotionOrder o : seq1) {
            if (o.potionModifier() == PotionModifier.HOMOGENOUS) {
                seq2.add(o);
            }
        }
        for (PotionOrder o : seq1) {
            if (o.potionModifier() == PotionModifier.CONCENTRATED) {
                seq2.add(o);
            }
        }

        return seq2;
    }

    // returns null if the potion is ready
    public static PotionComponent getNextInLeverSequence(PotionType goal, List<PotionComponent> mixer) {
        if (goal == PotionType.MIXALOT) {
            List<PotionComponent> remaining = Arrays.asList(PotionComponent.AGA, PotionComponent.LYE, PotionComponent.MOX);
            for (int i = mixer.size() - 1; i >= 0; i--) {
                if (remaining.contains(mixer.get(i))) {
                    remaining.remove(mixer.get(i));
                } else {
                    return remaining.get(0);
                }
            }
            return null;
        } else {
            PotionComponent[] recipe = goal.components();
            for (int mix_start = 0; mix_start < mixer.size(); mix_start++) {
                boolean already_good = true;
                for (int recipe_offset = 0; recipe_offset < recipe.length; recipe_offset++) {
                    if (mix_start + recipe_offset >= mixer.size()) {
                        return recipe[recipe_offset];
                    }
                    if (recipe[recipe_offset] != mixer.get(mix_start + recipe_offset)) {
                        already_good = false;
                        break;
                    }
                }
                if (already_good) {
                    return null;
                }
            }
            return recipe[0];
        }
    }
}
