package com.develap.fichas;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AgregarActivity extends AppCompatActivity {

    DbSqlite dbsqlite = new DbSqlite(this);
    EditText txtNombre, txtApellido, txtDireccion, txtTelefono, txtFicha, txtFechanac;
    Button btnAgregar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAgregar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Declaro cajas de texto
        txtNombre = (EditText) findViewById(R.id.txtNombre);
        txtApellido = (EditText) findViewById(R.id.txtApellido);
        txtDireccion = (EditText) findViewById(R.id.txtDireccion);
        txtTelefono = (EditText) findViewById(R.id.txtTelefono);
        txtFicha = (EditText) findViewById(R.id.txtFicha);
        txtFechanac = (EditText) findViewById(R.id.txtFechanac);

        // Defino que cajas de texto deben comenzar por mayusculas
        txtNombre.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        txtApellido.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        txtDireccion.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_agregar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void Agregar(MenuItem item) {

        Toast toast;
        String fechanac = "";
        String fecha = txtFechanac.getText().toString();
        fecha = fecha.replace('.', '/').replace('-', '/');
        Date date = new Date(), today = new Date();

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

        SQLiteDatabase db = dbsqlite.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("apellido", txtApellido.getText().toString());
        values.put("nombre", txtNombre.getText().toString());
        values.put("direccion", txtDireccion.getText().toString());
        values.put("fechanac", fechanac);
        values.put("ficha", txtFicha.getText().toString());
        values.put("telefono", txtTelefono.getText().toString());
        db.insert("pacientes", null, values);
        txtApellido.setText("");
        txtNombre.setText("");
        txtFechanac.setText("");
        txtFicha.setText("");
        txtDireccion.setText("");
        txtTelefono.setText("");
        txtNombre.requestFocus();
        toast = Toast.makeText(this, "Registro añadido correctamente", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0,0);
        toast.show();
    }
}
