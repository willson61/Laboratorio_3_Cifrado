package com.example.sthephan.laboratorio_3_cifrado;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigInteger;
import java.nio.charset.Charset;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GenerarLlavesRSA extends AppCompatActivity {

    private static Charset UTF8 = Charset.forName("UTF-8");
    public String llavePublica = "";
    public String llavePrivada = "";
    AlgoritmoRSA llaves = new AlgoritmoRSA();

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

    public String obtenerNombreDeArchivoDeUri(Uri uri) {
        String displayName = "";
        Cursor cursor = getApplicationContext().getContentResolver().query(uri, null, null, null, null, null);
        try {
            // moveToFirst() returns false if the cursor has 0 rows.  Very handy for
            // "if there's anything to look at, look at it" conditionals.
            if (cursor != null && cursor.moveToFirst()) {

                // Note it's called "Display Name".  This is
                // provider-specific, and might not necessarily be the file name.
                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            }
        } finally {
            cursor.close();
        }
        return displayName;
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
                BigInteger valP = new BigInteger(txtClaveP.getText().toString());
                BigInteger valQ = new BigInteger(txtClaveQ.getText().toString());
                if(llaves.esPrimo(valP) && llaves.esPrimo(valQ)){
                    llaves.GenerarClaves(valP, valQ);
                    llavePublica += llaves.getClavePublica();
                    llavePrivada += llaves.getClavePrivada();
                    Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("*/*");
                    intent.putExtra(Intent.EXTRA_TITLE, "llavePublica.key");
                    startActivityForResult(intent, 123);
                    Intent intent2 = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                    intent2.addCategory(Intent.CATEGORY_OPENABLE);
                    intent2.setType("*/*");
                    intent2.putExtra(Intent.EXTRA_TITLE, "llavePrivada.key");
                    startActivityForResult(intent2, 1234);
                }
                else{
                    Toast message = Toast.makeText(getApplicationContext(), "Alguno de los numeros de P o Q no es primo, por favor ingrese valores primos", Toast.LENGTH_LONG);
                    message.show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String name = obtenerNombreDeArchivoDeUri(data.getData());
        if (requestCode == 123 && resultCode == RESULT_OK) {
            try {
                Uri selectedFile = data.getData();
                if (name.contains(".key")) {
                    escribirTextoAUri(selectedFile, llavePublica);
                    Toast message = Toast.makeText(getApplicationContext(), "Llave publica creada exitosamente", Toast.LENGTH_LONG);
                    message.show();
                    finish();
                    startActivity(new Intent(GenerarLlavesRSA.this, MainActivity.class));
                } else {
                    Toast message = Toast.makeText(getApplicationContext(), "El nombre del archivo seleccionado no posee la extension .key de la llave. Por favor escriba un nombre de archivo con extension .key", Toast.LENGTH_LONG);
                    message.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast message = Toast.makeText(getApplicationContext(), "Error en la creacion de archivo", Toast.LENGTH_LONG);
                message.show();
            }
        }
        else if (requestCode == 1234 && resultCode == RESULT_OK) {
            try {
                Uri selectedFile = data.getData();
                if (name.contains(".key")) {
                    escribirTextoAUri(selectedFile, llavePrivada);
                    Toast message = Toast.makeText(getApplicationContext(), "Llave privada creada exitosamente", Toast.LENGTH_LONG);
                    message.show();
                    finish();
                    startActivity(new Intent(GenerarLlavesRSA.this, MainActivity.class));
                } else {
                    Toast message = Toast.makeText(getApplicationContext(), "El nombre del archivo seleccionado no posee la extension .key de la llave. Por favor escriba un nombre de archivo con extension .key", Toast.LENGTH_LONG);
                    message.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast message = Toast.makeText(getApplicationContext(), "Error en la creacion de archivo", Toast.LENGTH_LONG);
                message.show();
            }
        }
        else if (resultCode == RESULT_CANCELED) {
            Toast message = Toast.makeText(getApplicationContext(), "Por favor escriba un nombre para guardar la llave", Toast.LENGTH_LONG);
            message.show();
        }
    }
}
