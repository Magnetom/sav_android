package odyssey.projects.pref;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import odyssey.projects.sav.driver.Settings;

/**
 * Created by Odyssey on 25.04.2017.
 */

public class LocalSettings {

    // Текущее транспортное средство.
    public static final String SP_VEHICLE           = "vehicle";
    public static final String SP_SERVER_ADDRESS    = "server_address";
    public static final String SP_USE_SSID_FILTER   = "use_ssid_filter";
    public static final String SP_ALLOWED_WIFI_SSID = "pref_wifi_ssid";
    public static final String SP_NOT_FIRST_JOIN    = "not_first_join";
    public static final String SP_ALL_DB_REMOVE     = "all_db_remove";

    private static final String APP_DEFAULT_PREFERENCES = "AppSettings";

    private static LocalSettings instance;
    private SharedPreferences sPref;

    private LocalSettings(Context context, String prefName){
        sPref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
    }

    private LocalSettings(Context context){
        //sPref = context.getSharedPreferences(APP_DEFAULT_PREFERENCES, Context.MODE_PRIVATE);
        sPref = PreferenceManager.getDefaultSharedPreferences(context);
        // Настройки, которые будут применены при первом запуске.
        firstJoin();
    }

    public static LocalSettings getInstance(Context context){
        if (instance == null) return instance = new LocalSettings(context);
        return instance;
    }

    private void firstJoin(){
        if (!getBoolean(SP_NOT_FIRST_JOIN)){
            saveBoolean(SP_NOT_FIRST_JOIN, true);

            // Применяем настройки по-умолчанию (при первом запуске програмы).
            saveBoolean(SP_USE_SSID_FILTER, true); // Использовать SSID-фильтрацию.
            saveText(SP_ALLOWED_WIFI_SSID, Settings.ALLOWED_WIFI_DEFAULT_SSID); // Одобренный SSID по-умолчанию.
            saveText(SP_SERVER_ADDRESS,    Settings.DB_SERVER_DEFAULT_ADDRESS); // Адрес/имя удаленного сервера.
        }
    }

    public void saveText (String key, String value){

        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void saveInt (String key, int value){
        SharedPreferences.Editor editor = sPref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public String getText (String key){
        return sPref.getString(key, "");
    }

    public long getLong (String key){
        return sPref.getLong(key, 0);
    }
    public int  getInt  (String key){
        return sPref.getInt (key, 0);
    }

    public void clearText (String key){
        SharedPreferences.Editor editor = sPref.edit();
        editor.remove(key);
        editor.apply();
    }

    public boolean getBoolean(String key){
        return sPref.getBoolean(key, false);
    }

    public void saveBoolean(String key, boolean value){
        SharedPreferences.Editor editor = sPref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public SharedPreferences getSharedPrefInstance(){
        return sPref;
    }

    public String getScriptUrl(String script_name){
        return Settings.MAIN_PROTOCOL + getText(SP_SERVER_ADDRESS) + Settings.WORK_DIRECTORY + script_name;
    }
}
