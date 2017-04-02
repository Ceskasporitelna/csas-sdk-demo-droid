package cz.csas.demo.components;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.csas.cscore.utils.TimeUtils;
import cz.csas.demo.R;
import cz.csas.lockerui.utils.TypefaceUtils;
import cz.csas.transparent_acc.Transaction;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /01/16.
 */
public class TransactionsAdapter extends BaseAdapter {

    @Nullable
    @Bind(R.id.tv_amount)
    TextView tvAmount;

    @Nullable
    @Bind(R.id.tv_date)
    TextView tvDate;

    @Nullable
    @Bind(R.id.tv_sender)
    TextView tvSender;

    @Nullable
    @Bind(R.id.tv_desc)
    TextView tvDesc;

    /**
     * The Tv placeholder.
     */
    @Nullable
    @Bind(R.id.tv_placeholder_text)
    public TextView tvPlaceholder;

    private final int VIEW_TYPE_PLACEHOLDER = 0;
    private final int VIEW_TYPE_APPLICATION = 1;
    private List<Transaction> mOriginalData;
    private List<Transaction> mData;
    private Activity mActivity;

    public TransactionsAdapter(List<Transaction> data, Activity activity) {
        this.mOriginalData = data;
        this.mData = data;
        this.mActivity = activity;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
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
            if (convertView == null) {
                convertView = mActivity.getLayoutInflater().inflate(R.layout.lv_transactions_row, parent, false);
                ButterKnife.bind(this, convertView);
                holder = new Holder();
                holder.amount = tvAmount;
                holder.date = tvDate;
                holder.sender = tvSender;
                holder.desc = tvDesc;
                holder.amount.setTypeface(TypefaceUtils.getRobotoMedium(mActivity));
                holder.date.setTypeface(TypefaceUtils.getRobotoMedium(mActivity));
                holder.sender.setTypeface(TypefaceUtils.getRobotoRegular(mActivity));
                holder.desc.setTypeface(TypefaceUtils.getRobotoRegular(mActivity));
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            Transaction transaction = mData.get(position);

            if (transaction.getAmount() != null && transaction.getAmount().getValue() != null && transaction.getAmount().getPrecision() != null && transaction.getAmount().getCurrency() != null) {
                double amount = transaction.getAmount().getValue() / Math.pow(10, transaction.getAmount().getPrecision());
                if (amount < 0)
                    holder.amount.setTextColor(Color.RED);
                holder.amount.setText(String.valueOf(amount) + " " + transaction.getAmount().getCurrency());
            }
            if (transaction.getProcessingDate() != null)
                holder.date.setText(TimeUtils.getISO8601String(transaction.getProcessingDate()));

            if (transaction.getSender() != null && transaction.getSender().getName() != null) {
                holder.sender.setText(transaction.getSender().getName());
                holder.sender.setVisibility(View.VISIBLE);
            } else
                holder.sender.setVisibility(View.GONE);

            if (transaction.getSender() != null && transaction.getSender().getDescription() != null) {
                holder.desc.setText(transaction.getSender().getDescription());
                holder.desc.setVisibility(View.VISIBLE);
            } else
                holder.desc.setVisibility(View.GONE);

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
            holder.text.setText(R.string.trans_acc_placeholder);
        }
        return convertView;
    }

    /**
     * Change data.
     *
     * @param changedData the changed data
     */

    public void changeData(List<Transaction> changedData) {
        mOriginalData.clear();
        mData.clear();
        mOriginalData = changedData;
        mData = mOriginalData;
        notifyDataSetChanged();
    }

    private class Holder {
        public TextView amount;
        public TextView date;
        public TextView sender;
        public TextView desc;
    }

    private class PlaceholderHolder {
        /**
         * The Text.
         */
        public TextView text;
    }
}
