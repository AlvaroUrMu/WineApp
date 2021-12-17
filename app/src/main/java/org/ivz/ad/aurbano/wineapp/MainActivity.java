package org.ivz.ad.aurbano.wineapp;

import androidx.appcompat.app.AppCompatActivity;

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

public class MainActivity extends AppCompatActivity {

    private TextView tvWineList;
    private EditText etIdEdit;
    private Button btEdit, btAdd;
    ArrayList<Vino> lista = new ArrayList<>();
    public String fileName = "listadoVinos.csv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
    }

    private void initialize() {
        tvWineList = findViewById(R.id.tvWineList);

        etIdEdit = findViewById(R.id.etIdEdit);

        btEdit = findViewById(R.id.btEdit);
        btAdd = findViewById(R.id.btAdd);

        //Si el fichero no existe se crea
        if (!readFileArray()) {
            writeEmpty();
        }else{ //Si el archivo existe se rellena con el ArrayList de los vinos
            readFileArray();
        }

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toAddActivity();
            }
        });

        btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editId = etIdEdit.getText().toString();
                //Si el ID del vino a editar no se ha introducido
                if(editId.equals("")){
                    Toast.makeText(MainActivity.this, "Debe introducir el ID del vino a editar", Toast.LENGTH_SHORT).show();
                } else{ //Si el ID del vino a editar se ha introducido
                    //Si el ID del vino a editar ya existe
                    if(existId(Integer.parseInt(editId), lista)){
                        toEditActivity();
                    } else{ //Si el ID del vino a editar no existe
                        Toast.makeText(MainActivity.this, "El ID introducido no existe", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        tvWineList.setText(readInternalFile());
    }

    //Método para ir a la actividad de añadir vino
    public void toAddActivity(){
        Intent toAddActivity = new Intent(this, AddActivity.class);
        startActivity(toAddActivity);
    }

    //Método para ir a la actividad de editar vino
    public void toEditActivity(){
        Intent toEditActivity = new Intent(this, EditActivity.class);
        toEditActivity.putExtra("id", etIdEdit.getText().toString());
        startActivity(toEditActivity);
    }

    //Método para comprobar si existe el ID
    public static boolean existId(int id, ArrayList<Vino> lista){
        if(lista.size() > 0){
            for (int i = 0; i < lista.size(); i++){
                if(id == lista.get(i).getId()){
                    return true;
                }
            }
        }
        return false;
    }

    public String readInternalFile(){
        return Files.readFile(getFilesDir(), fileName);
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