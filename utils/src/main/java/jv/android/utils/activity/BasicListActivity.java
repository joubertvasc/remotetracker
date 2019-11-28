package jv.android.utils.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import jv.android.utils.R;
import jv.android.utils.interfaces.IBasicListActivityEvents;

/**
 * Created by joubertvasc on 26/10/2016.
 */

public abstract class BasicListActivity extends AppCompatActivity implements IBasicListActivityEvents {

    public static final int NEW_ITEM = 0;
    public static final int EDIT_ITEM = 1;

    private Intent activityIntent;
    private int cad_menu;
    private String _id = "id";
    private Class new_record_class;
    private ListView lvItems;
    private List items;
    private ArrayAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityIntent = getIntent();

        setMenu(R.menu.cad_menu);

        if (activityIntent != null) {
            Bundle params = activityIntent.getExtras();

            if (params != null) {
                doGetExtraParameters(params);
            }
        }

        doLoadData();
    }

    public abstract void doGetExtraParameters(Bundle params);

    public void setMenu(int menu) {
        this.cad_menu = menu;
    }

    public void setNewRecordClass(Class activity) {
        // Must be overrided
        this.new_record_class = activity;
    }

    public void setIdField(String field) {
        _id = field;
    }

    public abstract void addExtraParametersNewItem(Intent newItemIntent);

    public abstract Object doNewItemResult(Intent data);

    public abstract void doLoadData();

    public abstract int getItemKey(int idx);

    public abstract String getSortKey(Object obj);

    public abstract void getFieldsToEdit(int id, int idx, Intent activityIntent);

    public abstract void doEditItemResultField(Object item, Intent data);

    public void setListViewComponents(List items, ListView lvItems, ArrayAdapter itemsAdapter) {
        if (items == null) {
            Toast.makeText(this, getString(R.string.avListItemsIsNull), Toast.LENGTH_LONG).show();
        } else if (lvItems == null) {
            Toast.makeText(this, getString(R.string.avListViewIsNull), Toast.LENGTH_LONG).show();
        } else if (itemsAdapter == null) {
            Toast.makeText(this, getString(R.string.avAdapterIsNull), Toast.LENGTH_LONG).show();
        } else {
            this.items = items;
            this.lvItems = lvItems;
            this.itemsAdapter = itemsAdapter;

            sort();
            lvItems.setAdapter(itemsAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(cad_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            doNewItem();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == NEW_ITEM) {
                doInternalNewItemResult(data);
            } else if (requestCode == EDIT_ITEM) {
                doEditItemResult(data);
            }
        }
    }

    public void addNewItem(Object obj) {
        if (obj != null) {
            items.add(obj);

            sort();
            itemsAdapter.notifyDataSetChanged();
        }
    }

    private void doInternalNewItemResult(Intent data) {
        addNewItem(doNewItemResult(data));
    }

    public void sort() {
        if (items != null) {
            Collections.sort(items, new Comparator<Object>() {
                public int compare(Object b1, Object b2) {
                    return getSortKey(b1).compareTo(getSortKey(b2));
                }
            });
        }
    }

    public int findItemIndexById(int id) {
        for (int i = 0; i < items.size(); i++)
            if (id == getItemKey(i)) {
                return i;
            }

        return -1;
    }

    public boolean onBeforeNewItem() {
        return true;
    }

    private void doNewItem() {
        if (new_record_class == null) {
            Toast.makeText(this, getString(R.string.avSetNewRecordClassNotDefined), Toast.LENGTH_LONG).show();
        } else {
            if (onBeforeNewItem()) {
                Intent intent = new Intent(this, new_record_class);
                addExtraParametersNewItem(intent);
                startActivityForResult(intent, NEW_ITEM);
            }
        }
    }

    public void doUpdateItem(int idx, Object obj) {
        items.remove(idx);
        items.add(obj);

        sort();
        itemsAdapter.notifyDataSetChanged();
    }

    private void doEditItemResult(Intent data) {
        int id = data.getIntExtra(_id, -1);

        if (id > -1) {
            int idx = findItemIndexById(id);

            if (idx > -1) {
                doEditItemResultField(items.get(idx), data);
                sort();
                itemsAdapter.notifyDataSetChanged();
            }
        }
    }

    public void doRemoveItem(int id) {
        int idx = findItemIndexById(id);

        if (idx > -1) {
            items.remove(idx);
            itemsAdapter.notifyDataSetChanged();
        }
    }

    public void doEraseItem(int id, int idx) {
        if (idx > -1) {
            items.remove(idx);
            itemsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onEditButton(final int id) {
        int idx = findItemIndexById(id);

        Intent intent = new Intent(this, new_record_class);
        getFieldsToEdit(id, idx, intent);
        startActivityForResult(intent, EDIT_ITEM);
    }

    @Override
    public void onTrashButton(final int id) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setIcon(R.drawable.exclamation);
        alert.setTitle(getString(R.string.warning));
        alert.setMessage(getString(R.string.avConfirmErase));
        alert.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                int idx = findItemIndexById(id);

                doEraseItem(id, idx);
            }
        });

        alert.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });

        alert.create();
        alert.show();
    }
}
