package com.develap.fichas;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditarActivity extends AppCompatActivity {

    DbSqlite dbsqlite = new DbSqlite(this);
    EditText txtFicha, txtNombre, txtApellido, txtDireccion, txtTelefono, txtFechanac;
    String ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarEditar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Cargo la variable con el Id del Registro a Editar
        this.ID = getIntent().getStringExtra("RECORD_ID");
        // Cargo el registro desde la base de datos
        CargarRegistro();

        // Declaro las cajas de texto
        txtFicha = (EditText) findViewById(R.id.txtEFicha);
        txtNombre = (EditText) findViewById(R.id.txtENombre);
        txtApellido = (EditText) findViewById(R.id.txtEApellido);
        txtDireccion = (EditText) findViewById(R.id.txtEDireccion);
        txtTelefono = (EditText) findViewById(R.id.txtETelefono);
        txtFechanac = (EditText) findViewById(R.id.txtEFechanac);

        // Defino las cajas de texto que quiero que comiencen con mayusculas
        txtNombre.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        txtApellido.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        txtDireccion.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_editar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    public void CargarRegistro()
    {
        final String id = this.ID;

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                    SQLiteDatabase db = dbsqlite.getReadableDatabase();
                    Cursor cursor = db.rawQuery("SELECT * FROM pacientes WHERE id = '"+ id +"'",null, null);
                    cursor.moveToFirst();

                    final String ficha = cursor.getString(cursor.getColumnIndexOrThrow("ficha"));
                    final String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                    final String apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido"));
                    final String direccion = cursor.getString(cursor.getColumnIndexOrThrow("direccion"));
                    final String telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono"));
                    String fechastr = cursor.getString(cursor.getColumnIndexOrThrow("fechanac"));

                    try {
                        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = format.parse(fechastr);
                        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                        fechastr = df.format(date);
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }

                    final String fechanac = fechastr;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtNombre.setText(nombre);
                        txtApellido.setText(apellido);
                        txtDireccion.setText(direccion);
                        txtTelefono.setText(telefono);
                        txtFicha.setText(ficha);
                        txtFechanac.setText(fechanac);
                    }
                });
            }
        });

    }

    public void Eliminar(MenuItem item) {
        final String id = this.ID;
        final Context context = this;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Confirmar borrado de datos");
        builder.setMessage("¿Está seguro que desea eliminar la ficha actual?\r\n\r\nAl confirmar, la ficha será eliminada sin posibilidad recuperación.\r\n\r\n¿Desea continuar?");
        builder.setPositiveButton("Eliminar ficha", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SQLiteDatabase db = dbsqlite.getWritableDatabase();
                db.execSQL("DELETE FROM pacientes WHERE id = " + id);
                Toast toast;
                toast = Toast.makeText(context, "Ficha eliminada", (int) Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
                EditarActivity.super.onBackPressed();

            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast toast;
                toast = Toast.makeText(context, "Se cancelo la acción Eliminar ficha.", (int) Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0,0);
                toast.show();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void Modificar(MenuItem item) {
        Toast toast;
        String fechanac = "";
        SQLiteDatabase db = dbsqlite.getWritableDatabase();
        ContentValues values = new ContentValues();
        String where = "id = ?";
        String[] like = {this.ID};
        String fecha = txtFechanac.getText().toString();
        Date date = new Date(), today = new Date();

        fecha = fecha.replace('.', '/').replace('-', '/');
        try {
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            date = format.parse(fecha);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            fechanac = df.format(date);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        if (txtNombre.getText().toString().isEmpty())
        {
            toast = Toast.makeText(this, "El valor del campo \"Nombre\" no es válido", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0,0);
            toast.show();
            return;
        }

        if (txtApellido.getText().toString().isEmpty())
        {
            toast = Toast.makeText(this, "El valor del campo \"Apellido\" no es válido", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0,0);
            toast.show();
            return;
        }

        try {
            if (Integer.parseInt(txtFicha.getText().toString()) == 0) {
                toast = Toast.makeText(this, "El valor del campo \"Ficha\" no es válido", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }
        }catch(Exception e)
        {
            e.printStackTrace();
            toast = Toast.makeText(this, "El valor del campo \"Ficha\" no es válido", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }

        if (date.after(today) || date == today)
        {
            toast = Toast.makeText(this, "El valor del campo \"Nacimiento\" no es válido", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0,0);
            toast.show();
            return;
        }

        values.put("apellido", txtApellido.getText().toString());
        values.put("nombre", txtNombre.getText().toString());
        values.put("direccion", txtDireccion.getText().toString());
        values.put("fechanac", fechanac);
        values.put("ficha", txtFicha.getText().toString());
        values.put("telefono", txtTelefono.getText().toString());
        db.update("pacientes", values, where, like );

        toast = Toast.makeText(this, "Registro modificado correctamente", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0,0);
        toast.show();
    }
}
