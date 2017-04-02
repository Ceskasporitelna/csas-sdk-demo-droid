package cz.csas.demo.components;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.csas.demo.R;
import cz.csas.lockerui.utils.TypefaceUtils;
import cz.csas.netbanking.AccountNumber;
import cz.csas.netbanking.Amount;
import cz.csas.netbanking.accounts.MainAccount;

/**
 * The type Accounts adapter.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /01/16.
 */
public class AccountsAdapter extends BaseAdapter {

    /**
     * The Tv account number.
     */
    @Nullable
    @Bind(R.id.tv_account_number)
    TextView tvAccountNumber;

    /**
     * The Tv account balance.
     */
    @Nullable
    @Bind(R.id.tv_account_balance)
    TextView tvAccountBalance;

    /**
     * The Tv placeholder.
     */
    @Nullable
    @Bind(R.id.tv_placeholder_text)
    public TextView tvPlaceholder;

    private final int VIEW_TYPE_PLACEHOLDER = 0;
    private final int VIEW_TYPE_APPLICATION = 1;
    private List<MainAccount> mOriginalData;
    private List<MainAccount> mData;
    private Activity mActivity;

    /**
     * Instantiates a new Accounts adapter.
     *
     * @param data     the data
     * @param activity the activity
     */
    public AccountsAdapter(List<MainAccount> data, Activity activity) {
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
                convertView = mActivity.getLayoutInflater().inflate(R.layout.lv_netbanking_account_row, parent, false);
                ButterKnife.bind(this, convertView);
                holder = new Holder();
                holder.number = tvAccountNumber;
                holder.balance = tvAccountBalance;
                holder.number.setTypeface(TypefaceUtils.getRobotoRegular(mActivity));
                holder.balance.setTypeface(TypefaceUtils.getRobotoMedium(mActivity));
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            MainAccount account = mData.get(position);

            AccountNumber accountNumber = account.getAccountno();
            if (accountNumber != null)
                holder.number.setText(String.format(mActivity.getString(R.string.netbanking_accountno) + " %s", accountNumber.getNumber()));
            else
                holder.number.setText("N/A");

            Amount amount = account.getBalance();
            if (amount != null && amount.getValue() != null && amount.getPrecision() != null && amount.getCurrency() != null)
                holder.balance.setText(BigDecimal.valueOf(amount.getValue(), amount.getPrecision()) + " " + amount.getCurrency());
            else
                holder.balance.setText("N/A");
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
            holder.text.setText(R.string.netbanking_accounts_placeholder);
        }
        return convertView;
    }

    /**
     * Change data.
     *
     * @param changedData the changed data
     */
    public void changeData(List<MainAccount> changedData) {
        mOriginalData.clear();
        mData.clear();
        mOriginalData = changedData;
        mData = mOriginalData;
        notifyDataSetChanged();
    }

    private class Holder {
        /**
         * The Number.
         */
        public TextView number;
        /**
         * The Id.
         */
        public TextView balance;
    }

    private class PlaceholderHolder {
        /**
         * The Text.
         */
        public TextView text;
    }
}
