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
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CypherSDES extends AppCompatActivity {

    private static Charset UTF8 = Charset.forName("UTF-8");
    public static Uri file1;
    public static Uri file2;
    public SDES cifrado = new SDES();

    @BindView(R.id.labelContenido)
    TextView labelContenido;
    @BindView(R.id.txtClave)
    EditText txtClave;
    @BindView(R.id.labelArchivo)
    TextView labelArchivo;
    @BindView(R.id.scrollview)
    ScrollView scrollview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cypher_sdes);
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

    @OnClick({R.id.btnBuscar, R.id.btnCancelar, R.id.btnCifrar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnBuscar:
                if (CypherSDES.file1 != null) {
                    borrarCampos();
                } else {
                    Intent intent = new Intent()
                            .addCategory(Intent.CATEGORY_OPENABLE)
                            .setType("*/*")
                            .setAction(Intent.ACTION_OPEN_DOCUMENT);
                    startActivityForResult(Intent.createChooser(intent, "Select a File"), 123);
                }
                break;
            case R.id.btnCancelar:
                borrarCampos();
                break;
            case R.id.btnCifrar:
                if (CypherSDES.file1 != null && !labelContenido.getText().toString().equals("")) {
                    try {
                        if (txtClave.getText().toString().equals("")) {
                            Toast message = Toast.makeText(getApplicationContext(), "Ingrese una clave para poder continuar con el cifrado", Toast.LENGTH_LONG);
                            message.show();
                        } else {
                            if (!(txtClave.getText().toString().length() == 10)) {
                                Toast message = Toast.makeText(getApplicationContext(), "La longitud de la clave binaria debe de ser de 10 bits", Toast.LENGTH_LONG);
                                message.show();
                            } else {
                                if(revisarBinario(txtClave.getText().toString())){
                                    Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                                    intent.setType("*/*");
                                    intent.putExtra(Intent.EXTRA_TITLE, obtenerNombreDeArchivoDeUri(CypherSDES.file1).replace(".txt", ".scif"));
                                    startActivityForResult(intent, 1234);
                                }
                                else{
                                    Toast message = Toast.makeText(getApplicationContext(), "El numero ingresado no es un binario de 10 bits", Toast.LENGTH_LONG);
                                    message.show();
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast message = Toast.makeText(getApplicationContext(), "Error en cifrado de texto", Toast.LENGTH_LONG);
                        message.show();
                    }
                } else {
                    Toast message = Toast.makeText(getApplicationContext(), "Seleccione un archivo para poder continuar", Toast.LENGTH_LONG);
                    message.show();
                }
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
                if (name.contains(".txt")) {
                    Toast message = Toast.makeText(getApplicationContext(), "Archivo seleccionado exitosamente", Toast.LENGTH_LONG);
                    message.show();
                    labelArchivo.setText(name);
                    labelContenido.setText(leerTextoDeUri(selectedFile));
                    CypherSDES.file1 = selectedFile;
                } else {
                    borrarCampos();
                    Toast message = Toast.makeText(getApplicationContext(), "El archivo seleccionado no posee la extension .txt para cifrado. Por favor seleccione un archivo de extension .txt", Toast.LENGTH_LONG);
                    message.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (resultCode == RESULT_CANCELED) {
            Toast message = Toast.makeText(getApplicationContext(), "Por favor seleccione un archivo para continuar con el cifrado", Toast.LENGTH_LONG);
            message.show();
            borrarCampos();
        }
        else if (requestCode == 1234 && resultCode == RESULT_OK && name.contains(".scif")) {
            try {
                Uri selectedFile = data.getData();
                CypherSDES.file2 = selectedFile;
                cifrado.Llaves.generarLlaves(txtClave.getText().toString());
                cifrado.Metodos.setearSBoxes();
                //cifrarCaracteresDeUri(CypherSDES.file1);
                String text = "";
                for(int i = 0; i < labelContenido.getText().toString().toCharArray().length; i++){
                    text += cifrado.Cifrar(labelContenido.getText().toString().charAt(i));
                }
                escribirCaracterAUri(CypherSDES.file2, text);
                /*int cont = 0;
                Character entrada = leerCaracterDeUri(CypherSDES.file1, cont);
                while(entrada != '\u0000'){
                    escribirCaracterAUri(CypherSDES.file2, cifrado.Cifrar(entrada));
                    cont++;
                    entrada = leerCaracterDeUri(CypherSDES.file1, cont);
                }*/
            } catch (Exception e) {
                e.printStackTrace();
                Toast message = Toast.makeText(getApplicationContext(), "Error al escribir al archivo cifrado", Toast.LENGTH_LONG);
                message.show();
            }
            if (uriExiste(CypherSDES.this.getApplicationContext(), CypherSDES.file2)) {
                Toast message = Toast.makeText(getApplicationContext(), "El archivo se a creado exitosamente", Toast.LENGTH_LONG);
                message.show();
                borrarCampos();
                finish();
                startActivity(new Intent(CypherSDES.this, MainActivity.class));
            } else {
                Toast message = Toast.makeText(getApplicationContext(), "El archivo no existe", Toast.LENGTH_LONG);
                message.show();
                borrarCampos();
                finish();
                startActivity(new Intent(CypherSDES.this, MainActivity.class));
            }
        } else if (!name.contains(".scif")) {
            Toast message = Toast.makeText(getApplicationContext(), "Por favor utilice la extension .cif para guardar el archivo cifrado", Toast.LENGTH_LONG);
            message.show();
        }
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
                        startActivity(new Intent(CypherSDES.this, MainActivity.class));
                    }
                }).create().show();
    }

    public static boolean uriExiste(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        return (cursor != null && cursor.moveToFirst());
    }

    public String obtenerNombreDeArchivoDeUri(Uri uri) {
        String displayName = "";
        Cursor cursor = getApplicationContext().getContentResolver().query(uri, null, null, null, null, null);
        try {
            // moveToFirst() returns false if the cursor has 0 rows.  Very handy for
            // "if there's anything to look at, look at it" conditionals.
            if (cursor != null && cursor.moveToFirst()) {
                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            }
        } finally {
            cursor.close();
        }
        return displayName;
    }

    private String leerTextoDeUri(Uri uri) throws IOException {
        InputStream input = getContentResolver().openInputStream(uri);
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
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

    public Character leerCaracterDeUri(Uri uri, int posicion) throws IOException{
        InputStream input = getContentResolver().openInputStream(uri);
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        int cont = 0;
        int line = 0;
        while ((line = reader.read()) != -1) {
            if(cont == posicion){
                char val = (char) line;
                return val;
            }
            else{
                cont++;
            }
        }
        input.close();
        reader.close();
        return '\u0000';
    }

    public void cifrarCaracteresDeUri(Uri uri) throws IOException{
        InputStream input = getContentResolver().openInputStream(uri);
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        int line = 0;
        while ((line = reader.read()) != -1) {
            char val = (char) line;
            escribirCaracterAUri(CypherSDES.file2, cifrado.Cifrar(val));
        }
        input.close();
        reader.close();
    }

    public void escribirCaracterAUri(Uri uri, Character caracter) throws IOException{
        ParcelFileDescriptor file = this.getContentResolver().openFileDescriptor(uri, "wa");
        FileOutputStream fos = new FileOutputStream(file.getFileDescriptor());
        Writer wr = new OutputStreamWriter(fos, UTF8);
        wr.write(caracter);
        wr.flush();
        wr.close();
        fos.close();
        file.close();
    }

    public void escribirCaracterAUri(Uri uri, String caracter) throws IOException{
        ParcelFileDescriptor file = this.getContentResolver().openFileDescriptor(uri, "wa");
        FileOutputStream fos = new FileOutputStream(file.getFileDescriptor());
        Writer wr = new OutputStreamWriter(fos, UTF8);
        wr.write(caracter);
        wr.flush();
        wr.close();
        fos.close();
        file.close();
    }

    public boolean revisarBinario(String texto) {
        boolean val = false;
        for (int i = 0; i < texto.length(); i++) {
            Character e = new Character(texto.charAt(i));
            if ((e.equals('0')) || (e.equals('1'))) {
                val = true;
            } else {
                return false;
            }
        }
        return val;
    }

    public void borrarCampos() {
        CypherSDES.file1 = null;
        CypherSDES.file2 = null;
        labelArchivo.setText(null);
        labelContenido.setText(null);
        txtClave.setText(null);
    }
}
