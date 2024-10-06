package work.fking.masteringmixology;

public enum PotionComponent {
    MOX('M', "03a9f4"),
    AGA('A', "00e676"),
    LYE('L', "e91e63");

    private static final PotionComponent[] TYPES = PotionComponent.values();

    private final char character;
    private final String color;

    PotionComponent(char character, String color) {
        this.character = character;
        this.color = color;
    }

    public static PotionComponent from(int potionComponentId) {
        if (potionComponentId < 0 || potionComponentId >= TYPES.length) {
            return null;
        }
        return TYPES[potionComponentId];
    }

    public char character() {
        return character;
    }

    public String color() {
        return color;
    }
}
