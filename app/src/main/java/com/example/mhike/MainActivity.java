package com.example.mhike;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import com.example.mhike.R;
import com.example.mhike.DetailActivity;
import com.google.android.material.snackbar.Snackbar;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemTouchHelperListener {

    private RecyclerView rcvHike;
    private Button btnAdd;
    private Button btnCancel;
    private HikeAdater hikeAdater;

    private SearchView searchView;

    private RelativeLayout rootView;

    ArrayList<Hike> hikeArrayList;
    ArrayList<Hike> hikeDeleteArrayList;

    private Toolbar toolbar;

    private static final int REQUEST_CODE = 1;

    SQLiteHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        btnCancel = findViewById(R.id.btnCancel);
        setSupportActionBar(toolbar);

        rcvHike = findViewById(R.id.rcv_hike);
        hikeAdater = new HikeAdater(this);
        myDB = new SQLiteHelper(MainActivity.this);
        hikeArrayList = new ArrayList<>();
        hikeDeleteArrayList = new ArrayList<>();
        rootView = findViewById(R.id.rootView);
        addMethods();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

        rcvHike.setLayoutManager(linearLayoutManager);

        hikeAdater.setData(hikeArrayList);
        rcvHike.setAdapter(hikeAdater);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcvHike.addItemDecoration(itemDecoration);

        //xử lý vuốt phải xóa
        ItemTouchHelper.SimpleCallback simpleCallback = new RecylerViewItemToucherHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(rcvHike);
    }

    private void addMethods() {
        storeDateInArray();
    }

    /**
     * đọc dữ liệu và gắn lên recyler view
     */
    void storeDateInArray() {
        Cursor cursor = myDB.getData();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                Hike hike = new Hike();
                hike.setId(Integer.parseInt(cursor.getString(0)));
                hike.setName(cursor.getString(1));
                hike.setPosition(cursor.getString(2));
                hike.setDate(cursor.getString(3));
                hike.setLength(Double.parseDouble(cursor.getString(4)));
                hike.setParking(Integer.parseInt(cursor.getString(5)));
                hike.setImage(cursor.getString(6));
                hike.setWeather(cursor.getString(7));
                hike.setLevel(cursor.getString(8));
                hike.setNote(cursor.getString(9));
                hike.setObserve(cursor.getString(10));
                hike.setObserve_date(cursor.getString(11));
                hike.setNote_other(cursor.getString(12));
                hikeArrayList.add(hike);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
//        tim kiem
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.findItem).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                hikeAdater.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                hikeAdater.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    //ẩn filter khi ấn nút ẩn bàn phím
    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            hikeArrayList = new ArrayList<>();
            storeDateInArray();
            hikeAdater.setData(hikeArrayList);
            hikeAdater.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        btnAdd = findViewById(R.id.btnAdd);
        if (item.getItemId() == R.id.addItem) {
            startActivityForResult(new Intent(MainActivity.this, DetailActivity.class), REQUEST_CODE);
        } else if (item.getItemId() == R.id.findItem) {
        }

        return super.onOptionsItemSelected(item);
    }

    private List<Hike> getListHike() {
        List<Hike> list = new ArrayList<>();
        list.add(new Hike(1, "test 1", "quảng ninh", "20/01/2001", R.id.radioButton, 100, "Dễ", "", "test", R.drawable.scene_1, "Mưa", "observation 1", "01/20/2001", "note 1"));
        list.add(new Hike(1, "test 2", "quảng ninh", "20/01/2001", R.id.radioButton, 100, "Dễ", "", "test", R.drawable.scene_1, "Mưa", "observation 1", "01/20/2001", "note 1"));
        list.add(new Hike(1, "test 3", "quảng ninh", "20/01/2001", R.id.radioButton, 100, "Dễ", "", "test", R.drawable.scene_1, "Mưa", "observation 1", "01/20/2001", "note 1"));
        list.add(new Hike(1, "test 4", "quảng ninh", "20/01/2001", R.id.radioButton, 100, "Dễ", "", "test", R.drawable.scene_1, "Mưa", "observation 1", "01/20/2001", "note 1"));
        return list;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof HikeAdater.HikeViewHodler) {
            String name = hikeArrayList.get(viewHolder.getAdapterPosition()).getName();

            final Hike hikeDelete = hikeArrayList.get(viewHolder.getAdapterPosition());
            final int indexDelete = viewHolder.getAdapterPosition();
            SQLiteHelper mydb = new SQLiteHelper(MainActivity.this);
            for (int i = 0; i < hikeDeleteArrayList.size(); i++) {
                mydb.deleteHike(String.valueOf(hikeDeleteArrayList.get(i).getId()));
            }
            hikeDeleteArrayList.clear();
            hikeDeleteArrayList.add(hikeDelete);

            //remove item
            hikeAdater.removeItem(indexDelete);

            //hiển thị notify undo item
            Snackbar snackbar = Snackbar.make(rootView, name + " removed!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hikeAdater.undoItem(hikeDelete, indexDelete);
                    if (indexDelete == 0 || indexDelete == hikeArrayList.size() - 1) {
                        rcvHike.scrollToPosition(indexDelete);
                    }
                }
            }).setCallback(new Snackbar.Callback() {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                        mydb.deleteHike(String.valueOf(hikeDelete.getId()));
                        hikeDeleteArrayList.clear();
                    }
                }
            });

            //
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();

        }
    }
}
