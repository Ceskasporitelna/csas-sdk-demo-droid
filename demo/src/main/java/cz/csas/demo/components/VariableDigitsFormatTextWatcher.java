package cz.csas.demo.components;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 24/08/16.
 */
public class VariableDigitsFormatTextWatcher implements TextWatcher {

    // Change this to what you want... ' ', '-' etc..
    private static final char space = ' ';
    private int numOfDigits;

    public VariableDigitsFormatTextWatcher(int numOfDigits) {
        this.numOfDigits = numOfDigits;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        // Remove spacing char
        if (s.length() > 0 && (s.length() % numOfDigits + 1) == 0) {
            final char c = s.charAt(s.length() - 1);
            if (space == c) {
                s.delete(s.length() - 1, s.length());
            }
        }
        // Insert char where needed.
        if (s.length() > 0 && (s.length() % (numOfDigits + 1)) == 0) {
            char c = s.charAt(s.length() - 1);
            // Only if its a digit where there should be a space we insert a space
            if (Character.isDigit(c) && TextUtils.split(s.toString(), String.valueOf(space)).length <= numOfDigits - 1) {
                s.insert(s.length() - 1, String.valueOf(space));
            }
        }
    }
}