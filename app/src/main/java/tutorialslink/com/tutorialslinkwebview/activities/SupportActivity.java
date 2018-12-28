package tutorialslink.com.tutorialslinkwebview.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tutorialslink.com.tutorialslinkwebview.R;
import tutorialslink.com.tutorialslinkwebview.global.UserSharedPreferenceData;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientBase;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientGetLoginDetails;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientSupport;
import tutorialslink.com.tutorialslinkwebview.pojos.loginDetailspojo.LoginDetailPojo;
import tutorialslink.com.tutorialslinkwebview.util.NetworkCheck;

public class SupportActivity extends AppCompatActivity {

    private Toolbar tb;
    private AutoCompleteTextView autoCompleteTextViewCountry,autoCompleteTextViewState;
    private TextView textViewReportBug;
    private LoginDetailPojo loginDetailPojo;
    private RelativeLayout layout;
    private ApiClientGetLoginDetails apiClientGetLoginDetails;
    private ApiClientSupport apiClientSupport;
    private Button buttonSubmit;
    private TextInputEditText textInputEditTextName,textInputEditTextEmail,textInputEditTextPhone,textInputEditTextCity,textInputEditTextSubject,textInputEditTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        autoCompleteTextViewCountry= findViewById(R.id.autoCompleteTextViewCountry);
        autoCompleteTextViewState= findViewById(R.id.autoCompleteTextViewState);

        textInputEditTextName= findViewById(R.id.textInputEditTextName);
        textInputEditTextEmail= findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPhone= findViewById(R.id.textInputEditTextPhone);
        textInputEditTextCity= findViewById(R.id.textInputEditTextCity);
        textInputEditTextSubject= findViewById(R.id.textInputEditTextSubject);
        textInputEditTextMessage= findViewById(R.id.textInputEditTextMessage);


        buttonSubmit = findViewById(R.id.buttonSubmit);

        layout = findViewById(R.id.layout);
        layout.setVisibility(View.GONE);


        String[] countries = new String[]{"Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra", "Angola",
                "Anguilla", "Antarctica", "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria",
                "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bermuda",
                "Bhutan", "Bolivia", "Bosnia and Herzegowina", "Botswana", "Bouvet Island", "Brazil", "British Indian Ocean Territory",
                "Brunei Darussalam", "Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde",
                "Cayman Islands", "Central African Republic", "Chad", "Chile", "China", "Christmas Island", "Cocos (Keeling) Islands",
                "Colombia", "Comoros", "Congo", "Congo, the Democratic Republic of the", "Cook Islands", "Costa Rica", "Cote d'Ivoire",
                "Croatia (Hrvatska)", "Cuba", "Cyprus", "Czech Republic", "Denmark", "Djibouti", "Dominica", "Dominican Republic",
                "East Timor", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia",
                "Falkland Islands (Malvinas)", "Faroe Islands", "Fiji", "Finland", "France", "France Metropolitan", "French Guiana",
                "French Polynesia", "French Southern Territories", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Gibraltar",
                "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti",
                "Heard and Mc Donald Islands", "Holy See (Vatican City State)", "Honduras", "Hong Kong", "Hungary", "Iceland", "India",
                "Indonesia", "Iran (Islamic Republic of)", "Iraq", "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan",
                "Kazakhstan", "Kenya", "Kiribati", "Korea, Democratic People's Republic of", "Korea, Republic of", "Kuwait",
                "Kyrgyzstan", "Lao, People's Democratic Republic", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libyan Arab Jamahiriya",
                "Liechtenstein", "Lithuania", "Luxembourg", "Macau", "Macedonia, The Former Yugoslav Republic of", "Madagascar",
                "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Martinique", "Mauritania", "Mauritius",
                "Mayotte", "Mexico", "Micronesia, Federated States of", "Moldova, Republic of", "Monaco", "Mongolia", "Montserrat",
                "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands", "Netherlands Antilles", "New Caledonia",
                "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island", "Northern Mariana Islands", "Norway", "Oman",
                "Pakistan", "Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn", "Poland", "Portugal",
                "Puerto Rico", "Qatar", "Reunion", "Romania", "Russian Federation", "Rwanda", "Saint Kitts and Nevis", "Saint Lucia",
                "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia", "Senegal",
                "Seychelles", "Sierra Leone", "Singapore", "Slovakia (Slovak Republic)", "Slovenia", "Solomon Islands", "Somalia",
                "South Africa", "South Georgia and the South Sandwich Islands", "Spain", "Sri Lanka", "St. Helena",
                "St. Pierre and Miquelon", "Sudan", "Suriname", "Svalbard and Jan Mayen Islands", "Swaziland", "Sweden",
                "Switzerland", "Syrian Arab Republic", "Taiwan, Province of China", "Tajikistan", "Tanzania, United Republic of",
                "Thailand", "Togo", "Tokelau", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan",
                "Turks and Caicos Islands", "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom",
                "United States", "United States Minor Outlying Islands", "Uruguay", "Uzbekistan", "Vanuatu", "Venezuela",
                "Vietnam", "Virgin Islands (British)", "Virgin Islands (U.S.)", "Wallis and Futuna Islands", "Western Sahara",
                "Yemen", "Yugoslavia", "Zambia", "Zimbabwe"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_dropdown_item_1line, countries);

        autoCompleteTextViewCountry.setAdapter(adapter);
        autoCompleteTextViewCountry.setThreshold(1);

        tb = (Toolbar) findViewById(R.id.toolbar);
        textViewReportBug=findViewById(R.id.textViewReportBug);
        setSupportActionBar(tb);

        tb.setNavigationIcon(R.mipmap.ic_back);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle("Support");
        tb.setTitleTextColor(getResources().getColor(R.color.colorPrimary));

        textViewReportBug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SupportActivity.this,ReportActivity.class));
            }
        });

        if(NetworkCheck.isNetworkAvailable(this))
        {
            getUserDetails();
        }

        else {

            Snackbar.make(layout, "Network Unavailable", Snackbar.LENGTH_INDEFINITE).
                    setAction("Retry", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            //    Toast.makeText(DashboardActivity.this,"Snackbar",Toast.LENGTH_SHORT).show();
                            //    finish();
                            //    startActivity(getIntent());

                            recreate();


                        }
                    }).show();
        }

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validate())
                {
                    if(NetworkCheck.isNetworkAvailable(SupportActivity.this))
                    {
                        support();
                    }

                    else {

                        Snackbar.make(layout, "Network Unavailable", Snackbar.LENGTH_INDEFINITE).
                                setAction("Retry", new View.OnClickListener() {

                                    @Override
                                    public void onClick(View view) {
                                        //    Toast.makeText(DashboardActivity.this,"Snackbar",Toast.LENGTH_SHORT).show();
                                        //    finish();
                                        //    startActivity(getIntent());

                                        recreate();


                                    }
                                }).show();
                    }
                }

            }
        });

        textInputEditTextSubject.requestFocus();
    }

    private boolean validate() {

        if(textInputEditTextName.getText().toString().equals(""))
        {
            textInputEditTextName.setError("Enter Name");
            return false;
        }

        if(textInputEditTextEmail.getText().toString().equals(""))
        {
            textInputEditTextEmail.setError("Enter Email");
            return false;
        }

        if(textInputEditTextPhone.getText().toString().equals(""))
        {
            textInputEditTextPhone.setError("Enter Phone");
            return false;
        }

        if(autoCompleteTextViewCountry.getText().toString().equals(""))
        {
            autoCompleteTextViewCountry.setError("Enter Country");
            return false;
        }

        if(autoCompleteTextViewState.getText().toString().equals(""))
        {
            autoCompleteTextViewState.setError("Enter State");
            return false;
        }

        if(textInputEditTextCity.getText().toString().equals(""))
        {
            textInputEditTextCity.setError("Enter City");
            return false;
        }

        if(textInputEditTextSubject.getText().toString().equals(""))
        {
            textInputEditTextSubject.setError("Enter Subject");
            return false;
        }

        if(textInputEditTextMessage.getText().toString().equals(""))
        {
            textInputEditTextMessage.setError("Enter Message");
            return false;
        }

        return true;
    }

    private void support() {

        final ProgressDialog pDialog = new ProgressDialog(this);

        pDialog.setMessage("Please wait");
        pDialog.setCancelable(false);
        pDialog.show();

        // show it
        apiClientSupport = ApiClientBase.getApiClient().create(ApiClientSupport.class);

        String url = "UserEmail?email=" + UserSharedPreferenceData.getLoggedInUserID(this);

        //    System.out.println("url               : " + url);

        Call<String> call = apiClientSupport.support(textInputEditTextName.getText().toString(),textInputEditTextEmail.getText().toString(),textInputEditTextPhone.getText().toString(),autoCompleteTextViewCountry.getText().toString(),autoCompleteTextViewState.getText().toString(),textInputEditTextCity.getText().toString(),textInputEditTextSubject.getText().toString(),textInputEditTextMessage.getText().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String res  = response.body();

                //      Toast.makeText(getApplicationContext(),loginDetailPojo.getTable().toString()+"", Toast.LENGTH_SHORT).show();

                if (res != null) {
                    if (res.equals("0"))
                    {
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();

                    }

                    else {
                     //   Toast.makeText(getApplicationContext(), "1 and success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SupportActivity.this,ThankyouActivity.class);
                        intent.putExtra("data","1");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }
                }

                pDialog.dismiss();


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                pDialog.dismiss();


                // if(skip==0)
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                System.out.println("failure" + "+ : " + t.getMessage());
                System.out.println("failure" + "+ : " + t.getCause());
                System.out.println("failure" + "+ : " + t.toString());
            }
        });



    }


    private void getUserDetails()
    {

        final ProgressDialog pDialog = new ProgressDialog(this);

        pDialog.setMessage("Fetching Profile");
        pDialog.setCancelable(false);
        pDialog.show();

        // show it
        apiClientGetLoginDetails = ApiClientBase.getApiClient().create(ApiClientGetLoginDetails.class);

        String url = "UserEmail?email=" + UserSharedPreferenceData.getLoggedInUserID(this);

    //    System.out.println("url               : " + url);

        Call<LoginDetailPojo> call = apiClientGetLoginDetails.getLoginDetails(url);
        call.enqueue(new Callback<LoginDetailPojo>() {
            @Override
            public void onResponse(Call<LoginDetailPojo> call, Response<LoginDetailPojo> response) {

                loginDetailPojo = response.body();

                //      Toast.makeText(getApplicationContext(),loginDetailPojo.getTable().toString()+"", Toast.LENGTH_SHORT).show();

                if (loginDetailPojo != null) {
                    if (loginDetailPojo.getTable().size() > 0) {
                        layout.setVisibility(View.VISIBLE);
                        updateUI();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();

                }

                pDialog.dismiss();


            }

            @Override
            public void onFailure(Call<LoginDetailPojo> call, Throwable t) {

                pDialog.dismiss();


                // if(skip==0)
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                System.out.println("failure" + "+ : " + t.getMessage());
                System.out.println("failure" + "+ : " + t.getCause());
                System.out.println("failure" + "+ : " + t.toString());
            }
        });
    }

    private void updateUI() {



        textInputEditTextName.setText(loginDetailPojo.getTable().get(0).getFirst_name()+" " +loginDetailPojo.getTable().get(0).getLast_name());

        if(!loginDetailPojo.getTable().get(0).getCountry().equals("")&&loginDetailPojo.getTable().get(0).getCountry()!=null) {
            autoCompleteTextViewCountry.setText(loginDetailPojo.getTable().get(0).getCountry());
        }

        if(loginDetailPojo.getTable().get(0).getEmail().contains("@")&&loginDetailPojo.getTable().get(0).getEmail().contains("."))
        {
            textInputEditTextEmail.setText(loginDetailPojo.getTable().get(0).getEmail());
        }

        if(!loginDetailPojo.getTable().get(0).getCity().equals("")&&loginDetailPojo.getTable().get(0).getCity()!=null) {
            textInputEditTextCity.setText(loginDetailPojo.getTable().get(0).getCity());
        }

        if(!loginDetailPojo.getTable().get(0).getState().equals("")&&loginDetailPojo.getTable().get(0).getState()!=null) {
            autoCompleteTextViewState.setText(loginDetailPojo.getTable().get(0).getState());
        }

        if(loginDetailPojo.getTable().get(0).getMobile_number()!=null&&!loginDetailPojo.getTable().get(0).getMobile_number().equals(""))
        {
            textInputEditTextPhone.setText(loginDetailPojo.getTable().get(0).getMobile_number());
        }

        if(loginDetailPojo.getTable().get(0).getMobile_number()!=null&&!loginDetailPojo.getTable().get(0).getMobile_number().equals(""))
        {
            textInputEditTextPhone.setText(loginDetailPojo.getTable().get(0).getMobile_number());
        }

        if(loginDetailPojo.getTable().get(0).getMobile_number()!=null&&!loginDetailPojo.getTable().get(0).getMobile_number().equals(""))
        {
            textInputEditTextPhone.setText(loginDetailPojo.getTable().get(0).getMobile_number());
        }


    }
}
