package initiativaromania.hartabanilorpublici.ui;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
    private static int CONTRACT_TABLE_DEFAULT_ROW_NUMBER        = 8;
    private static final String RED_FLAG_PREFERENCE             = "red_flag_preference";


    private View originalView;
    public Contract contract = null;
    private Fragment fragmentCopy;
    private LayoutInflater inflater;
    private int rowNumber = CONTRACT_TABLE_DEFAULT_ROW_NUMBER;
    private String oldTitle;
    private int parentID;
    private SharedPreferences redFlagPrefs;


    public void setParentID(int parentID) {
        this.parentID = parentID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        originalView = inflater.inflate(R.layout.fragment_contract, container, false);

        fragmentCopy = this;
        this.inflater = inflater;

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
            contract.company.name = bundle.getString(CommManager.BUNDLE_CONTRACT_COMP_NAME);
            displayCompanyButton();
        }

        System.out.println("Created fragment for contract " + contract.id +
                " type " + contract.type);
        if (contract.pi != null)
            System.out.println("PI " + contract.pi.id + " name " + contract.pi.name);

        /* Setup red flag button */
        setupRedFlag();

        /* Change the Activity title */
        updateActivityTitle();

        /* Get contract information from the server */
        getServerContractInfo();

        return originalView;
    }


    /* Setup the Red flag button */
    public void setupRedFlag() {
        System.out.println("Setting up the Red Flag button");

        redFlagPrefs = originalView.getContext().getSharedPreferences(RED_FLAG_PREFERENCE, 0);

        Button button = (Button)originalView.findViewById(R.id.button_red_flag);
        if (button == null)
            return;

        button.setText("Semnalează (" + contract.votes + ")");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Check whether you have voted before */
                int votedBefore = redFlagPrefs.getInt("Contract" + contract.id, -1);
                System.out.println("Saved preference value for contract " + contract.id + " id " + votedBefore);

                if (votedBefore == -1) {

                    CommManagerResponse redFlagResponse = new CommManagerResponse() {
                        @Override
                        public void processResponse(JSONArray response) {
                            receiveRedFlagAck(response);
                        }

                        @Override
                        public void onErrorOccurred(String errorMsg) {
                            if (fragmentCopy != null)
                                Toast.makeText(fragmentCopy.getContext(), errorMsg,
                                        Toast.LENGTH_SHORT).show();
                        }
                    };

                    System.out.println("Never voted. Calling justify from button");
                    if (contract.type == Contract.CONTRACT_TYPE_DIRECT_ACQUISITION) {
                        CommManager.requestRedFlagAD(redFlagResponse, contract.id);
                    } else {
                        CommManager.requestRedFlagTender(redFlagResponse, contract.id);
                    }
                } else
                    Toast.makeText(originalView.getContext(), "Ai mai semnalat acest contract. Mulțumim!",
                            Toast.LENGTH_SHORT).show();
            }
        });

    }


    /* Receive server ACK that contract has been marked */
    private void receiveRedFlagAck(JSONArray response) {
        contract.votes++;
        Button button = (Button)originalView.findViewById(R.id.button_red_flag);
        if (button == null)
            return;

        button.setText("Semnalează (" + contract.votes + ")");

        /* Remember the fact that you voted for this contract */
        SharedPreferences.Editor editor = redFlagPrefs.edit();
        editor.putInt("Contract" + contract.id, 1);
        editor.commit();

        Toast.makeText(originalView.getContext(), "Contractul a fost semnalat", Toast.LENGTH_LONG).show();
    }


    /* Update the Activity title to match the contract */
    private void updateActivityTitle() {
        oldTitle = ((HomeActivity) getActivity()).getActionBarTitle();

        switch (contract.type) {
            case Contract.CONTRACT_TYPE_DIRECT_ACQUISITION:
                ((HomeActivity) getActivity()).setActionBarTitle("Achiziție Directă");
                break;

            case Contract.CONTRACT_TYPE_TENDER:
                ((HomeActivity) getActivity()).setActionBarTitle("Licitație");
                break;

            default:
                System.out.println("Unknown contract type for id " + contract.id);
        }
    }


    /* Get contract information from the server */
    private void getServerContractInfo() {
        if (contract == null) {
            System.out.println("Uninitialized contract");
            return;
        }

        switch (contract.type) {
            case Contract.CONTRACT_TYPE_DIRECT_ACQUISITION:
                /* Send request to get the direct acquisition */
                CommManager.requestAD(new CommManagerResponse() {
                    @Override
                    public void processResponse(JSONArray response) {
                        receiveAD(response);
                    }

                    @Override
                    public void onErrorOccurred(String errorMsg) {
                        if (fragmentCopy != null)
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
                        if (fragmentCopy != null)
                            Toast.makeText(fragmentCopy.getContext(), errorMsg,
                                    Toast.LENGTH_SHORT).show();
                    }
                }, contract.id);
                break;

            default:
                System.out.println("Unknown contract type for id " + contract.id);
        }
    }


    /* Request Contract and/or PI info based on server response */
    private void requestAdditionalInfo() {
        if (contract == null) {
            System.out.println("Uninitialized contract");
            return;
        }

        if (contract.pi == null) {
            /* Send request to get the Public institution */
            CommManager.requestPISummary(new CommManagerResponse() {
                @Override
                public void processResponse(JSONArray response) {
                    receivePI(response);
                }

                @Override
                public void onErrorOccurred(String errorMsg) {
                    if (fragmentCopy != null)
                        Toast.makeText(fragmentCopy.getContext(), errorMsg,
                                Toast.LENGTH_SHORT).show();
                }
            }, contract.institutionID);
        }

        if (contract.company != null)
            return;

        /* Get company info, we only need the name */
        /* Send request to get the Company */
        CommManager.requestCompany(new CommManagerResponse() {
            @Override
            public void processResponse(JSONArray response) {
                receiveCompany(response);
            }

            @Override
            public void onErrorOccurred(String errorMsg) {
                if (fragmentCopy != null)
                    Toast.makeText(fragmentCopy.getContext(), errorMsg,
                            Toast.LENGTH_SHORT).show();
            }
        }, contract.companyID);
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
            contract.votes = contractSummary.getInt(CommManager.JSON_CONTRACT_RED_FLAGS);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* Show the info received from the server */
        displayAD();

        /* Get PI and company info based on response */
        requestAdditionalInfo();
    }


    /* Receive Tender information from the server */
    private void receiveTender(JSONArray response) {
        System.out.println("ContractFragment: receiveTender " + response);

        try {
            JSONObject contractSummary = response.getJSONObject(0);
            contract.procedureType = contractSummary.getString(CommManager.JSON_TENDER_PROCEDURE_TYPE);
            contract.tenderContractType = contractSummary.getString(CommManager.JSON_TENDER_CONTRACT_TYPE);
            contract.announceOfferNumber = contractSummary.getString(CommManager.JSON_TENDER_ANN_OFFER_NUMBER);
            contract.offerDate = contractSummary.getString(CommManager.JSON_TENDER_OFFER_DATE);
            contract.finaliseContractType = contractSummary.getString(CommManager.JSON_TENDER_FINAL_CONTRACT_TYPE);
            contract.offerCriteria = contractSummary.getString(CommManager.JSON_TENDER_OFFER_CRITERIA);
            contract.nrOffers = contractSummary.getString(CommManager.JSON_TENDER_NUMBER_OFFERS);
            contract.subcontract = contractSummary.getString(CommManager.JSON_TENDER_SUBCONTRACT);
            contract.number = contractSummary.getString(CommManager.JSON_TENDER_NUMBER);
            contract.date = contractSummary.getString(CommManager.JSON_TENDER_DATE);
            contract.title = contractSummary.getString(CommManager.JSON_TENDER_TITLE);
            contract.value = contractSummary.getDouble(CommManager.JSON_TENDER_VALUE);
            contract.currency = contractSummary.getString(CommManager.JSON_TENDER_CURRENCY);
            contract.valueEUR = contractSummary.getDouble(CommManager.JSON_TENDER_VALUE_EUR);
            contract.valueRON = contractSummary.getDouble(CommManager.JSON_TENDER_VALUE_RON);
            contract.CPVCode = contractSummary.getString(CommManager.JSON_TENDER_CPV_CODE);
            contract.participationNumber = contractSummary.getString(CommManager.JSON_TENDER_PARTICIP_NUMBER);
            contract.participationDate = contractSummary.getString(CommManager.JSON_TENDER_PARTICIP_DATE);
            contract.participationEstimValue = contractSummary.getDouble(CommManager.JSON_TENDER_PARTICIP_ESTIM_VALUE);
            contract.participationCurrency = contractSummary.getString(CommManager.JSON_TENDER_PARTICIP_ESTIM_CURR);
            contract.deposit = contractSummary.getString(CommManager.JSON_TENDER_DEPOSITS);
            contract.finance = contractSummary.getString(CommManager.JSON_TENDER_FINANCE);
            contract.institutionID = contractSummary.getInt(CommManager.JSON_TENDER_INSTITUTION_ID);
            contract.companyID = contractSummary.getInt(CommManager.JSON_TENDER_COMPANY_ID);
            contract.votes = contractSummary.getInt(CommManager.JSON_CONTRACT_RED_FLAGS);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* Show the info received from the server */
        displayTender();

        /* Get PI and company info based on response */
        requestAdditionalInfo();
    }


    /* Receive Public Institution information from the server */
    private void receivePI(JSONArray response) {
        System.out.println("ContractFragment: receivePI " + response);

        try {
            JSONObject piSummary = response.getJSONObject(0);
            contract.pi = new PublicInstitution();
            contract.pi.id = contract.institutionID;
            contract.pi.name = piSummary.getString(CommManager.JSON_PI_NAME);

            displayPIButton();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /* Receive Company information from the server */
    private void receiveCompany(JSONArray response) {
        System.out.println("ContractFragment: receiveCompany " + response);

        try {
            JSONObject companySummary = response.getJSONObject(0);
            contract.company = new Company();
            contract.company.id = contract.companyID;
            contract.company.name = companySummary.getString(CommManager.JSON_COMPANY_NAME);
            contract.company.address = companySummary.getString(CommManager.JSON_COMPANY_ADDRESS);
            contract.company.CUI = companySummary.getString(CommManager.JSON_COMPANY_CUI);
            contract.company.type = Company.COMPANY_TYPE_ALL;

            displayCompanyButton();

        } catch (JSONException e) {
            e.printStackTrace();
        }
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

        Button button = (Button)originalView.findViewById(R.id.button_red_flag);
        if (button == null)
            return;

        button.setText("Semnalează (" + contract.votes + ")");
    }


    /* Add a new row in the table */
    private void addDynamicRow(String key1, String value1, String key2, String value2) {

        LinearLayout contractTable = (LinearLayout)originalView.findViewById(R.id.contractTable);
        if (contractTable == null)
            return;

        View row = inflater.inflate(R.layout.contract_row, (LinearLayout)null, false);
        if (row == null)
            return;

        contractTable.addView(row);

        TextView textT = ((TextView) row.findViewById(R.id.fieldTitle1));
        if (textT == null)
            return;

        textT.setText(key1);

        TextView textV = ((TextView) row.findViewById(R.id.fieldValue1));
        if (textV == null)
            return;

        textV.setText(value1);

        textT = ((TextView) row.findViewById(R.id.fieldTitle2));
        if (textT == null)
            return;

        textT.setText(key2);

        textV = ((TextView) row.findViewById(R.id.fieldValue2));
        if (textV == null)
            return;

        textV.setText(value2);

        this.rowNumber++;
    }


    /* Add dynamic text */
    private void addDynamicText(String title, String text) {
        LinearLayout contractTable = (LinearLayout)originalView.findViewById(R.id.contractTable);
        if (contractTable == null)
            return;

        View row = inflater.inflate(R.layout.contract_text, (LinearLayout)null, false);
        if (row == null)
            return;

        contractTable.addView(row);

        TextView textT = ((TextView) row.findViewById(R.id.titleContractText));
        if (textT == null)
            return;

        textT.setText(title);

        TextView textV = ((TextView) row.findViewById(R.id.valueContractText));
        if (textV == null)
            return;

        textV.setText(text);
    }


    /* Tender info */
    private void displayTender() {

        /* Tender includes AD information */
        displayAD();

        addDynamicRow("Tip Contract", contract.tenderContractType, "", "");
        addDynamicRow("Număr Anunț Participare", contract.participationNumber,
                        "Data Anunț Participare", contract.participationDate);
        addDynamicRow("Număr Anunț Atribuire", contract.announceOfferNumber,
                        "Data Anunț Atribuire", contract.offerDate);
        addDynamicRow("Tip Criterii Atribuire", contract.offerCriteria,
                        "Număr Oferte Primite", contract.nrOffers);
        addDynamicRow("Subcontractat", contract.subcontract.equals("") ?
                "-" : contract.subcontract,"Valoare Estimatată",
                contract.participationEstimValue +
                " " + contract.participationCurrency);

        addDynamicText("Depozite și garanții:", contract.deposit);
        addDynamicText("Modalități de finanțare:", contract.finance);
    }


    /* Display Company and Institution buttons */
    private void displayPIButton() {
        View piView = originalView.findViewById(R.id.piInContract);

        TextView piName = (TextView) piView.findViewById(R.id.institutionListName);
        piName.setText(contract.pi.name);
        piView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Click on pi " + contract.pi.name);

                Fragment piFragment = new InstitutionFragment();
                FragmentManager fragmentManager = fragmentCopy.getActivity().getSupportFragmentManager();

                /* Build Fragment Arguments */
                Bundle bundle = new Bundle();
                bundle.putInt(CommManager.BUNDLE_PI_ID, contract.pi.id);
                bundle.putString(CommManager.BUNDLE_PI_NAME, contract.pi.name);
                bundle.putInt(CommManager.BUNDLE_INST_TYPE,
                        InstitutionFragment.CONTRACT_LIST_FOR_PUBLIC_INSTITUTION);

                piFragment.setArguments(bundle);

                /* Got the Company Fragment */
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.add(parentID, piFragment)
                        .addToBackStack(piFragment.getClass().getName())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
            }
        });
    }


    /* Display Company and Institution buttons */
    private void displayCompanyButton() {
        View piView = originalView.findViewById(R.id.companyInContract);

        TextView companyName = (TextView) piView.findViewById(R.id.companyListName);
        companyName.setText(contract.company.name);
        piView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("Click on company " + contract.company.name);

                Fragment companyFragment = new InstitutionFragment();
                FragmentManager fragmentManager = fragmentCopy.getActivity().getSupportFragmentManager();

                /* Build Fragment Arguments */
                Bundle bundle = new Bundle();
                bundle.putInt(CommManager.BUNDLE_COMPANY_ID, contract.company.id);
                bundle.putInt(CommManager.BUNDLE_COMPANY_TYPE, contract.company.type);
                bundle.putString(CommManager.BUNDLE_COMPANY_NAME, contract.company.name);
                bundle.putInt(CommManager.BUNDLE_INST_TYPE, InstitutionFragment.CONTRACT_LIST_FOR_COMPANY);

                companyFragment.setArguments(bundle);

                /* Got the Company Fragment */
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.add(parentID, companyFragment)
                        .addToBackStack(companyFragment.getClass().getName())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
            }
        });
    }

    @Override
    public void onStop() {
        ((HomeActivity) getActivity()).setActionBarTitle(oldTitle);
        super.onStop();
    }
}
