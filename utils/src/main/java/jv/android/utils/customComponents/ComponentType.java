package jv.android.utils.customComponents;

public enum ComponentType {
	
	BUTTON(0),
	EDITTEXT(1),
	GENERIC(2);
	
    private final int value;
    private ComponentType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
	

}
