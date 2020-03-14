package com.stay.toolslibrary.widget;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * 拼音快速检索bar
 */
public class AlphabetInviteScrollBar extends View {

	private Paint mPaint = new Paint();
	private String[] mAlphabet = new String[] { "A", "B", "C", "D", "E", "F",
			"G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
			"T", "U", "V", "W", "X", "Y", "Z", "#" };
	private boolean mPressed;
	private int mCurPosIdx = -1;
	private int mOldPosIdx = -1;
	private OnTouchBarListener mTouchListener;
	private TextView LetterNotice;

	public AlphabetInviteScrollBar(Context arg0, AttributeSet arg1, int arg2) {
		super(arg0, arg1, arg2);
		// TODO Auto-generated constructor stub
	}

	public AlphabetInviteScrollBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public AlphabetInviteScrollBar(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public void setTextView(TextView LetterNotice) {
		this.LetterNotice = LetterNotice;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		int width = this.getWidth();
		int height = this.getHeight();

		int singleLetterH = height / mAlphabet.length;

		// if (mPressed) {
		// canvas.drawColor(Color.parseColor("#40000000"));
		// }
		int size =sp2px(getContext(), 12);
		for (int i = 0; i < mAlphabet.length; i++) {
			mPaint.setColor(Color.parseColor("#333333"));
			mPaint.setAntiAlias(true);
			mPaint.setTextSize(size);

			float x = width / 2 - mPaint.measureText(mAlphabet[i]) / 2;
			float y = singleLetterH * i + singleLetterH;

			if (i == mCurPosIdx) {
				mPaint.setColor(Color.parseColor("#3593EA"));
				mPaint.setFakeBoldText(true);
			}
			canvas.drawText(mAlphabet[i], x, y, mPaint);
			mPaint.reset();
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {

		int action = arg0.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			mPressed = true;
			mCurPosIdx = (int) (arg0.getY() / this.getHeight() * mAlphabet.length);
			if (mTouchListener != null && mOldPosIdx != mCurPosIdx) {
				if ((mCurPosIdx >= 0) && (mCurPosIdx < mAlphabet.length)) {
					mTouchListener.onTouch(mAlphabet[mCurPosIdx]);
					this.invalidate();
				}
				mOldPosIdx = mCurPosIdx;
			}

			LetterNotice.setText(mAlphabet[mCurPosIdx]);
			LetterNotice.setVisibility(View.VISIBLE);

			return true;
		case MotionEvent.ACTION_UP:

			if (LetterNotice != null) {
				LetterNotice.setVisibility(View.INVISIBLE);
			}

			mPressed = false;
			mCurPosIdx = -1;
			this.invalidate();
			return true;
		case MotionEvent.ACTION_MOVE:
			mCurPosIdx = (int) (arg0.getY() / this.getHeight() * mAlphabet.length);
			if (mTouchListener != null && mCurPosIdx != mOldPosIdx) {
				if ((mCurPosIdx >= 0) && (mCurPosIdx < mAlphabet.length)) {
					mTouchListener.onTouch(mAlphabet[mCurPosIdx]);
					this.invalidate();
				}
				mOldPosIdx = mCurPosIdx;
			}

			if (mCurPosIdx >= 0 && mCurPosIdx < mAlphabet.length) {
				LetterNotice.setText(mAlphabet[mCurPosIdx]);
				LetterNotice.setVisibility(View.VISIBLE);
			}

			return true;
		default:
			return super.onTouchEvent(arg0);
		}

	}

	public static interface OnTouchBarListener {
		void onTouch(String letter);
	}

	public void setOnTouchBarListener(OnTouchBarListener listener) {
		mTouchListener = listener;
	}

	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}
}
