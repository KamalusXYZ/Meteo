package com.example.meteo;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public final class VMListProvider<P extends ListProvider<E>, E> extends AndroidViewModel {
    private final Application application;
    private static final String ERROR_WRONG_SERVER_RESPONSE = "ERROR_WRONG_SERVER_RESPONSE";
    private static final String ERROR_WRONG_JSON_RESPONSE = "ERROR_WRONG_JSON_RESPONSE";
    static final String STATE_LOADING_STARTS = "STATE_LOADING_STARTS";
    static final String STATE_LOADING_ENDS = "STATE_LOADING_ENDS";
    static final String STATE_CLICK_ON_ITEM = "STATE_CLICK_ON_ITEM";
    static final String STATE_DONE = "STATE_DONE";
    static final String STATE_NO_INTERNET = "STATE_NO_INTERNET";


    private final MutableLiveData<P> mldProvider = new MutableLiveData<>();
    private MutableLiveData<ArrayList<E>> mldList;
    private final MutableLiveData<String> mldState = new MutableLiveData<>();
    private final MutableLiveData<Integer> mldPosition = new MutableLiveData<>();


    public VMListProvider(@NonNull Application application) {
        super(application);
        // Récupérer la référence à l'application.
        this.application = application;
    }

    public MutableLiveData<String> getMldState() {
        return mldState;
    }

    public MutableLiveData<Integer> getMldPosition() {
        return mldPosition;
    }

    public void setStateDone() {
        mldState.setValue(STATE_DONE);
    }


    public MutableLiveData<ArrayList<E>> getMldList(boolean forceReload) {
        // Si pas encore ou plus d'ArrayList, instancier et requeter le serveur du provider.
        if (mldList == null) {
            mldList = new MutableLiveData<>();
            loadData();
        } else if (forceReload) {
            loadData();
        }
        // Retourner l'ArrayList.
        return mldList;
    }

    public P getProvider() {
        return mldProvider.getValue();
    }

    public void setProvider(P provider) {
        this.mldProvider.setValue(provider);
    }

    public void setPosition(int position) {
        // Sauvegarder la position
        mldPosition.setValue(position);
        // Signaler le clique sur item.
        mldState.setValue(STATE_CLICK_ON_ITEM);
    }

    public E getItem() {
        ArrayList<E> list = mldList.getValue();
        Integer position = mldPosition.getValue();
        return list != null && position != null ? list.get(position) : null;
    }


    private void loadData() {
        // Si connexion internet active, requêter le serveur du provider.
        if (Util.isConnected(this.application))
            new AsyncTaskProvider().execute(mldProvider.getValue());
        else mldState.setValue(STATE_NO_INTERNET);
    }

    /**
     * L'API level 30 a marqué comme déprécié AsyncTask (Java) mais pas CoroutineScope (Kotlin) alors
     * que cette dernière est une stricte copie de la première.
     * C'est une démarche marketing pour promouvoir Kotlin en poussant les développeur Java à utiliser
     * java.util.concurrent nettement moins abordable.
     */

    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskProvider extends AsyncTask<P, Void, ArrayList<E>> {

        @Override
        protected void onPreExecute() {
            // Signaler le début du chargement.
            mldState.setValue(STATE_LOADING_STARTS);
        }

        @SafeVarargs
        @Override
        protected final ArrayList<E> doInBackground(P... providers) {
            // Requêter le ListProvider.
            P provider = providers[0];
            // Préparer un InputStream.
            InputStream is;
            //Requêter le serveur du ListProvider.
            try {
                is = new URL(provider.getURL()).openStream();
            } catch (IOException e) {
                Log.e("ListProvider", ERROR_WRONG_SERVER_RESPONSE);
                return null;
            }
            // Lire la réponse ligne à ligne
            StringBuilder sb = new StringBuilder();
            Scanner scanner = new Scanner(is);
            while (scanner.hasNextLine())
                sb.append(scanner.nextLine());
            // Exploiter la réponse et récupérer un ArrayList.
            ArrayList<E> list;
            try {
                list = provider.toList(sb.toString());
            } catch (Exception e) {
                Log.e("ListProvider", ERROR_WRONG_JSON_RESPONSE);
                return null;
            }
            // Retourne l'ArrayList.
            return list;
        }


        @Override
        protected void onPostExecute(ArrayList<E> list) {
            Log.d("onPostExecute", list.toString());
            // Définir l'ArrayList LiveData à partir de l'ArrayList reçu.
            VMListProvider.this.mldList.setValue(list);
            // Signaler la fin du chargement.
            mldState.setValue(STATE_LOADING_ENDS);
        }
    }
}