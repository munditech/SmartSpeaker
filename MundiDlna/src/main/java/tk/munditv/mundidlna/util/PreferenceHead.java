package tk.munditv.mundidlna.util;

import android.content.Context;
import android.preference.Preference;
import android.view.View;

import tk.munditv.mundidlna.R;

public class PreferenceHead extends Preference {

	public PreferenceHead(Context context) {
		super(context);
		setLayoutResource(R.layout.preference_head);
	}

	@Override
	protected void onBindView(View view) {
		super.onBindView(view);
	}
}
