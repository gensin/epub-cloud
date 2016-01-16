package es.pau.epubcloud.bookList.bookList;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;

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

    private static final boolean USE_OAUTH1 = false;

    DropboxAPI<AndroidAuthSession> mApi;

    private boolean mLoggedIn;

    // Android widgets

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
    }
}
