package tutorialslink.com.tutorialslinkwebview.fragments;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tutorialslink.com.tutorialslinkwebview.R;
import tutorialslink.com.tutorialslinkwebview.global.UserSharedPreferenceData;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientBase;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientFollow;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientUpdateUserCover;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientUpdateUserImg;
import tutorialslink.com.tutorialslinkwebview.pojos.loginDetailspojo.LoginDetailPojo;
import tutorialslink.com.tutorialslinkwebview.util.FtpUploader;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfilePersonalFragment extends Fragment {

    private ImageView imageViewBackBanner,imageViewChangeBanner,imageViewChangePhoto;
    private CircleImageView imageViewProfilePicture;
    private static final int ID_CHANGE_BACK_BANNER = 1;
    private static final int ID_CHANGE_PHOTO = 2;
    private LoginDetailPojo loginDetailPojo;
    private TextInputEditText textInputLayoutName,textInputLayoutCity,textInputLayoutPostalCode,textInputLayoutAboutMe;
    private AutoCompleteTextView autoCompleteTextViewCountry,autoCompleteTextViewState;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private Button buttonNext,buttonSavePhotos;
    private OnCompleteListener1 mListener;
    private Boolean isUserPhotoChanged=false,isUserCoverChanged=false;
    private Uri picUri,coverUri;
    private Rect rect = new Rect(0, 0, 2000, 300);
    private ApiClientUpdateUserImg apiClientUpdateUserImg;
    private ApiClientUpdateUserCover apiClientUpdateUserCover;
    private String userPicName="",userCoverName="";


    public EditProfilePersonalFragment(LoginDetailPojo loginDetailPojo) {
        this.loginDetailPojo = loginDetailPojo;

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_edit_profile_personal, container, false);

        imageViewBackBanner=view.findViewById(R.id.imageViewBackBanner);
        imageViewChangeBanner=view.findViewById(R.id.imageViewChangeBanner);
        imageViewChangePhoto=view.findViewById(R.id.imageViewChangePhoto);
        imageViewProfilePicture=view.findViewById(R.id.imageViewProfilePicture);

        textInputLayoutName = view.findViewById(R.id.textInputLayoutName);
        textInputLayoutCity = view.findViewById(R.id.textInputLayoutCity);
        textInputLayoutPostalCode = view.findViewById(R.id.textInputLayoutPostalCode);
        textInputLayoutAboutMe = view.findViewById(R.id.textInputLayoutAboutMe);
        autoCompleteTextViewCountry = view.findViewById(R.id.autoCompleteTextViewCountry);
        autoCompleteTextViewState = view.findViewById(R.id.autoCompleteTextViewState);

        buttonNext = view.findViewById(R.id.buttonNext);
        buttonSavePhotos=view.findViewById(R.id.buttonSavePhotos);


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
                (getContext(), android.R.layout.simple_dropdown_item_1line, countries);

        autoCompleteTextViewCountry.setAdapter(adapter);
        autoCompleteTextViewCountry.setThreshold(1);


        imageViewChangeBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkAndRequestPermissions()) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, ID_CHANGE_BACK_BANNER);
                }

            }
        });

        imageViewChangePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkAndRequestPermissions()) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, ID_CHANGE_PHOTO);
                }

            }
        });

        setUI();

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mListener.onComplete1(textInputLayoutName.getText().toString(),autoCompleteTextViewCountry.getText().toString(),autoCompleteTextViewState.getText().toString(),textInputLayoutCity.getText().toString(),textInputLayoutPostalCode.getText().toString(),textInputLayoutAboutMe.getText().toString(),true);
            }
        });

        buttonSavePhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updatePics();

            }
        });

        textInputLayoutName.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                mListener.onComplete1(textInputLayoutName.getText().toString(),autoCompleteTextViewCountry.getText().toString(),autoCompleteTextViewState.getText().toString(),textInputLayoutCity.getText().toString(),textInputLayoutPostalCode.getText().toString(),textInputLayoutAboutMe.getText().toString(),false);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        autoCompleteTextViewCountry.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mListener.onComplete1(textInputLayoutName.getText().toString(),autoCompleteTextViewCountry.getText().toString(),autoCompleteTextViewState.getText().toString(),textInputLayoutCity.getText().toString(),textInputLayoutPostalCode.getText().toString(),textInputLayoutAboutMe.getText().toString(),false);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        autoCompleteTextViewState.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mListener.onComplete1(textInputLayoutName.getText().toString(),autoCompleteTextViewCountry.getText().toString(),autoCompleteTextViewState.getText().toString(),textInputLayoutCity.getText().toString(),textInputLayoutPostalCode.getText().toString(),textInputLayoutAboutMe.getText().toString(),false);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        textInputLayoutCity.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mListener.onComplete1(textInputLayoutName.getText().toString(),autoCompleteTextViewCountry.getText().toString(),autoCompleteTextViewState.getText().toString(),textInputLayoutCity.getText().toString(),textInputLayoutPostalCode.getText().toString(),textInputLayoutAboutMe.getText().toString(),false);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        textInputLayoutPostalCode.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mListener.onComplete1(textInputLayoutName.getText().toString(),autoCompleteTextViewCountry.getText().toString(),autoCompleteTextViewState.getText().toString(),textInputLayoutCity.getText().toString(),textInputLayoutPostalCode.getText().toString(),textInputLayoutAboutMe.getText().toString(),false);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        textInputLayoutAboutMe.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mListener.onComplete1(textInputLayoutName.getText().toString(),autoCompleteTextViewCountry.getText().toString(),autoCompleteTextViewState.getText().toString(),textInputLayoutCity.getText().toString(),textInputLayoutPostalCode.getText().toString(),textInputLayoutAboutMe.getText().toString(),false);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



    //    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    //    startActivityForResult(takePicture, 0);

//        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
//                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(pickPhoto , 1);

        checkAndRequestPermissions();
        return view;
    }

    private void updatePics() {

        if(isUserPhotoChanged)
        {
            //update user picture

                  //  /*
                    int SDK_INT = android.os.Build.VERSION.SDK_INT;
                    if (SDK_INT > 8)
                    {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                .permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        //your codes here

                        FtpUploader ftpUploader;

                        final ProgressDialog pDialog = new ProgressDialog(getContext());

                        try {

                            pDialog.setMessage("Uploading cover pic");
                            pDialog.setCancelable(false);
                            pDialog.show();


                            Long tsLong = System.currentTimeMillis()/1000;
                            String ts = tsLong.toString();
                            ftpUploader = new FtpUploader("waws-prod-pn1-001.ftp.azurewebsites.windows.net", "tutorialslink\\$tutorialslink", "NwrJjKks46q5mNTFH0jgoGZLrHNmPzbXFwwwqibaGsrHcdn6eRdXwaeXKjgA",getContext());
                            ftpUploader.uploadFile(getRealFilePath(getContext(),picUri), UserSharedPreferenceData.getLoggedInUserID(getContext())+ts+"user_pic.png", "/site/wwwroot/Article_img/User_pic/");
                            userPicName=UserSharedPreferenceData.getLoggedInUserID(getContext())+ts+"user_pic.png";
                            ftpUploader.disconnect();
                  //          System.out.println("Done");
                                updatePicInDb();

                            pDialog.dismiss();

                        }

                        catch (Exception e)
                        {
                            e.printStackTrace();
                            Toast.makeText(getContext(),"User Profile Photo Upload Failed",Toast.LENGTH_LONG).show();
                            pDialog.dismiss();
                        }

                    }
            //        */

        }

        if(isUserCoverChanged)
        {
            //update cover
                //    /*
                    int SDK_INT = android.os.Build.VERSION.SDK_INT;
                    if (SDK_INT > 8)
                    {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                .permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        //your codes here


                        FtpUploader ftpUploader;
                        final ProgressDialog pDialog = new ProgressDialog(getContext());

                        try {

                            pDialog.setMessage("Uploading cover pic");
                            pDialog.setCancelable(false);
                            pDialog.show();

                            Long tsLong = System.currentTimeMillis()/1000;
                            String ts = tsLong.toString();
                            ftpUploader = new FtpUploader("waws-prod-pn1-001.ftp.azurewebsites.windows.net", "tutorialslink\\$tutorialslink", "NwrJjKks46q5mNTFH0jgoGZLrHNmPzbXFwwwqibaGsrHcdn6eRdXwaeXKjgA",getContext());
                            ftpUploader.uploadFile(getRealFilePath(getContext(),coverUri), UserSharedPreferenceData.getLoggedInUserID(getContext())+ts+"back_img.png", "/site/wwwroot/Article_img/User_back/");
                            ftpUploader.disconnect();
                         // System.out.println("Done");
                            userCoverName=UserSharedPreferenceData.getLoggedInUserID(getContext())+ts+"back_img.png";
                           updateCoverInDb();

                            pDialog.dismiss();


                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getContext()," User Cover Photo Upload Failed",Toast.LENGTH_LONG).show();
                            pDialog.dismiss();
                        }

                    }
        //            */

        }
    }

    private void updateCoverInDb() {

        final ProgressDialog pDialog = new ProgressDialog(getContext());


        pDialog.setMessage("Saving cover pic");
        pDialog.setCancelable(false);
        pDialog.show();


        String url = "Author_Backimg_Update?email="+UserSharedPreferenceData.getLoggedInUserID(getContext())+"&imagname="+userCoverName;
        apiClientUpdateUserCover = ApiClientBase.getApiClient().create(ApiClientUpdateUserCover.class);
        Call<String> call= apiClientUpdateUserCover.updateUserCover(url);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {


                String res =response.body();

                //         Toast.makeText(AuthorProfileActivity.this,""+res,Toast.LENGTH_SHORT).show();

                if(res!=null&&res.equals("1"))
                {

                    buttonSavePhotos.setVisibility(View.GONE);
                    Toast.makeText(getActivity(),"user cover update",Toast.LENGTH_SHORT).show();
                    UserSharedPreferenceData.setPrefLoggedinUserProfBack(getContext(),"/Article_img/User_back/"+userCoverName);
                    isUserCoverChanged=false;

                }

                else
                {
                    buttonSavePhotos.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext()," Res :  "+res,Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(),"user cover cannot update",Toast.LENGTH_SHORT).show();
                }

                pDialog.dismiss();


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {


                  Toast.makeText(getActivity(),"Something went wrong cannot update cover",Toast.LENGTH_SHORT).show();

                  buttonSavePhotos.setVisibility(View.VISIBLE);

                pDialog.dismiss();

            }
        });

    }



    private void updatePicInDb() {

        final ProgressDialog pDialog = new ProgressDialog(getContext());


        pDialog.setMessage("Saving profile pic....");
        pDialog.setCancelable(false);
        pDialog.show();


        String url = "Author_Picture_Update?email="+UserSharedPreferenceData.getLoggedInUserID(getContext())+"&imagname="+userPicName;
        apiClientUpdateUserImg = ApiClientBase.getApiClient().create(ApiClientUpdateUserImg.class);
        Call<String> call= apiClientUpdateUserImg.updateUserImg(url);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {


                String res =response.body();

                //         Toast.makeText(AuthorProfileActivity.this,""+res,Toast.LENGTH_SHORT).show();

                if(res!=null&&res.equals("1"))
                {

                    buttonSavePhotos.setVisibility(View.GONE);
                    Toast.makeText(getActivity(),"user pic update",Toast.LENGTH_SHORT).show();
                    UserSharedPreferenceData.setPrefLoggedinUserProf(getContext(),"/Article_img/User_pic/"+userPicName);
                    isUserPhotoChanged=false;

                }
                else
                {
                    buttonSavePhotos.setVisibility(View.VISIBLE);
               //     Toast.makeText(getContext()," Res :  "+res,Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(),"user pic cannot update",Toast.LENGTH_SHORT).show();
                }

                pDialog.dismiss();


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {


                Toast.makeText(getActivity(),"Something went wrong cannot update pic",Toast.LENGTH_SHORT).show();

                buttonSavePhotos.setVisibility(View.VISIBLE);

                pDialog.dismiss();

            }
        });


    }

    private void setUI() {

        if(!loginDetailPojo.getTable().get(0).getPicture().equals("")&&loginDetailPojo.getTable().get(0).getPicture()!=null) {
            if (loginDetailPojo.getTable().get(0).getPicture().startsWith("/")) {
                Picasso.with(getContext()).load("https://tutorialslink.com" + loginDetailPojo.getTable().get(0).getPicture()).into(imageViewProfilePicture);
            } else {
                Picasso.with(getContext()).load(loginDetailPojo.getTable().get(0).getPicture()).into(imageViewProfilePicture);
            }
        }

        if(loginDetailPojo.getTable().get(0).getBack_img()!=null&&!loginDetailPojo.getTable().get(0).getBack_img().equals("0")&&!loginDetailPojo.getTable().get(0).getBack_img().equals(""))
        {
            Picasso.with(getContext()).load("https://tutorialslink.com" + loginDetailPojo.getTable().get(0).getBack_img()).into(imageViewBackBanner);
        }


        textInputLayoutName.setText(loginDetailPojo.getTable().get(0).getFirst_name()+" " +loginDetailPojo.getTable().get(0).getLast_name());

        if(!loginDetailPojo.getTable().get(0).getCountry().equals("")&&loginDetailPojo.getTable().get(0).getCountry()!=null) {
            autoCompleteTextViewCountry.setText(loginDetailPojo.getTable().get(0).getCountry());
        }

        if(!loginDetailPojo.getTable().get(0).getCity().equals("")&&loginDetailPojo.getTable().get(0).getCity()!=null) {
            textInputLayoutCity.setText(loginDetailPojo.getTable().get(0).getCity());
        }

        if(!loginDetailPojo.getTable().get(0).getState().equals("")&&loginDetailPojo.getTable().get(0).getState()!=null) {
            autoCompleteTextViewState.setText(loginDetailPojo.getTable().get(0).getState());
        }

        if(!loginDetailPojo.getTable().get(0).getPostal_Code().equals("")&&loginDetailPojo.getTable().get(0).getPostal_Code()!=null) {
            textInputLayoutPostalCode.setText(loginDetailPojo.getTable().get(0).getPostal_Code());
        }


        if(!loginDetailPojo.getTable().get(0).getAbout_us().equals("")) {
            textInputLayoutAboutMe.setText(loginDetailPojo.getTable().get(0).getAbout_us());
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {

            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE :
                CropImage.ActivityResult result = CropImage.getActivityResult(imageReturnedIntent);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    imageViewBackBanner.setImageURI(resultUri);
                    isUserCoverChanged=true;
                    coverUri=resultUri;

                    buttonSavePhotos.setVisibility(View.VISIBLE);

              //      mListener.onComplete1(textInputLayoutName.getText().toString(),autoCompleteTextViewCountry.getText().toString(),autoCompleteTextViewState.getText().toString(),textInputLayoutCity.getText().toString(),textInputLayoutPostalCode.getText().toString(),textInputLayoutAboutMe.getText().toString(),false);

                }

                else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            break;


            case ID_CHANGE_BACK_BANNER:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                  //  imageViewBackBanner.setImageURI(selectedImage);

                    CropImage.activity(selectedImage).setInitialCropWindowRectangle(rect)
                            .start(getContext(), this);


                //    System.out.println("URi is     :  "+getRealFilePath(getContext(),selectedImage));
                //    Toast.makeText(getContext(),getRealFilePath(getContext(),selectedImage),Toast.LENGTH_LONG).show();


                    //update cover
                    /*
                    int SDK_INT = android.os.Build.VERSION.SDK_INT;
                    if (SDK_INT > 8)
                    {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                .permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        //your codes here


                        FtpUploader ftpUploader;

                        try {
                            Long tsLong = System.currentTimeMillis()/1000;
                            String ts = tsLong.toString();
                            ftpUploader = new FtpUploader("waws-prod-pn1-001.ftp.azurewebsites.windows.net", "tutorialslink\\$tutorialslink", "NwrJjKks46q5mNTFH0jgoGZLrHNmPzbXFwwwqibaGsrHcdn6eRdXwaeXKjgA",getContext());
                            ftpUploader.uploadFile(getRealFilePath(getContext(),selectedImage), UserSharedPreferenceData.getLoggedInUserID(getContext())+ts+"back_img.png", "/site/wwwroot/Article_img/User_back/");
                            ftpUploader.disconnect();
                         // System.out.println("Done");


                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getContext()," User Cover Photo Upload Failed",Toast.LENGTH_LONG).show();
                        }

                    }
                    */

                }

                break;


            case ID_CHANGE_PHOTO:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    imageViewProfilePicture.setImageURI(selectedImage);

                    isUserPhotoChanged=true;
                    picUri=selectedImage;
                  //  mListener.onComplete1(textInputLayoutName.getText().toString(),autoCompleteTextViewCountry.getText().toString(),autoCompleteTextViewState.getText().toString(),textInputLayoutCity.getText().toString(),textInputLayoutPostalCode.getText().toString(),textInputLayoutAboutMe.getText().toString(),false);
                    buttonSavePhotos.setVisibility(View.VISIBLE);
                    //update user picture

                    /*
                    int SDK_INT = android.os.Build.VERSION.SDK_INT;
                    if (SDK_INT > 8)
                    {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                .permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        //your codes here


                        FtpUploader ftpUploader;

                        try {
                            Long tsLong = System.currentTimeMillis()/1000;
                            String ts = tsLong.toString();
                            ftpUploader = new FtpUploader("waws-prod-pn1-001.ftp.azurewebsites.windows.net", "tutorialslink\\$tutorialslink", "NwrJjKks46q5mNTFH0jgoGZLrHNmPzbXFwwwqibaGsrHcdn6eRdXwaeXKjgA",getContext());
                            ftpUploader.uploadFile(getRealFilePath(getContext(),selectedImage), UserSharedPreferenceData.getLoggedInUserID(getContext())+ts+"user_pic.png", "/site/wwwroot/Article_img/User_pic/");
                            ftpUploader.disconnect();
                  //          System.out.println("Done");


                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(),"User Profile Photo Upload Failed",Toast.LENGTH_LONG).show();
                        }

                    }
                    */
                }
                break;
        }
    }

    public static String getRealFilePath(final Context context, final Uri uri )
    {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if
                ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {

            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {

                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        //   Toast.makeText(getContext(), "Hello", Toast.LENGTH_SHORT).show();

        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the noresult arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted
                    //      Toast.makeText(getContext(), "Permission granted", Toast.LENGTH_SHORT).show();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getContext(), "Permission Required in order to change Picture", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            // other case lines to check for other
        }
    }

    private boolean checkAndRequestPermissions() {

        int permissionSendMessage = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();


        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    public static interface OnCompleteListener1
    {
        public abstract void onComplete1(String name,String country,String state,String city,String postalCode,String aboutMe,Boolean next);
    }


    public void onAttach(Context context) {
        super.onAttach(context);
        try {
                this.mListener = (OnCompleteListener1)context;

        }
        catch (final ClassCastException e) {
            throw new ClassCastException(getTargetFragment().toString() + " must implement OnCompleteListener");
        }
    }



}
