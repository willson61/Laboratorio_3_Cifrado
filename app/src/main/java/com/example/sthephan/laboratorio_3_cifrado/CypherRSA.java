package com.example.sthephan.laboratorio_3_cifrado;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigInteger;
import java.nio.charset.Charset;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CypherRSA extends AppCompatActivity {

    private static Charset UTF8 = Charset.forName("UTF-8");
    public static Uri file1;
    public static Uri file2;
    public static Uri file3;
    public static String nombreArchivo;
    AlgoritmoRSA cifrado = new AlgoritmoRSA();

    @BindView(R.id.labelContenido)
    TextView labelContenido;
    @BindView(R.id.labelArchivoBusqueda)
    TextView labelArchivoBusqueda;
    @BindView(R.id.labelArchivoLlave)
    TextView labelArchivoLlave;
    @BindView(R.id.scrollview)
    ScrollView scrollview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cypher_rs);
        ButterKnife.bind(this);
        labelContenido.setMovementMethod(new ScrollingMovementMethod());
        scrollview.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                labelContenido.getParent().requestDisallowInterceptTouchEvent(false);

                return false;
            }
        });
        labelContenido.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                labelContenido.getParent().requestDisallowInterceptTouchEvent(true);

                return false;
            }
        });
    }

    public static boolean uriExiste(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        return (cursor != null && cursor.moveToFirst());
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

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                        startActivity(new Intent(CypherRSA.this, MainActivity.class));
                    }
                }).create().show();
    }

    private String leerTextoDeUri(Uri uri) throws IOException {
        InputStream input = getContentResolver().openInputStream(uri);
        BufferedReader reader = new BufferedReader(new InputStreamReader(input, UTF8));
        StringBuilder stringbuilder = new StringBuilder();
        int line = 0;
        while ((line = reader.read()) != -1) {
            char val = (char) line;
            stringbuilder.append(val);
        }
        input.close();
        reader.close();
        return stringbuilder.toString();
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

    @OnClick({R.id.btnCifrar, R.id.btnCancelarArchivo, R.id.btnBuscarArchivo, R.id.btnCancelarLlave, R.id.btnBuscarLlave})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnCifrar:
                if(CypherRSA.file1 != null && CypherRSA.file2 != null){
                    try{
                        Intent intent3 = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                        intent3.addCategory(Intent.CATEGORY_OPENABLE);
                        intent3.setType("*/*");
                        intent3.putExtra(Intent.EXTRA_TITLE, CypherRSA.nombreArchivo.replace(".txt", ".rsacif"));
                        startActivityForResult(intent3, 12345);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.btnCancelarArchivo:
                labelArchivoBusqueda.setText("");
                labelContenido.setText("");
                CypherRSA.file2 = null;
                break;
            case R.id.btnBuscarArchivo:
                Intent intent = new Intent()
                        .addCategory(Intent.CATEGORY_OPENABLE)
                        .setType("*/*")
                        .setAction(Intent.ACTION_OPEN_DOCUMENT);
                startActivityForResult(Intent.createChooser(intent, "Select a File"), 1234);
                break;
            case R.id.btnCancelarLlave:
                labelArchivoLlave.setText("");
                CypherRSA.file1 = null;
                break;
            case R.id.btnBuscarLlave:
                Intent intent2 = new Intent()
                        .addCategory(Intent.CATEGORY_OPENABLE)
                        .setType("*/*")
                        .setAction(Intent.ACTION_OPEN_DOCUMENT);
                startActivityForResult(Intent.createChooser(intent2, "Select a File"), 123);
                break;
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
                    Toast message = Toast.makeText(getApplicationContext(), "Archivo seleccionado exitosamente", Toast.LENGTH_LONG);
                    message.show();
                    labelArchivoLlave.setText(name);
                    CypherRSA.file1 = selectedFile;
                } else {
                    labelArchivoLlave.setText(null);
                    CypherRSA.file1 = null;
                    Toast message = Toast.makeText(getApplicationContext(), "El archivo seleccionado no posee la extension .key de la llave. Por favor seleccione un archivo de extension .key", Toast.LENGTH_LONG);
                    message.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast message = Toast.makeText(getApplicationContext(), "Error en la lectura de archivo", Toast.LENGTH_LONG);
                message.show();
            }
        }
        else if (requestCode == 1234 && resultCode == RESULT_OK ) {
            try{
                Uri selectedFile = data.getData();
                if (name.contains(".txt")) {
                    CypherRSA.nombreArchivo = name;
                    labelArchivoBusqueda.setText(name);
                    labelContenido.setText(leerTextoDeUri(selectedFile));
                    CypherRSA.file2 = selectedFile;
                    Toast message = Toast.makeText(getApplicationContext(), "Archivo seleccionado exitosamente", Toast.LENGTH_LONG);
                    message.show();
                } else {
                    labelArchivoBusqueda.setText(null);
                    CypherRSA.file2 = null;
                    Toast message = Toast.makeText(getApplicationContext(), "El archivo seleccionado no posee la extension .txt del contenido a cifrar. Por favor seleccione un archivo de extension .txt", Toast.LENGTH_LONG);
                    message.show();
                }
            }catch (Exception e){
                e.printStackTrace();
                Toast message = Toast.makeText(getApplicationContext(), "Error en la lectura de archivo", Toast.LENGTH_LONG);
                message.show();
            }
        }
        else if (requestCode == 12345 && resultCode == RESULT_OK ) {
            try{
                Uri selectedFile = data.getData();
                if (name.contains(".rsacif")) {
                    CypherRSA.file3 = selectedFile;
                    String llave = leerTextoDeUri(CypherRSA.file1);
                    String texto = leerTextoDeUri(CypherRSA.file2);
                    String textoCifrado = "";
                    if(texto.equals("")){
                        Toast message = Toast.makeText(getApplicationContext(), "El archivo de contenido a cifrar se encuentra vacio", Toast.LENGTH_LONG);
                        message.show();
                    }
                    else{
                        if(llave.equals("")){
                            Toast message = Toast.makeText(getApplicationContext(), "El archivo de llave se encuentra vacio", Toast.LENGTH_LONG);
                            message.show();
                        }
                        else{
                            String[] val = llave.split(",");
                            int n = Integer.parseInt(val[0]);
                            int e = Integer.parseInt(val[1]);
                            textoCifrado = cifrado.cifrar(texto, e, n);
                            escribirTextoAUri(CypherRSA.file3, textoCifrado);
                            finish();
                            startActivity(new Intent(CypherRSA.this, MainActivity.class));
                            Toast message = Toast.makeText(getApplicationContext(), "Archivo creado y cifrado completado exitosamente", Toast.LENGTH_LONG);
                            message.show();
                        }
                    }
                } else {
                    CypherRSA.file3 = null;
                    Toast message = Toast.makeText(getApplicationContext(), "El nombre del archivo seleccionado no posee la extension .rascif del resultado del cifrado. Por favor escriba un nombre de archivo con extension .rascif", Toast.LENGTH_LONG);
                    message.show();
                }
            }catch (Exception e){
                e.printStackTrace();
                Toast message = Toast.makeText(getApplicationContext(), "Error en la creacion y cifrado del archivo", Toast.LENGTH_LONG);
                message.show();
            }
        }
        else if (resultCode == RESULT_CANCELED) {
            Toast message = Toast.makeText(getApplicationContext(), "Por favor seleccione un archivo para continuar con el cifrado", Toast.LENGTH_LONG);
            message.show();
        }
    }
}
