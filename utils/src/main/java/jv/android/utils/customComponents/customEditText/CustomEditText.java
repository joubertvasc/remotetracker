package jv.android.utils.customComponents.customEditText;

import jv.android.utils.R;
import jv.android.utils.customComponents.BorderColors;
import jv.android.utils.customComponents.ComponentType;
import jv.android.utils.customComponents.CustomComponentUtils;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.EditText;

public class CustomEditText extends EditText {

	private Context context;

	public CustomEditText(Context context) {
		super(context);

		this.context = context;

		setCustomValues(null);
	}

	public CustomEditText(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.context = context;

		setCustomValues(attrs);
	}

	public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		this.context = context;

		setCustomValues(attrs);
	}

	private void setCustomValues(AttributeSet attrs) {
		setBackgroundResource(R.drawable.edittext_rounded_blue);
		setTextColor(Color.BLACK);

		if (attrs != null) {
			TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomEditText);
			try {
				boolean corners = a.getInt(R.styleable.CustomEditText_Corners, 0) == 0;
				BorderColors bc = BorderColors.values()[a.getInt(R.styleable.CustomEditText_Background_Color, BorderColors.BLUE.getValue())];
				setBackgroundResource(CustomComponentUtils.getBackGroundResourceFromAttr(ComponentType.EDITTEXT, bc, corners));
				
				setTextColor(a.getColor(R.styleable.CustomEditText_android_textColor, Color.BLACK));
			} finally {
				a.recycle();
			}
		}
	}
}
