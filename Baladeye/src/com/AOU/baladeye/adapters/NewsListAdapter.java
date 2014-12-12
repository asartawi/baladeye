package com.AOU.baladeye.adapters;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.AOU.baladeye.R;
import com.AOU.baladeye.models.News;

public class NewsListAdapter extends ArrayAdapter<News> {

	private List<News> newsList;
	private Context context;

	public NewsListAdapter(Context context, List<News> objects) {
		super(context, R.layout.activity_news, objects);
		this.newsList = objects;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.activity_news, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.label);
		TextView contentView = (TextView) rowView
				.findViewById(R.id.content_view);

		ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
		textView.setText(newsList.get(position).getTitle());
		contentView.setText(newsList.get(position).getContent());
		Bitmap bitmap = newsList.get(position).getBitmap();
		imageView.setImageBitmap(bitmap);
		return rowView;
	}
}
