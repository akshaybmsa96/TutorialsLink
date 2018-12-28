package tutorialslink.com.tutorialslinkwebview.global;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import java.net.URI;

public class UserSharedPreferenceData
{
    static final String PREF_LOGGEDIN_USER_ID = "logged_in_email";
    static final String PREF_USER_LOGGEDIN_STATUS = "logged_in_status";
    static final String PREF_LOGGEDIN_USER_NAME = "logged_in_username";
    static final String PREF_LOGGEDIN_With = "logged_in_with";
    static final String PREF_USER_PHN_STATUS = "phone_status";
    static final String PREF_LOGGEDIN_USER_PHN = "logged_in_user_phn";
    static final String PREF_LOGGEDIN_USER_PROF = "logged_in_user_profile";
    static final String PREF_LOGGEDIN_USER_PROF_BACK = "logged_in_user_profile_banner";



    public static SharedPreferences getSharedPreferences(Context ctx)
    {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setLoggedInUserName(Context ctx, String name)
    {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_LOGGEDIN_USER_NAME, name);
        editor.commit();
    }

    public static String getPrefLoggedinUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_LOGGEDIN_USER_NAME,"");
    }

    public static void setLoggedInUserPhn(Context ctx, String phn)
    {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_LOGGEDIN_USER_PHN, phn);
        editor.commit();
    }

    public static String getPrefLoggedinUserPhn(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_LOGGEDIN_USER_PHN, "");
    }



    public static void setLoggedInUserID(Context ctx, String email)
    {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_LOGGEDIN_USER_ID, email);
        editor.commit();
    }

    public static String getLoggedInUserID(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_LOGGEDIN_USER_ID, "");
    }

    public static void setUserLoggedInStatus(Context ctx, boolean status)
    {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(PREF_USER_LOGGEDIN_STATUS, status);
        editor.commit();
    }

    public static boolean getUserLoggedInStatus(Context ctx)
    {
        return getSharedPreferences(ctx).getBoolean(PREF_USER_LOGGEDIN_STATUS, false);
    }


    public static void setUserLoggedInWith(Context ctx, String loginWith)
    {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_LOGGEDIN_With, loginWith);
        editor.commit();
    }

    public static String getUserLoggedInWith(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_LOGGEDIN_With, "");
    }


    public static void setUserPhnStatus(Context ctx, boolean status) {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(PREF_USER_PHN_STATUS, status);
        editor.commit();
    }


    public static boolean getUserPhnStatus(Context ctx)
    {
        return getSharedPreferences(ctx).getBoolean(PREF_USER_PHN_STATUS, false);
    }



    public static void setPrefLoggedinUserProf(Context ctx, String uri) {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_LOGGEDIN_USER_PROF, uri);
        editor.commit();
    }


    public static String getPrefLoggedinUserProf(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_LOGGEDIN_USER_PROF,"");
    }


    public static void setPrefLoggedinUserProfBack(Context ctx, String uri) {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_LOGGEDIN_USER_PROF_BACK, uri);
        editor.commit();
    }


    public static String getPrefLoggedinUserProfBack(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_LOGGEDIN_USER_PROF_BACK,"");
    }



    public static void clearPref(Context ctx)
    {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.remove(PREF_LOGGEDIN_USER_ID);
        editor.remove(PREF_USER_LOGGEDIN_STATUS);
        editor.remove(PREF_LOGGEDIN_USER_NAME);
        editor.remove(PREF_LOGGEDIN_With);
        editor.remove(PREF_LOGGEDIN_USER_PHN);
        editor.remove(PREF_USER_PHN_STATUS);
        editor.remove(PREF_LOGGEDIN_USER_PROF);
        editor.remove(PREF_LOGGEDIN_USER_PROF_BACK);
        editor.commit();
    }
}
