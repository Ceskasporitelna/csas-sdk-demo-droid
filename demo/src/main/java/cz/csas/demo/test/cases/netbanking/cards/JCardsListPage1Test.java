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
import cz.csas.cscore.webapi.Pagination;
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
import cz.csas.netbanking.cards.CardsListResponse;
import cz.csas.netbanking.cards.CardsParameters;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JCardsListPage1Test extends TestCase {

    private final String X_JUDGE_CASE = "cards.list.page1";

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;
        ((JudgeRestService) mJudgeClient.getService()).nextCase(X_JUDGE_CASE, mXJudgeSessionHeader, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                final Map<String, String> headers = new HashMap<>();
                headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
                mNetbankingJudgeClient.setGlobalHeaders(headers);

                CardsParameters parameters = new CardsParameters(new Pagination(1, 1), null);

                mNetbankingJudgeClient.getCardsResource().list(parameters, new CallbackWebApi<CardsListResponse>() {
                    @Override
                    public void success(CardsListResponse cardsListResponse) {

                        if (!assertEquals(Long.valueOf(1), cardsListResponse.getPageNumber()))
                            return;
                        if (!assertEquals(Long.valueOf(2), cardsListResponse.getPageCount()))
                            return;
                        if (!assertEquals(Long.valueOf(1), cardsListResponse.getPageSize()))
                            return;

                        List<Card> cards = cardsListResponse.getCards();
                        if (!assertEquals(1, cards.size()))
                            return;

                        Card card = cards.get(0);
                        CardMainAccount account = card.getMainAccount();
                        AccountNumber accountNumber = account.getAccountno();

                        if (!assertEquals("FAFBFBDCAE6465F6DB8058746A828E195922CB15", card.getId()))
                            return;
                        if (!assertEquals("VRBA ALEŠ", card.getOwner()))
                            return;
                        if (!assertEquals("451161XXXXXX6026", card.getNumber()))
                            return;
                        if (!assertEquals("1", card.getSequenceNumber()))
                            return;
                        if (!assertEquals("4511611", card.getProductCode()))
                            return;
                        if (!assertEquals(TimeUtils.getPlainDate("2018-03-31"), card.getExpiryDate()))
                            return;
                        if (!assertEquals(CardState.ACTIVE, card.getState()))
                            return;
                        if (!assertEquals(CardType.BANK_CARD, card.getType()))
                            return;
                        if (!assertEquals(CardProvider.ERSTE_BANK, card.getProvider()))
                            return;
                        if (!assertEquals(TimeUtils.getPlainDate("2015-04-01"), card.getValidFromDate()))
                            return;
                        if (!assertEquals(CardCharacteristic.MAIN, card.getCharacteristic()))
                            return;

                        if (!assertEquals("4B2F9EBE742BCAE1E98A78E12F6FBC62464A74EE", account.getId()))
                            return;
                        if (!assertEquals("Aleš Vrba", account.getHolderName()))
                            return;
                        if (!assertEquals("2059930033", accountNumber.getNumber()))
                            return;
                        if (!assertEquals("0800", accountNumber.getBankCode()))
                            return;
                        if (!assertEquals("CZ", accountNumber.getCountryCode()))
                            return;
                        if (!assertEquals("CZ1208000000002059930033", accountNumber.getCzIban()))
                            return;
                        if (!assertEquals("GIBACZPX", accountNumber.getCzBic()))
                            return;

                        if (!assertEquals(CardDeliveryMode.HOME, card.getCardDeliveryMode()))
                            return;
                        List<String> flags = new ArrayList<String>();
                        flags.add("automaticReplacementOn");
                        flags.add("secureOnlineShoppingEnabled");
                        flags.add("contactlessEnabled");
                        if (!assertEquals(flags, card.getFlags()))
                            return;
                        List<String> features = new ArrayList<String>();
                        features.add("limitChange");
                        features.add("reissuePin");
                        features.add("contactless");
                        features.add("onlineLocking");
                        features.add("secureOnlineShopping");
                        features.add("temporaryLimitChange");
                        features.add("cardDelivery");
                        if (!assertEquals(features, card.getFeatures()))
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
        return "JCardsListPage1Test";
    }

}
