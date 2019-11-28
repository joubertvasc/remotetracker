package jv.android.utils.customComponents;

public enum BorderColors {
	
	BLUE(0),
	GREEN(1),
	ORANGE(2),
	RED(3),
	LIGHT_BLUE(4),
	LIGHT_GREEN(5);
			
    private final int value;
    private BorderColors(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
