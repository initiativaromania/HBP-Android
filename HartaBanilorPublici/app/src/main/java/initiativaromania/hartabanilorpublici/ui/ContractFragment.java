package initiativaromania.hartabanilorpublici.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import initiativaromania.hartabanilorpublici.R;
import initiativaromania.hartabanilorpublici.comm.CommManager;
import initiativaromania.hartabanilorpublici.comm.CommManagerResponse;
import initiativaromania.hartabanilorpublici.data.Company;
import initiativaromania.hartabanilorpublici.data.Contract;
import initiativaromania.hartabanilorpublici.data.PublicInstitution;

/**
 * Created by claudiu on 10/17/17.
 */

public class ContractFragment extends Fragment {
    private View originalView;
    private Contract contract = null;
    private Fragment fragmentCopy;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        originalView = inflater.inflate(R.layout.fragment_contract, container, false);
        fragmentCopy = this;

        Bundle bundle = getArguments();
        if (bundle == null)
            return originalView;

        contract = new Contract();
        contract.id = bundle.getInt(CommManager.BUNDLE_CONTRACT_ID);
        contract.type = bundle.getInt(CommManager.BUNDLE_CONTRACT_TYPE);

        /* Check for PI details */
        int piID = bundle.getInt(CommManager.BUNDLE_CONTRACT_PI_ID);
        if (piID != 0) {
            contract.pi = new PublicInstitution();
            contract.pi.id = piID;
            contract.pi.name = bundle.getString(CommManager.BUNDLE_CONTRACT_PI_NAME);
            displayPIButton();
        }

        /* Check for Company details */
        int compID = bundle.getInt(CommManager.BUNDLE_CONTRACT_COMP_ID);
        if (compID != 0) {
            contract.company = new Company();
            contract.company.id = compID;
            contract.company.name = bundle.getString(CommManager.BUNDLE_CONTRACT_PI_NAME);
        }

        System.out.println("Created fragment for contract " + contract.id +
                " type " + contract.type);
        if (contract.pi != null)
            System.out.println("PI " + contract.pi.id + " name " + contract.pi.name);


        /* Get contract information from the server */
        getServerADInfo();

        return originalView;
    }


    /* Get contract information from the server */
    private void getServerADInfo() {
        if (contract == null) {
            System.out.println("Uninitialized contract");
            return;
        }

        switch(contract.type) {
            case Contract.CONTRACT_TYPE_DIRECT_ACQUISITION:
                /* Send request to get the direct acquisition */
                CommManager.requestAD(new CommManagerResponse() {
                    @Override
                    public void processResponse(JSONArray response) {
                        receiveAD(response);
                    }

                    @Override
                    public void onErrorOccurred(String errorMsg) {
                        Toast.makeText(fragmentCopy.getContext(), errorMsg,
                                Toast.LENGTH_SHORT).show();
                    }
                }, contract.id);
                break;

            case Contract.CONTRACT_TYPE_TENDER:
                /* Send request to get the tender */
                CommManager.requestTender(new CommManagerResponse() {
                    @Override
                    public void processResponse(JSONArray response) {
                        receiveTender(response);
                    }

                    @Override
                    public void onErrorOccurred(String errorMsg) {
                        Toast.makeText(fragmentCopy.getContext(), errorMsg,
                                Toast.LENGTH_SHORT).show();
                    }
                }, contract.id);
                break;

            default:
                System.out.println("Unknown contract type for id " + contract.id);
        }
    }


    /* Receive Direct Acquisition information from the server */
    private void receiveAD(JSONArray response) {
        System.out.println("ContractFragment: receiveAD " + response);

        try {
            JSONObject contractSummary = response.getJSONObject(0);
            contract.procedureType = contractSummary.getString(CommManager.JSON_AD_PROCEDURE_TYPE);
            contract.participationDate = contractSummary.getString(CommManager.JSON_AD_PARTICIP_DATE);
            contract.finaliseContractType = contractSummary.getString(CommManager.JSON_AD_FINAL_CONTRACT_TYPE);
            contract.number = contractSummary.getString(CommManager.JSON_AD_NUMBER);
            contract.date = contractSummary.getString(CommManager.JSON_AD_DATE);
            contract.title = contractSummary.getString(CommManager.JSON_AD_TITLE);
            contract.value = contractSummary.getDouble(CommManager.JSON_AD_VALUE);
            contract.currency = contractSummary.getString(CommManager.JSON_AD_CURRENCY);
            contract.valueEUR = contractSummary.getDouble(CommManager.JSON_AD_VALUE_EUR);
            contract.valueRON = contractSummary.getDouble(CommManager.JSON_AD_VALUE_RON);
            contract.CPVCode = contractSummary.getString(CommManager.JSON_AD_CPV_CODE);
            contract.institutionID = contractSummary.getInt(CommManager.JSON_AD_INSTITUTION_ID);
            contract.companyID = contractSummary.getInt(CommManager.JSON_AD_COMPANY_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* Show the info received from the server */
        displayAD();
    }


    /* Receive Tender information from the server */
    private void receiveTender(JSONArray response) {
        System.out.println("ContractFragment: receiveTender " + response);

//        try {
//            JSONObject piSummary = response.getJSONObject(0);
//            pi.CUI = piSummary.getString(CommManager.JSON_PI_CUI);
//            pi.address = piSummary.getString(CommManager.JSON_PI_ADDRESS);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        /* Show the info received from the server */
//        displayServerPIInfo();
    }


    /* AD info */
    private void displayAD() {
        TextView text = ((TextView) originalView.findViewById(R.id.titleContractView));
        if (text != null)
            text.setText(contract.title);

        text = ((TextView) originalView.findViewById(R.id.nrContractContract));
        if (text != null)
            text.setText(contract.number);

        text = ((TextView) originalView.findViewById(R.id.valContractRONContract));
        if (text != null)
            text.setText(contract.valueRON + "");

        text = ((TextView) originalView.findViewById(R.id.valContractEURContract));
        if (text != null)
            text.setText(contract.valueEUR + "");

        text = ((TextView) originalView.findViewById(R.id.dataContractContract));
        if (text != null)
            text.setText(contract.date + "");

        text = ((TextView) originalView.findViewById(R.id.dataAnuntContract));
        if (text != null)
            text.setText(contract.participationDate + "");

        text = ((TextView) originalView.findViewById(R.id.codCPVContract));
        if (text != null)
            text.setText(contract.CPVCode + "");

        text = ((TextView) originalView.findViewById(R.id.tipProceduraContract));
        if (text != null)
            text.setText(contract.procedureType + "");

        text = ((TextView) originalView.findViewById(R.id.tipIncheiereContract));
        if (text != null)
            text.setText(contract.finaliseContractType + "");

    }


    /* Display Company and Institution buttons */
    private void displayPIButton() {
        View piView = originalView.findViewById(R.id.piInContract);

        TextView contractName = (TextView) piView.findViewById(R.id.listTitle);
        contractName.setText(contract.pi.name);
    }


    /* Display Company and Institution buttons */
    private void displayCompanyButton() {
        View piView = originalView.findViewById(R.id.companyInContract);

        TextView contractName = (TextView) piView.findViewById(R.id.listTitle);
        contractName.setText(contract.company.name);
    }

}
