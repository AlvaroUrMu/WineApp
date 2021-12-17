package org.ivz.ad.aurbano.wineapp;

import org.ivz.ad.aurbano.wineapp.data.Vino;
import org.ivz.ad.aurbano.wineapp.util.CSV;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Files extends AppCompatActivity{

    public static ArrayList<Vino> lista;
    public String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean writeFile(File file, String string){
        File f = new File(file, this.fileName);
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

    public static String readFile(File file, String fileName){
        File f = new File(file, fileName);
        String texto = "";

        try{
            BufferedReader br = new BufferedReader(new FileReader(f));
            String linea;
            while((linea = br.readLine()) != null){
                texto += linea + "\n";
            }
            br.close();
        } catch(Exception e) {
            texto = null;
        }
        return texto;
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

    /*private void writeResult(boolean result){
        String mensaje = getString(R.string.message_ok);

        if(!result) {
            mensaje = getString(R.string.message_no);
        }
        tvText.setText(mensaje);
    }*/

    public void writeInternalFile(String texto) {
        writeFile(getFilesDir(), texto);
    }

    public void writeExternalFile(String texto) {
        writeFile(getExternalFilesDir(null), texto);
    }

    /*private void writeReadResult(String result){
        String string = result;

        if(result == null){
            string = getString(R.string.read_no);
        } else if(result.isEmpty()){
            string = getString(R.string.read_ok);
        }

        tvText.setText(string);
    }*/

    public String readInternalFile(){
        return readFile(getFilesDir(), fileName);
    }

    public String readExternalFile() {
        return readFile(getExternalFilesDir(null), fileName);
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
