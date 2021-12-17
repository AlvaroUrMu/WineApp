package org.ivz.ad.aurbano.wineapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.ivz.ad.aurbano.wineapp.data.Vino;
import org.ivz.ad.aurbano.wineapp.util.CSV;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class EditActivity extends AppCompatActivity {

    private TextView tvEditId;
    private EditText etEditNombre, etEditBodega, etEditColor, etEditOrigen, etEditGraduacion, etEditFecha;
    private Button btEditOne, btDelete;
    ArrayList<Vino> lista = new ArrayList<>();
    public String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        
        initialize();
    }

    private void initialize() {
        tvEditId = findViewById(R.id.tvEditId);

        etEditNombre = findViewById(R.id.etEditNombre);
        etEditBodega = findViewById(R.id.etEditBodega);
        etEditColor = findViewById(R.id.etEditColor);
        etEditOrigen = findViewById(R.id.etEditOrigen);
        etEditGraduacion = findViewById(R.id.etEditGraduacion);
        etEditFecha = findViewById(R.id.etEditFecha);

        btEditOne = findViewById(R.id.btEditOne);
        btDelete = findViewById(R.id.btDelete);

        fileName = "listadoVinos.csv";

        readFileArray();

        //Recibo el intent de la MainActivity con el ID del vino para editar
        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString("id");

        //Relleno los campos
        fillFields(bundle);

        btEditOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editVino(Integer.parseInt(id), getFields());
                writeFile();

                Intent toMainActivity = new Intent(EditActivity.this, MainActivity.class);
                startActivity(toMainActivity);
            }
        });

        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                builder.setTitle(("Eliminar vino"))
                        .setMessage("Está a punto de eliminar el vino, ¿desea continuar?")
                        .setPositiveButton(R.string.confirmar, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteVino(Integer.parseInt(id));
                                writeFile();

                                Toast.makeText(EditActivity.this, "El vino se ha eliminado éxitosamente", Toast.LENGTH_SHORT).show();

                                Intent toMainActivity = new Intent(EditActivity.this, MainActivity.class);
                                startActivity(toMainActivity);
                            }
                        })
                        .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {}
                        });
                builder.create().show();
            }
        });
    }

    //Método para obtener un nuevo vino editado
    public Vino getFields(){
        Vino getVino = new Vino();

        getVino.setId(Integer.parseInt(tvEditId.getText().toString()));
        getVino.setNombre(etEditNombre.getText().toString());
        getVino.setBodega(etEditBodega.getText().toString());
        getVino.setColor(etEditColor.getText().toString());
        getVino.setOrigen(etEditOrigen.getText().toString());
        getVino.setGraduacion(Double.parseDouble(etEditGraduacion.getText().toString()));
        getVino.setFecha(Integer.parseInt(etEditFecha.getText().toString()));

        return getVino;
    }

    public void fillFields(Bundle bundle){
        String id = bundle.getString("id");
        tvEditId.setText(id);

        Vino vino = searchVino(Integer.parseInt(id));
        etEditNombre.setText(vino.getNombre());
        etEditBodega.setText(vino.getBodega());
        etEditColor.setText(vino.getColor());
        etEditOrigen.setText(vino.getOrigen());
        etEditGraduacion.setText(String.valueOf(vino.getGraduacion()));
        etEditFecha.setText(String.valueOf(vino.getFecha()));
    }

    //Método para buscar el vino dentro del ArrayList
    private Vino searchVino(int id){
        Vino vinoVino = new Vino();
        for (Vino getVino: lista) {
            int id2 = getVino.getId();
            if (id2 == id){
                vinoVino = getVino;
            }
        }
        return vinoVino;
    }

    //Método para borrar el vino del ArrayList
    public void deleteVino(int id){
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId() == id){
                lista.remove(i);
            }
        }
    }

    //Método para reemplazar el vino por el editado en el ArrayList
    private void editVino(int id, Vino editVino){
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId() == id){
                lista.set(i, editVino);
            }
        }
    }

    public boolean writeFile(){
        File f = new File(getFilesDir(), fileName);
        boolean ok = true;
        if (f.exists()) {
            f.delete();
            f = new File(getFilesDir(), fileName);
            FileWriter fw = null; //FileWriter(File f,boolean append)
            String infoVinoCsv;
            for (int i = 0; i < lista.size(); i++) {
                if (lista.get(i).getId() != 0){
                    infoVinoCsv = CSV.getCsv(lista.get(i));
                    try {
                        fw = new FileWriter(f, true);
                        fw.write( infoVinoCsv + "\n");
                        fw.flush();
                        fw.close();
                    } catch (IOException e) {
                        ok = false;
                    }
                }
            }
        }
        return ok;
    }

    public boolean readFileArray(){
        File f = new File(getFilesDir(), fileName);
        boolean read = true;

        try{
            BufferedReader br = new BufferedReader(new FileReader(f));
            String linea;
            while((linea = br.readLine()) != null){
                lista.add((Vino) CSV.getVino(linea));
            }
            br.close();
        } catch(Exception e) {
            read = false;
        }
        return read;
    }

    public String readInternalFile(){
        return Files.readFile(getFilesDir(), fileName);
    }

    public void writeEmpty(){
        File f = new File(getFilesDir(), fileName);
        FileWriter fw = null;
        try {
            fw = new FileWriter(f, true);
            fw.write("");
            fw.flush();
            fw.close();
        } catch (IOException e) {
        }
    }
}