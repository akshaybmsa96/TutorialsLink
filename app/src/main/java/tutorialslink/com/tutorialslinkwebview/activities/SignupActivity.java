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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import io.codetail.widget.RevealLinearLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tutorialslink.com.tutorialslinkwebview.R;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientBase;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientCheckEmail;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientUserEmailLogin;
import tutorialslink.com.tutorialslinkwebview.pojos.loginDetailspojo.TablePojo;
import tutorialslink.com.tutorialslinkwebview.util.NetworkCheck;

public class SignupActivity extends AppCompatActivity {
    private Toolbar tb;
    private ApiClientCheckEmail apiClientCheckEmail;
    private TextInputEditText textInputLayoutName,textInputLayoutEmail,textInputLayoutPostalCode,textInputLayoutCity;
    private AutoCompleteTextView autoCompleteTextViewCountry,autoCompleteTextViewState;
    private Button buttonContinue;
    private LinearLayout layout;
    private TablePojo userDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        tb.setNavigationIcon(R.mipmap.ic_back);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle("SignUp");
        tb.setTitleTextColor(getResources().getColor(R.color.colorPrimary));


        textInputLayoutName = findViewById(R.id.textInputLayoutName);
        textInputLayoutCity = findViewById(R.id.textInputLayoutCity);
        autoCompleteTextViewState = findViewById(R.id.autoCompleteTextViewState);
        autoCompleteTextViewCountry = findViewById(R.id.autoCompleteTextViewCountry);
        textInputLayoutPostalCode = findViewById(R.id.textInputLayoutPostalCode);
        textInputLayoutEmail = findViewById(R.id.textInputLayoutEmail);
        buttonContinue = findViewById(R.id.buttonContinue);

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



        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate())
                {
                    if(NetworkCheck.isNetworkAvailable(SignupActivity.this))
                    {
                        check();
                    }

                    else {

                        Snackbar.make(layout,"Network Unavailable",Snackbar.LENGTH_LONG).show();

                    }
                }
            }
        });


    }

    private void check() {


        final ProgressDialog pDialog = new ProgressDialog(this);


        pDialog.setMessage("Verifying");
        pDialog.setCancelable(false);
        pDialog.show();

        // show it
        apiClientCheckEmail = ApiClientBase.getApiClient().create(ApiClientCheckEmail.class);

        String url = "CheckEmail?email="+textInputLayoutEmail.getText().toString();

        Call<String> call= apiClientCheckEmail.check(url);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String output =response.body();

                //    Toast.makeText(getApplicationContext(),output+"", Toast.LENGTH_SHORT).show();

                //   Toast.makeText(getContext(),purchaseReportPojo.toString(),Toast.LENGTH_SHORT).show();
                if(output!=null) {

                    if(output.equals("1")) {
                        //   Toast.makeText(getApplicationContext(),"Login Success", Toast.LENGTH_SHORT).show();
                        //    getLoginDetails(editTextEmail.getText().toString());

                        Toast.makeText(getApplicationContext(),"Username already Exists", Toast.LENGTH_SHORT).show();


                    }

                    else if(output.equals("0"))
                    {
                    //    Snackbar.make(findViewById(R.id.relativelayoutView),"Already", Snackbar.LENGTH_LONG).show();

                        assign();
                        String data = new Gson().toJson(userDetail);
                   //     Toast.makeText(getApplicationContext(),data, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignupActivity.this,CreatePasswordActivity.class);
                        intent.putExtra("data",data);
                        startActivity(intent);
                    }


                }

                else

                {
                  //  Toast.makeText(getApplicationContext(),"New", Toast.LENGTH_SHORT).show();

                }

                pDialog.dismiss();


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                pDialog.dismiss();


                // if(skip==0)
                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                System.out.println("failure"+"+ : "+t.getMessage());
                System.out.println("failure"+"+ : "+t.getCause());
                System.out.println("failure"+"+ : "+t.toString());
            }
        });


    }

    private void assign()
    {

        userDetail = new TablePojo();

        userDetail.setFirst_name(textInputLayoutName.getText().toString());
        userDetail.setEmail(textInputLayoutEmail.getText().toString());
        userDetail.setCountry(autoCompleteTextViewCountry.getText().toString());
        userDetail.setState(autoCompleteTextViewState.getText().toString());
        userDetail.setCity(textInputLayoutCity.getText().toString());
        userDetail.setPostal_Code(textInputLayoutPostalCode.getText().toString());



    }

    private boolean validate()
    {

        if(textInputLayoutName.getText().toString().equals(""))
        {
            textInputLayoutName.setError("Enter Name");
            textInputLayoutName.requestFocus();

            return false;
        }

        else if(textInputLayoutEmail.getText().toString().equals(""))
        {
            textInputLayoutEmail.setError("Enter Email");
            textInputLayoutEmail.requestFocus();

            return false;
        }

        else if(!textInputLayoutEmail.getText().toString().contains("@")||!textInputLayoutEmail.getText().toString().contains("."))
        {
            textInputLayoutEmail.setError("Invalid Email");
            textInputLayoutEmail.requestFocus();

            return false;
        }

        else if(autoCompleteTextViewCountry.getText().toString().equals(""))
        {

            autoCompleteTextViewCountry.setError("Enter Country");
            autoCompleteTextViewCountry.requestFocus();
            return false;
        }

        else if(autoCompleteTextViewState.getText().toString().equals(""))
        {
            autoCompleteTextViewState.setError("Enter State");
            autoCompleteTextViewState.requestFocus();

            return false;
        }

        else if(textInputLayoutCity.getText().toString().equals(""))
        {

            textInputLayoutCity.setError("Enter City");
            textInputLayoutCity.requestFocus();

            return false;
        }

        else if(textInputLayoutPostalCode.getText().toString().equals(""))
        {
            textInputLayoutPostalCode.setError("Enter Postal Code");
            textInputLayoutPostalCode.requestFocus();
            return false;
        }

        return true;
    }
}
