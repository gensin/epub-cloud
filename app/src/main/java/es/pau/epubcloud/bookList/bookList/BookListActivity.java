package es.pau.epubcloud.bookList.bookList;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;

import es.pau.epubcloud.R;

public class BookListActivity extends AppCompatActivity {
    private static final String TAG = "BookList";

    ///////////////////////////////////////////////////////////////////////////
    //                      app-specific settings.                           //
    ///////////////////////////////////////////////////////////////////////////

    private static final String APP_KEY = "m13kixjbozs2w5i";
    private static final String APP_SECRET = "839pohxo9j8fo2g";

    ///////////////////////////////////////////////////////////////////////////
    //                      End app-specific settings.                       //
    ///////////////////////////////////////////////////////////////////////////

    private static final String ACCOUNT_PREFS_NAME = "prefs";
    private static final String ACCESS_KEY_NAME = "ACCESS_KEY";
    private static final String ACCESS_SECRET_NAME = "ACCESS_SECRET";

    DropboxAPI<AndroidAuthSession> mApi;

    private boolean mLoggedIn;

    // Android widgets
    private ListView epubList;
    private GridView epubGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Creamos una nueva sesión the autenticación con dropbox API
        AndroidAuthSession session = buildSession();
        mApi = new DropboxAPI<AndroidAuthSession>(session);

        // Si no estamos ya conectados, abrimos una sesión de login
        if (!mLoggedIn) {
            mApi.getSession().startOAuth2Authentication(BookListActivity.this);
        }
        // El login se resuelve en el onResume

        //
        setContentView(R.layout.activity_book_list);
        final ViewSwitcher vsGridList = (ViewSwitcher) findViewById(R.id.grid_list_switcher);
        vsGridList.reset();
        toList();

        Switch swViewList = (Switch) findViewById(R.id.view_switch);
        swViewList.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    vsGridList.reset();
                    toList();
                } else {
                    vsGridList.getNextView();
                    toGrid();
                }
            }
        });


    }

    private void toGrid() {

    }

    private void toList() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        AndroidAuthSession session = mApi.getSession();

        if (session.authenticationSuccessful()) {
            try {
                // Completamos la autenticacion
                session.finishAuthentication();

                // Guardamos la autenticacion
                storeAuth(session);
                mLoggedIn = true;
            } catch (IllegalStateException e) {
                showToast(getResources().getString(R.string.dropbox_fail) + e.getLocalizedMessage());
            }
        }
    }

    private void showToast(String msg) {
        Toast error = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        error.show();
    }


    /*
     * Guardamos el token de autenticacion del usuario para mantener la sesión abierta
     */
    private void storeAuth(AndroidAuthSession session) {
        //  Si hay token de lo guardamos
        String oauth2AccessToken = session.getOAuth2AccessToken();
        if (oauth2AccessToken != null) {
            SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
            SharedPreferences.Editor edit = prefs.edit();
            edit.putString(ACCESS_KEY_NAME, "oauth2:");
            edit.putString(ACCESS_SECRET_NAME, oauth2AccessToken);
            edit.apply();
        }
    }

    /**
     * Cargamos el token de autenticacion si este ya existia
     */
    private void loadAuth(AndroidAuthSession session) {
        SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
        String key = prefs.getString(ACCESS_KEY_NAME, null);
        String secret = prefs.getString(ACCESS_SECRET_NAME, null);
        if (key == null || secret == null || key.length() == 0 || secret.length() == 0) return;
        session.setOAuth2AccessToken(secret);

    }

    private AndroidAuthSession buildSession() {
        AppKeyPair appKeyPair = new AppKeyPair(APP_KEY, APP_SECRET);

        AndroidAuthSession session = new AndroidAuthSession(appKeyPair);
        loadAuth(session);
        return session;
    }
}
