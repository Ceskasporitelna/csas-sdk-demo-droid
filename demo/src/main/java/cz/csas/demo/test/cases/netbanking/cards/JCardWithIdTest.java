package cz.csas.demo.test.cases.netbanking.cards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.csas.cscore.client.rest.Callback;
import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.client.rest.CsRestError;
import cz.csas.cscore.client.rest.client.Response;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.judge.Constants;
import cz.csas.cscore.judge.JudgeRestService;
import cz.csas.cscore.utils.TimeUtils;
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.netbanking.AccountNumber;
import cz.csas.netbanking.cards.Card;
import cz.csas.netbanking.cards.CardCharacteristic;
import cz.csas.netbanking.cards.CardDeliveryMode;
import cz.csas.netbanking.cards.CardMainAccount;
import cz.csas.netbanking.cards.CardProvider;
import cz.csas.netbanking.cards.CardState;
import cz.csas.netbanking.cards.CardType;
import cz.csas.netbanking.cards.LockReason;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JCardWithIdTest extends TestCase {

    private final String X_JUDGE_CASE = "cards.withId.get";

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;
        ((JudgeRestService) mJudgeClient.getService()).nextCase(X_JUDGE_CASE, mXJudgeSessionHeader, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                final Map<String, String> headers = new HashMap<>();
                headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
                mNetbankingJudgeClient.setGlobalHeaders(headers);

                String cardId = "33A813886442D946122C78305EC4E482DE9F574D";
                mNetbankingJudgeClient.getCardsResource().withId(cardId).get(new CallbackWebApi<Card>() {
                    @Override
                    public void success(Card card) {
                        CardMainAccount account = card.getMainAccount();
                        AccountNumber accountNumber = account.getAccountno();

                        if (!assertEquals("33A813886442D946122C78305EC4E482DE9F574D", card.getId()))
                            return;
                        if (!assertEquals("VOJTÍŠKOVÁ ANNA", card.getOwner()))
                            return;
                        if (!assertEquals("451161XXXXXX1987", card.getNumber()))
                            return;
                        if (!assertEquals("2", card.getSequenceNumber()))
                            return;
                        if (!assertEquals("4511611", card.getProductCode()))
                            return;
                        if (!assertEquals(TimeUtils.getPlainDate("2018-03-31"), card.getExpiryDate()))
                            return;
                        if (!assertEquals(CardState.TEMPORARY_BLOCKED, card.getState()))
                            return;
                        if (!assertEquals(CardType.BANK_CARD, card.getType()))
                            return;
                        if (!assertEquals(CardProvider.ERSTE_BANK, card.getProvider()))
                            return;
                        if (!assertEquals(TimeUtils.getPlainDate("2015-04-01"), card.getValidFromDate()))
                            return;
                        if (!assertEquals(CardCharacteristic.MAIN, card.getCharacteristic()))
                            return;

                        if (!assertEquals("076E1DBCCCD38729A99D93AC8D3E8273237C7E36", account.getId()))
                            return;
                        if (!assertEquals("Anna Vojtíšková", account.getHolderName()))
                            return;
                        if (!assertEquals("2328489013", accountNumber.getNumber()))
                            return;
                        if (!assertEquals("0800", accountNumber.getBankCode()))
                            return;
                        if (!assertEquals("CZ", accountNumber.getCountryCode()))
                            return;
                        if (!assertEquals("CZ5908000000002328489013", accountNumber.getCzIban()))
                            return;
                        if (!assertEquals("GIBACZPX", accountNumber.getCzBic()))
                            return;

                        if (!assertEquals(CardDeliveryMode.HOME, card.getCardDeliveryMode()))
                            return;
                        List<String> flags = new ArrayList<String>();
                        flags.add("automaticReplacementOn");
                        flags.add("secureOnlineShoppingEnabled");
                        flags.add("activationAllowed");
                        flags.add("contactlessEnabled");
                        if (!assertEquals(flags, card.getFlags()))
                            return;
                        List<String> features = new ArrayList<String>();
                        features.add("contactless");
                        features.add("replacementCard");
                        features.add("secureOnlineShopping");
                        if (!assertEquals(features, card.getFeatures()))
                            return;
                        if (!assertEquals(LockReason.LOSS, card.getLockReason()))
                            return;
                        if (!assertEquals("Visa Classic debetní - Partner", card.getProductI18N()))
                            return;
                        mTestCallback.result(mTestResult);
                    }

                    @Override
                    public void failure(CsSDKError error) {
                        handleError(error);
                    }
                });
            }

            @Override
            public void failure(CsRestError error) {
                handleError(error);
            }
        });
    }

    @Override
    public String getName() {
        return "JCardWithIdTest";
    }

}
