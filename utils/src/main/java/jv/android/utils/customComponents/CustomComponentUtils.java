package jv.android.utils.customComponents;

import jv.android.utils.R;

public class CustomComponentUtils {

	public static int getBackGroundResourceFromAttr(ComponentType ct, BorderColors bc, boolean corners) {
		if (ct == ComponentType.BUTTON)
			return getButtonBackGround(corners, bc);
		else if (ct == ComponentType.EDITTEXT)
			return getEditTextBackGround(corners, bc);			
		else
			return getGenericBackGroundResourceFromAttr(corners, false, bc);			
	}

	private static int getButtonBackGround(boolean corners, BorderColors bc) {
		switch (bc) {
		case BLUE:        	
			if (corners)
				return R.drawable.button_rounded_blue;
			else 
				return R.drawable.button_flat_blue;
		case GREEN:
			if (corners)
				return R.drawable.button_rounded_green;
			else 
				return R.drawable.button_flat_green;
		case LIGHT_BLUE:
			if (corners)
				return R.drawable.button_rounded_light_blue;
			else 
				return R.drawable.button_flat_light_blue;
		case LIGHT_GREEN:
			if (corners)
				return R.drawable.button_rounded_light_green;
			else 
				return R.drawable.button_flat_light_green;
		case ORANGE:
			if (corners)
				return R.drawable.button_rounded_orange;
			else 
				return R.drawable.button_flat_orange;
		case RED:
			if (corners)
				return R.drawable.button_rounded_red;
			else 
				return R.drawable.button_flat_red;
		default:
			return R.drawable.button_rounded_blue;
		}
	}

	private static int getEditTextBackGround(boolean corners, BorderColors bc) {
		switch (bc) {
		case BLUE:        	
			if (corners)
				return R.drawable.edittext_rounded_blue;
			else 
				return R.drawable.edittext_flat_blue;
		case GREEN:
			if (corners)
				return R.drawable.edittext_rounded_green;
			else 
				return R.drawable.edittext_flat_green;
		case LIGHT_BLUE:
			if (corners)
				return R.drawable.edittext_rounded_light_blue;
			else 
				return R.drawable.edittext_flat_light_blue;
		case LIGHT_GREEN:
			if (corners)
				return R.drawable.edittext_rounded_light_green;
			else 
				return R.drawable.edittext_flat_light_green;
		case ORANGE:
			if (corners)
				return R.drawable.edittext_rounded_orange;
			else 
				return R.drawable.edittext_flat_orange;
		case RED:
			if (corners)
				return R.drawable.edittext_rounded_red;
			else 
				return R.drawable.edittext_flat_red;
		default:
			return R.drawable.edittext_rounded_blue;
		}
	}

	public static int getGenericBackGroundResourceFromAttr(boolean corners, boolean solid, BorderColors bc) {
		switch (bc) {
		case BLUE:        	
			if (solid) {
				if (corners)
					return R.drawable.generic_solid_rounded_shape_blue;
				else 
					return R.drawable.generic_solid_flat_shape_blue;
			} else {
				if (corners)
					return R.drawable.generic_rounded_shape_blue;
				else 
					return R.drawable.generic_flat_shape_blue;
			}
		case GREEN:
			if (solid) {
				if (corners)
					return R.drawable.generic_solid_rounded_shape_green;
				else 
					return R.drawable.generic_solid_flat_shape_green;
			} else {
				if (corners)
					return R.drawable.generic_rounded_shape_green;
				else 
					return R.drawable.generic_flat_shape_green;
			}
		case LIGHT_BLUE:
			if (solid) {
				if (corners)
					return R.drawable.generic_solid_rounded_shape_light_blue;
				else 
					return R.drawable.generic_solid_flat_shape_light_blue;
			} else {
				if (corners)
					return R.drawable.generic_rounded_shape_light_blue;
				else 
					return R.drawable.generic_flat_shape_light_blue;
			}
		case LIGHT_GREEN:
			if (solid) {
				if (corners)
					return R.drawable.generic_solid_rounded_shape_light_green;
				else 
					return R.drawable.generic_solid_flat_shape_light_green;
			} else {
				if (corners)
					return R.drawable.generic_rounded_shape_light_green;
				else 
					return R.drawable.generic_flat_shape_light_green;
			}
		case ORANGE:
			if (solid) {
				if (corners)
					return R.drawable.generic_solid_rounded_shape_orange;
				else 
					return R.drawable.generic_solid_flat_shape_orange;
			} else {
				if (corners)
					return R.drawable.generic_rounded_shape_orange;
				else 
					return R.drawable.generic_flat_shape_orange;
			}
		case RED:
			if (solid) {
				if (corners)
					return R.drawable.generic_solid_rounded_shape_red;
				else 
					return R.drawable.generic_solid_flat_shape_red;
			} else {
				if (corners)
					return R.drawable.generic_rounded_shape_red;
				else 
					return R.drawable.generic_flat_shape_red;
			}
		default:
			return R.drawable.edittext_rounded_blue;
		}
	}
}
