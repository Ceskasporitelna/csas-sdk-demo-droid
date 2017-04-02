package cz.csas.demo.components;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.csas.corporate.AccountNumber;
import cz.csas.corporate.accounts.Account;
import cz.csas.demo.R;
import cz.csas.lockerui.utils.TypefaceUtils;

/**
 * The type Accounts adapter.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /01/16.
 */
public class CorporateAccountsAdapter extends BaseAdapter {

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
    private List<Account> mOriginalData;
    private List<Account> mData;
    private Activity mActivity;

    /**
     * Instantiates a new Accounts adapter.
     *
     * @param data     the data
     * @param activity the activity
     */
    public CorporateAccountsAdapter(List<Account> data, Activity activity) {
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

            Account account = mData.get(position);
            AccountNumber accountNumber = account.getAccountNo();
            if (accountNumber != null)
                holder.number.setText(String.format(mActivity.getString(R.string.netbanking_accountno) + " %s", accountNumber.getAccountNumber()));
            else
                holder.number.setText("N/A");

            if (account.getAccountName() != null)
                holder.balance.setText(account.getAccountName());
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
    public void changeData(List<Account> changedData) {
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
