package jv.android.utils.async;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by joubert on 31/10/2016.
 */

public abstract class BaseServerSideAsync extends CustomAsyncTask {

    public static final int NONE = -1;
    public static final int CREATE = 0;
    public static final int READ = 1;
    public static final int UPDATE = 2;
    public static final int DELETE = 3;

    private ProgressDialog progressDialog;
    private String msg;
    private Context context;
    private int crud;

    public BaseServerSideAsync(Context context, String msg, int crudOption) {
        this.context = context;
        this.msg = msg;
        this.crud = crudOption;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        try {
            progressDialog=ProgressDialog.show(context, null, msg);
        } catch (Exception e) {
            e.printStackTrace();
        }

        doBeforeBegin();

        if (crud == BaseServerSideAsync.CREATE) {
            onBeforeCreate();
        } else if (crud == BaseServerSideAsync.READ) {
            onBeforeRead();
        } else if (crud == BaseServerSideAsync.UPDATE) {
            onBeforeUpdate();
        } else if (crud == BaseServerSideAsync.DELETE) {
            onBeforeDelete();
        }
    }

    @Override
    protected void onPostExecute( Void result ) {
        super.onPostExecute(result);

        if (progressDialog != null) {
            progressDialog.dismiss();
        }

        onAfterPost();

        if (crud == BaseServerSideAsync.CREATE) {
            onAfterCreate();
        } else if (crud == BaseServerSideAsync.READ) {
            onAfterRead();
        } else if (crud == BaseServerSideAsync.UPDATE) {
            onAfterUpdate();
        } else if (crud == BaseServerSideAsync.DELETE) {
            onAfterDelete();
        }
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        callWebservice(crud);

        return null;
    }

    public Context getContext() {
        return context;
    }

    public int getCrudOption() {
        return crud;
    }

    public abstract void doBeforeBegin();

    public abstract void callWebservice(int crudOption);

    public abstract void onAfterPost();

    public abstract void onAfterCreate();

    public abstract void onAfterRead();

    public abstract void onAfterUpdate();

    public abstract void onAfterDelete();

    public abstract void onBeforeCreate();

    public abstract void onBeforeRead();

    public abstract void onBeforeUpdate();

    public abstract void onBeforeDelete();
}
