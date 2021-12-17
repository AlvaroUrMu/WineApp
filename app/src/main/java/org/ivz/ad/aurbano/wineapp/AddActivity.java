package org.ivz.ad.aurbano.wineapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.ivz.ad.aurbano.wineapp.data.Vino;
import org.ivz.ad.aurbano.wineapp.util.CSV;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class AddActivity extends AppCompatActivity {

    private EditText etId, etNombre, etBodega, etColor, etOrigen, etGraduacion, etFecha;
    private Button btAddNew;
    ArrayList<Vino> lista = new ArrayList<>();
    public String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        initialize();
    }

    private void initialize() {
        etId = findViewById(R.id.etAddId);
        etNombre = findViewById(R.id.etAddNombre);
        etBodega = findViewById(R.id.etAddBodega);
        etColor = findViewById(R.id.etAddColor);
        etOrigen = findViewById(R.id.etAddOrigen);
        etGraduacion = findViewById(R.id.etAddGraduacion);
        etFecha = findViewById(R.id.etAddFecha);

        btAddNew = findViewById(R.id.btAddNew);

        fileName = "listadoVinos.csv";

        readFileArray();

        btAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idAdd = Integer.parseInt(etId.getText().toString());
                //Si el ID del vino a añadir no existe
                if(!MainActivity.existId(idAdd, lista)){
                    //Crea un nuevo vino y lo introduce en el archivo csv
                    Vino vino = new Vino(
                            idAdd,
                            etNombre.getText().toString(),
                            etBodega.getText().toString(),
                            etColor.getText().toString(),
                            etOrigen.getText().toString(),
                            Double.parseDouble(String.valueOf(etGraduacion.getText())),
                            Integer.parseInt(String.valueOf(etFecha.getText())));

                    String infoVino = CSV.getCsv(vino);

                    writeInternalFile(infoVino);

                    Intent toMainActivity = new Intent(AddActivity.this, MainActivity.class);
                    startActivity(toMainActivity);
                } else{
                    //Si el ID del vino a añadir ya existe
                    Toast.makeText(AddActivity.this, "El ID introducido ya existe", Toast.LENGTH_SHORT).show();
                }
            }
        });
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

    public boolean writeFile(File file, String string){
        File f = new File(file, fileName);
        FileWriter fw = null;
        boolean ok = true;

        try {
            fw = new FileWriter(f, true);
            fw.write(string + "\n");
            fw.flush();
            fw.close();
        } catch (IOException e) {
            ok = false;
        }
        return ok;
    }

    public void writeInternalFile(String texto) {
        writeFile(getFilesDir(), texto);
    }
}