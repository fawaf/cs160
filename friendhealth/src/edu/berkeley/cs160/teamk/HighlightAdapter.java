package edu.berkeley.cs160.teamk;

import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;


public class HighlightAdapter extends SimpleAdapter {
	private int[] colors = new int[] {Color.BLACK, 0xff003300};
	private int highlight;
	
	public HighlightAdapter(Context context, 
			List<HashMap<String, String>> items,
			int resource,
			String[] from,
			int[] to,
			int hl) {
		super(context, items, resource, from, to);
		highlight = hl;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		if (position == highlight) {
			view.setBackgroundColor(colors[1]);
		}
		else {
			view.setBackgroundColor(colors[0]);
		}
		return view;
	}
}
