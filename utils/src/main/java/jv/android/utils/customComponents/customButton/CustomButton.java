package jv.android.utils.customComponents.customButton;

import jv.android.utils.R;
import jv.android.utils.customComponents.BorderColors;
import jv.android.utils.customComponents.ComponentType;
import jv.android.utils.customComponents.CustomComponentUtils;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.Button;

public class CustomButton extends Button {

	private Context context;

	public CustomButton(Context context) {
		super(context);

		this.context = context;

		setCustomValues(null);
	}

	public CustomButton(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.context = context;

		setCustomValues(attrs);
	}

	public CustomButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		this.context = context;

		setCustomValues(attrs);
	}

	private void setCustomValues(AttributeSet attrs) {
		setBackgroundResource(R.drawable.button_rounded_blue);
		setTextColor(Color.WHITE);

		if (attrs != null) {
			TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomButton);
			try {
				boolean corners = a.getInt(R.styleable.CustomButton_Corners, 0) == 0;
				BorderColors bc = BorderColors.values()[a.getInt(R.styleable.CustomButton_Background_Color, BorderColors.BLUE.getValue())];
				setBackgroundResource(CustomComponentUtils.getBackGroundResourceFromAttr(ComponentType.BUTTON, bc, corners));
				
				setTextColor(a.getColor(R.styleable.CustomButton_android_textColor, Color.WHITE));
			} finally {
				a.recycle();
			}
		}
	}

}
