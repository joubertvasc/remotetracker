package jv.android.utils.activity;

import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import jv.android.utils.R;

public abstract class BasicEditActivity extends AppCompatActivity {

    private Intent editIntent;
    private boolean _isEditing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        editIntent = getIntent();
        _isEditing = false;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (editIntent != null) {
            Bundle params = editIntent.getExtras();

            if (params != null) {
                doGetExtraParameters(params);
            }
        }
    }

    public abstract void doGetExtraParameters(Bundle params);

    public abstract boolean validFields();

    public void setFieldsToSave(Intent intent) {
        // Virtual
        intent.putExtra("isediting", _isEditing);
    }

    public boolean isEditing() {
        return _isEditing;
    }

    public void setEditing(boolean editing) {
        _isEditing = editing;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setIcon(R.drawable.exclamation);
            alert.setTitle(getString(R.string.warning));
            alert.setMessage(getString(_isEditing ? R.string.avCancelEditing : R.string.avCancelNewItem));
            alert.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    setResult(RESULT_CANCELED);
                    finish();
                }
            });

            alert.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.dismiss();
                }
            });

            alert.create();
            alert.show();

            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_ok) {
            doOk();
        }

        return super.onOptionsItemSelected(item);
    }

    private void doOk() {
        if (validFields()) {
            setFieldsToSave(editIntent);
            setResult(RESULT_OK, editIntent);
            finish();
        }
    }
}
