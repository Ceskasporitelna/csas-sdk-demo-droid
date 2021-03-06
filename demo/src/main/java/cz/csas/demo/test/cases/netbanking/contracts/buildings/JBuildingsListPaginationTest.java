package cz.csas.demo.test.cases.netbanking.contracts.buildings;

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
import cz.csas.cscore.webapi.PaginatedParameters;
import cz.csas.cscore.webapi.Pagination;
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.netbanking.AccountNumber;
import cz.csas.netbanking.contracts.buildings.BuildingAccountType;
import cz.csas.netbanking.contracts.buildings.BuildingSaving;
import cz.csas.netbanking.contracts.buildings.BuildingsContract;
import cz.csas.netbanking.contracts.buildings.BuildingsContractStatus;
import cz.csas.netbanking.contracts.buildings.BuildingsListResponse;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JBuildingsListPaginationTest extends TestCase {

    private final String X_JUDGE_CASE = "contracts.buildings.list.pagination";
    private boolean flag = false;

    @Override
    public void run(final TestCallback callback) {
        mTestCallback = callback;
        ((JudgeRestService) mJudgeClient.getService()).nextCase(X_JUDGE_CASE, mXJudgeSessionHeader, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                final Map<String, String> headers = new HashMap<>();
                headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
                mNetbankingJudgeClient.setGlobalHeaders(headers);

                int pages[] = {0, 1, 0};
                for (int i = 0; i < pages.length; i++) {
                    final int page = pages[i];
                    if (i == 2)
                        flag = true;
                    mNetbankingJudgeClient.getContractsResource().getBuildingsResource().list(new PaginatedParameters(new Pagination(page, 1)),
                            new CallbackWebApi<BuildingsListResponse>() {
                                @Override
                                public void success(BuildingsListResponse buildingsListResponse) {
                                    List<String> contractHolders = new ArrayList<String>();
                                    contractHolders.add("Hana Bielčíková");
                                    List<String> flags = new ArrayList<String>();
                                    flags.add("accountQueryAllowed");
                                    switch (page) {
                                        case 0:
                                            if (!assertEquals(Long.valueOf(0), buildingsListResponse.getPageNumber()))
                                                return;
                                            if (!assertEquals(Long.valueOf(2), buildingsListResponse.getPageCount()))
                                                return;
                                            if (!assertEquals(Long.valueOf(1), buildingsListResponse.getPageSize()))
                                                return;
                                            if (!assertEquals(Long.valueOf(1), buildingsListResponse.getNextPage()))
                                                return;

                                            if (!assertEquals(1, buildingsListResponse.getBuildingsContracts().size()))
                                                return;
                                            BuildingsContract building = buildingsListResponse.getBuildingsContracts().get(0);
                                            if (!assertEquals("BCEF6B001FAE755D163A6CC9475E9FDFD9CD4A79", building.getId()))
                                                return;
                                            if (!assertEquals(BuildingAccountType.BUILD_SAVING, building.getType()))
                                                return;
                                            if (!assertEquals("280", building.getProduct()))
                                                return;
                                            if (!assertEquals("Stavební spoření", building.getProductI18N()))
                                                return;
                                            if (!assertEquals(Long.valueOf(169200), building.getBalance().getValue()))
                                                return;
                                            if (!assertEquals(Integer.valueOf(2), building.getBalance().getPrecision()))
                                                return;
                                            if (!assertEquals("CZK", building.getBalance().getCurrency()))
                                                return;
                                            if (!assertEquals(contractHolders, building.getContractHolders()))
                                                return;
                                            if (!assertEquals(BuildingsContractStatus.ACTIVE, building.getStatus()))
                                                return;

                                            BuildingSaving saving = building.getSaving();
                                            if (!assertEquals(Long.valueOf(14100000), saving.getTargetAmount().getValue()))
                                                return;
                                            if (!assertEquals(Integer.valueOf(2), saving.getTargetAmount().getPrecision()))
                                                return;
                                            if (!assertEquals("CZK", saving.getTargetAmount().getCurrency()))
                                                return;
                                            if (!assertEquals(Long.valueOf(70500), saving.getAgreedMonthlySavings().getValue()))
                                                return;
                                            if (!assertEquals(Integer.valueOf(2), saving.getAgreedMonthlySavings().getPrecision()))
                                                return;
                                            if (!assertEquals("CZK", saving.getAgreedMonthlySavings().getCurrency()))
                                                return;

                                            if (!assertEquals(flags, building.getFlags()))
                                                return;

                                            AccountNumber accountNo = building.getAccountno();
                                            if (!assertEquals("51-1050445627", accountNo.getNumber()))
                                                return;
                                            if (!assertEquals("8060", accountNo.getBankCode()))
                                                return;
                                            if (!assertEquals("CZ", accountNo.getCountryCode()))
                                                return;
                                            if (!assertEquals("CZ1580600000511050445627", accountNo.getCzIban()))
                                                return;
                                            if (!assertEquals("GIBACZPX", accountNo.getCzBic()))
                                                return;
                                            break;
                                        case 1:
                                            if (!assertEquals(Long.valueOf(1), buildingsListResponse.getPageNumber()))
                                                return;
                                            if (!assertEquals(Long.valueOf(2), buildingsListResponse.getPageCount()))
                                                return;
                                            if (!assertEquals(Long.valueOf(1), buildingsListResponse.getPageSize()))
                                                return;

                                            if (!assertEquals(1, buildingsListResponse.getBuildingsContracts().size()))
                                                return;
                                            building = buildingsListResponse.getBuildingsContracts().get(0);
                                            if (!assertEquals("EC91C2D1B33D5A9E927C5B3EC509589818E292D1", building.getId()))
                                                return;
                                            if (!assertEquals(BuildingAccountType.BUILD_SAVING, building.getType()))
                                                return;
                                            if (!assertEquals("280", building.getProduct()))
                                                return;
                                            if (!assertEquals("Stavební spoření", building.getProductI18N()))
                                                return;
                                            if (!assertEquals(Long.valueOf(0), building.getBalance().getValue()))
                                                return;
                                            if (!assertEquals(Integer.valueOf(2), building.getBalance().getPrecision()))
                                                return;
                                            if (!assertEquals("CZK", building.getBalance().getCurrency()))
                                                return;
                                            if (!assertEquals(contractHolders, building.getContractHolders()))
                                                return;
                                            if (!assertEquals(BuildingsContractStatus.ACTIVE, building.getStatus()))
                                                return;

                                            saving = building.getSaving();
                                            if (!assertEquals(Long.valueOf(18600000), saving.getTargetAmount().getValue()))
                                                return;
                                            if (!assertEquals(Integer.valueOf(2), saving.getTargetAmount().getPrecision()))
                                                return;
                                            if (!assertEquals("CZK", saving.getTargetAmount().getCurrency()))
                                                return;
                                            if (!assertEquals(Long.valueOf(93000), saving.getAgreedMonthlySavings().getValue()))
                                                return;
                                            if (!assertEquals(Integer.valueOf(2), saving.getAgreedMonthlySavings().getPrecision()))
                                                return;
                                            if (!assertEquals("CZK", saving.getAgreedMonthlySavings().getCurrency()))
                                                return;

                                            if (!assertEquals(flags, building.getFlags()))
                                                return;

                                            accountNo = building.getAccountno();
                                            if (!assertEquals("51-1050690710", accountNo.getNumber()))
                                                return;
                                            if (!assertEquals("8060", accountNo.getBankCode()))
                                                return;
                                            if (!assertEquals("CZ", accountNo.getCountryCode()))
                                                return;
                                            if (!assertEquals("CZ1780600000511050690710", accountNo.getCzIban()))
                                                return;
                                            if (!assertEquals("GIBACZPX", accountNo.getCzBic()))
                                                return;
                                            break;

                                    }
                                    if (flag)
                                        mTestCallback.result(mTestResult);
                                }

                                @Override
                                public void failure(CsSDKError error) {
                                    handleError(error);
                                }
                            });
                }
            }

            @Override
            public void failure(CsRestError error) {
                handleError(error);
            }
        });
    }

    @Override
    public String getName() {
        return "JBuildingsListPaginationTest";
    }

}
