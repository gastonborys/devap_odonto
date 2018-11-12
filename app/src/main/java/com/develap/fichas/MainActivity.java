package com.develap.fichas;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    DbSqlite dbsqlite = new DbSqlite(this);
    ListView Lista;
    ArrayList<Paciente> listado;
    ListadoPacientes adapter;
    SearchView searchview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Lista = (ListView) findViewById(R.id.ListadoMain);

        Lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Paciente item;
                item = listado.get(position);
                Intent intent = new Intent(MainActivity.this, EditarActivity.class);
                intent.putExtra("RECORD_ID", item.id);
                startActivity(intent);


            }
        });

        CargarListado();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast toast;
        Intent intent = null;

        switch(item.getItemId())
        {
            case R.id.action_add:
                intent = new Intent(this, AgregarActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_search:
                searchview = (SearchView) item.getActionView();
                searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                    @Override
                    public boolean onQueryTextSubmit(String query) {

                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        adapter.getFilter().filter(newText);

                        return false;
                    }
                });
                return true;
            case R.id.action_restart:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(true);
                builder.setTitle("Confirmar borrado de datos");
                builder.setMessage("¿Está seguro que desea limpiar la base de datos?\r\n\r\nAl confirmar, todos los registros de la tabla: \"fichas de paciente\" serán eliminados.\r\n\r\n¿Desea continuar?");
                builder.setPositiveButton("Eliminar Datos", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase db = dbsqlite.getWritableDatabase();
                        db.execSQL("DELETE FROM pacientes");
                        CargarListado();
                        Toast toast;
                        toast = Toast.makeText(MainActivity.this, "Base de datos eliminada", (int) Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast toast;
                        toast = Toast.makeText(MainActivity.this, "Se cancelo la acción Eliminar datos.", (int) Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0,0);
                        toast.show();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            case R.id.action_refresh:
                CargarListado();
                toast = Toast.makeText(this, "Actualizado", (int) Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0,0);
                toast.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void CargarListado()
    {
        Toast toast;
        SQLiteDatabase db = dbsqlite.getReadableDatabase();
        String[] campos = {"ficha", "apellido", "nombre", "fechanac"};
        String order = "ficha ASC";


        Cursor cursor = db.rawQuery("SELECT id, ficha, nombre, apellido, fechanac, telefono FROM pacientes ORDER BY ficha ASC",null, null);

        listado = new ArrayList<>();
        adapter = new ListadoPacientes(this, listado);
        int i = cursor.getCount();
        Log.i("Contador de registros: ", Integer.toString(i));
        while(cursor.moveToNext())
        {
            String fechanac = "N/D";
            String id = cursor.getString(cursor.getColumnIndexOrThrow("id"));
            String ficha = cursor.getString(cursor.getColumnIndexOrThrow("ficha"));
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
            String apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido"));
            String fechastr = cursor.getString(cursor.getColumnIndexOrThrow("fechanac"));
            String telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono"));
            try {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date date = format.parse(fechastr);
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                fechanac = df.format(date);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            listado.add(new Paciente(id, apellido, nombre, fechanac, ficha, telefono));
        }


        Lista.setAdapter(adapter);
        Lista.setTextFilterEnabled(true);

    }
}
