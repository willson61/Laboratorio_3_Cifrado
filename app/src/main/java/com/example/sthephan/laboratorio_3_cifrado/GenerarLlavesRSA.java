package com.example.sthephan.laboratorio_3_cifrado;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GenerarLlavesRSA extends AppCompatActivity {

    private static Charset UTF8 = Charset.forName("UTF-8");

    @BindView(R.id.txtClaveP)
    EditText txtClaveP;
    @BindView(R.id.txtClaveQ)
    EditText txtClaveQ;
    @BindView(R.id.scrollview)
    ScrollView scrollview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generar_llaves_rs);
        ButterKnife.bind(this);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                        startActivity(new Intent(GenerarLlavesRSA.this, MainActivity.class));
                    }
                }).create().show();
    }

    public void escribirTextoAUri(Uri uri, String caracter) throws IOException {
        ParcelFileDescriptor file = this.getContentResolver().openFileDescriptor(uri, "wa");
        FileOutputStream fos = new FileOutputStream(file.getFileDescriptor());
        Writer wr = new OutputStreamWriter(fos, UTF8);
        wr.write(caracter);
        wr.flush();
        wr.close();
        fos.close();
        file.close();
    }

    @OnClick(R.id.btnCifrar)
    public void onViewClicked() {
        if((txtClaveP.getText().toString().equals("")) && (txtClaveQ.getText().toString().equals(""))){
            Toast message = Toast.makeText(getApplicationContext(), "Ingrese los valores de P y Q para continuar con la generacion de llaves", Toast.LENGTH_LONG);
            message.show();
        }
        else{
            if((txtClaveP.getText().toString().equals("")) || (txtClaveQ.getText().toString().equals(""))){
                Toast message = Toast.makeText(getApplicationContext(), "Alguno de los campos de P o Q se encuentra vacio, por favor ingreselo para poder continuar", Toast.LENGTH_LONG);
                message.show();
            }
            else{

            }
        }
    }
}
