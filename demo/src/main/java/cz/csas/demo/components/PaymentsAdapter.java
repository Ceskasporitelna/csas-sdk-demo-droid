package cz.csas.demo.components;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.csas.cscore.utils.TimeUtils;
import cz.csas.demo.R;
import cz.csas.lockerui.utils.TypefaceUtils;
import cz.csas.netbanking.Amount;
import cz.csas.netbanking.orders.Payment;

/**
 * The type Payments adapter.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /01/16.
 */
public class PaymentsAdapter extends BaseAdapter {

    /**
     * The Tv amount.
     */
    @Nullable
    @Bind(R.id.tv_amount)
    TextView tvAmount;

    /**
     * The Tv date.
     */
    @Nullable
    @Bind(R.id.tv_date)
    TextView tvDate;

    /**
     * The Tv sender.
     */
    @Nullable
    @Bind(R.id.tv_sender)
    TextView tvSender;

    /**
     * The Tv receiver.
     */
    @Nullable
    @Bind(R.id.tv_receiver)
    TextView tvReceiver;

    /**
     * The Tv placeholder.
     */
    @Nullable
    @Bind(R.id.tv_placeholder_text)
    public TextView tvPlaceholder;

    private final int VIEW_TYPE_PLACEHOLDER = 0;
    private final int VIEW_TYPE_APPLICATION = 1;
    private List<Payment> mOriginalData;
    private List<Payment> mData;
    private Activity mActivity;

    /**
     * Instantiates a new Payments adapter.
     *
     * @param data     the data
     * @param activity the activity
     */
    public PaymentsAdapter(List<Payment> data, Activity activity) {
        this.mOriginalData = data;
        this.mData = data;
        this.mActivity = activity;
    }

    @Override
    public int getCount() {
        return mData.size() != 0 ? mData.size() : 1;
    }

    @Override
    public Object getItem(int position) {
        if (mData.size() != 0)
            return mData.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public int getItemViewType(int position) {
        return (mData.size() == 0) ? VIEW_TYPE_PLACEHOLDER : VIEW_TYPE_APPLICATION;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (getItemViewType(position) == VIEW_TYPE_APPLICATION) {
            Holder holder;
            if (convertView == null || convertView.getTag() instanceof PlaceholderHolder) {
                convertView = mActivity.getLayoutInflater().inflate(R.layout.lv_netbanking_payment_row, parent, false);
                ButterKnife.bind(this, convertView);
                holder = new Holder();
                holder.amount = tvAmount;
                holder.date = tvDate;
                holder.sender = tvSender;
                holder.receiver = tvReceiver;
                holder.amount.setTypeface(TypefaceUtils.getRobotoMedium(mActivity));
                holder.date.setTypeface(TypefaceUtils.getRobotoMedium(mActivity));
                holder.sender.setTypeface(TypefaceUtils.getRobotoRegular(mActivity));
                holder.receiver.setTypeface(TypefaceUtils.getRobotoRegular(mActivity));
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            Payment payment = mData.get(position);

            Amount amount = payment.getAmount();
            if (amount != null && amount.getValue() != null && amount.getPrecision() != null && amount.getCurrency() != null) {
                BigDecimal value = BigDecimal.valueOf(amount.getValue(), amount.getPrecision());
                if (value.compareTo(BigDecimal.ZERO) < 0)
                    holder.amount.setTextColor(Color.RED);
                holder.amount.setText(String.valueOf(value) + " " + amount.getCurrency());
            }
            Date executionDate = payment.getExecutionDate();
            if (executionDate != null)
                holder.date.setText(TimeUtils.getPlainDateString(executionDate));

            String sender = payment.getSenderName();
            if (sender != null) {
                holder.sender.setText(String.format(mActivity.getString(R.string.netbanking_sender) + " %s", sender));
                holder.sender.setVisibility(View.VISIBLE);
            } else
                holder.sender.setVisibility(View.GONE);

            String receiver = payment.getReceiverName();
            if (receiver != null) {
                holder.receiver.setText(String.format(mActivity.getString(R.string.netbanking_receiver) + " %s", receiver));
                holder.receiver.setVisibility(View.VISIBLE);
            } else
                holder.receiver.setVisibility(View.GONE);
        } else {
            PlaceholderHolder holder;
            if (convertView == null || convertView.getTag() instanceof Holder) {
                convertView = mActivity.getLayoutInflater().inflate(R.layout.place_item_placeholder_component, parent, false);
                ButterKnife.bind(this, convertView);
                holder = new PlaceholderHolder();
                holder.text = tvPlaceholder;
                convertView.setTag(holder);
            } else {
                holder = (PlaceholderHolder) convertView.getTag();
            }

            holder.text.setTypeface(TypefaceUtils.getRobotoRegular(mActivity));
            holder.text.setText(R.string.netbanking_payments_placeholder);
        }
        return convertView;
    }

    /**
     * Change data.
     *
     * @param changedData the changed data
     */
    public void changeData(List<Payment> changedData) {
        mOriginalData.clear();
        mData.clear();
        mOriginalData = changedData;
        mData = mOriginalData;
        notifyDataSetChanged();
    }


    private class Holder {
        /**
         * The Amount.
         */
        public TextView amount;
        /**
         * The Date.
         */
        public TextView date;
        /**
         * The Sender.
         */
        public TextView sender;
        /**
         * The Receiver.
         */
        public TextView receiver;
    }

    private class PlaceholderHolder {
        /**
         * The Text.
         */
        public TextView text;
    }
}
