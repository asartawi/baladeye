package com.AOU.baladeye.adapters;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.AOU.baladeye.PayInvoiceActivity;
import com.AOU.baladeye.R;
import com.AOU.baladeye.models.Invoice;

public class InvoicesListAdapter extends ArrayAdapter<Invoice> {

	private List<Invoice> invoicesList;
	private Context context;

	public InvoicesListAdapter(Context context, List<Invoice> objects) {
		super(context, R.layout.activity_invoices, objects);
		this.invoicesList = objects;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.activity_invoices, parent,
				false);
		TextView typeView = (TextView) rowView.findViewById(R.id.type);
		TextView valueView = (TextView) rowView.findViewById(R.id.value);

		TextView detailsView = (TextView) rowView
				.findViewById(R.id.extra_details);
		typeView.setText(invoicesList.get(position).getType());
		valueView.setText(invoicesList.get(position).getValue());
		detailsView.setText(invoicesList.get(position).getExtraDetails());

		TextView dateView = (TextView) rowView.findViewById(R.id.bill_date);

		TextView isPaidView = (TextView) rowView.findViewById(R.id.is_paid);
		dateView.setText(invoicesList.get(position).getBillDate());
		isPaidView
				.setText(invoicesList.get(position).isPaid() == true ? R.string.is_paid_true
						: R.string.is_paid_false);
		Button payBtn = (Button) rowView.findViewById(R.id.pay_btn);
		payBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, PayInvoiceActivity.class);
				context.startActivity(intent);
			}
		});
		if (invoicesList.get(position).isPaid() == false) {
			payBtn.setVisibility(View.VISIBLE);
		} else {
			payBtn.setVisibility(View.INVISIBLE);

		}
		return rowView;
	}
}