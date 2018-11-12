package com.develap.fichas;

import android.content.Context;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import android.widget.TextView;


import java.util.ArrayList;

public class ListadoPacientes extends BaseAdapter implements Filterable {
    private ArrayList<Paciente> pacientes;
    private ArrayList<Paciente> original;
    LayoutInflater inflater;


    public ListadoPacientes(Context context, ArrayList<Paciente> pacientes)
    {
        inflater = LayoutInflater.from(context);

        this.pacientes = pacientes;
        this.original = pacientes;
    }

    @Override
    public int getCount(){
        return pacientes.size();
    }

    @Override
    public Object getItem(int position) {
        return pacientes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return pacientes.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertview, ViewGroup parent)
    {

        if (convertview == null)
        {
            convertview = inflater.inflate(R.layout.item_paciente, parent, false);
        }

        TextView cpoId = (TextView) convertview.findViewById(R.id.cpoId);
        TextView cpoFicha = (TextView) convertview.findViewById(R.id.cpoFicha);
        TextView cpoApellido = (TextView) convertview.findViewById(R.id.cpoApellido);
        TextView cpoNombre = (TextView) convertview.findViewById(R.id.cpoNombre);
        TextView cpoFechanac = (TextView) convertview.findViewById(R.id.cpoFechanac);
        TextView cpoTelefono = (TextView) convertview.findViewById(R.id.cpoTelefono);

        cpoId.setText(pacientes.get(position).id);
        cpoFicha.setText(pacientes.get(position).ficha);
        cpoApellido.setText(pacientes.get(position).apellido);
        cpoNombre.setText(pacientes.get(position).nombre);
        cpoFechanac.setText(pacientes.get(position).fechanac);
        cpoTelefono.setText(pacientes.get(position).telefono);

        return convertview;

    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {

                pacientes = (ArrayList<Paciente>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                ArrayList<Paciente> FilteredArrList = new ArrayList<>();

                if (original == null) {
                    original = new ArrayList<Paciente>(pacientes);
                }
                if (constraint == null || constraint.length() == 0) {

                    results.count = original.size();
                    results.values = original;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < original.size(); i++) {
                        String data = original.get(i).apellido +  " " + original.get(i).nombre +  " " + original.get(i).fechanac +  " " + original.get(i).telefono;
                        if (data.toLowerCase().contains(constraint.toString())) {
                            FilteredArrList.add(new Paciente(original.get(i).id, original.get(i).apellido, original.get(i).nombre, original.get(i).fechanac, original.get(i).ficha, original.get(i).telefono));
                        }
                    }
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }

                return results;
            }
        };
        return filter;
    }
}
