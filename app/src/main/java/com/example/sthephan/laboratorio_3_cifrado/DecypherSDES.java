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
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DecypherSDES extends AppCompatActivity {

    private static Charset UTF8 = Charset.forName("UTF-8");
    private static Charset ISO = Charset.forName("ISO-8859-1");
    private static Charset ANSI = Charset.forName("Cp1252");
    public static Uri file1;
    public static Uri file2;
    public static String nombreArchivo;
    public SDES decifrado = new SDES();

    @BindView(R.id.labelArchivo)
    TextView labelArchivo;
    @BindView(R.id.labelContenido)
    TextView labelContenido;
    @BindView(R.id.txtClave)
    EditText txtClave;
    @BindView(R.id.scrollview)
    ScrollView scrollview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decypher_sdes);
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

    @OnClick({R.id.btnBuscar, R.id.btnCancelar, R.id.btnDecifrar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnBuscar:
                Intent intent = new Intent()
                        .addCategory(Intent.CATEGORY_OPENABLE)
                        .setType("*/*")
                        .setAction(Intent.ACTION_OPEN_DOCUMENT);
                startActivityForResult(Intent.createChooser(intent, "Select a File"), 123);
                break;
            case R.id.btnCancelar:
                borrarCampos();
                break;
            case R.id.btnDecifrar:
                if (DecypherSDES.file1 != null && !labelContenido.getText().toString().equals("")) {
                    try {
                        if (txtClave.getText().toString().equals("")) {
                            Toast message = Toast.makeText(getApplicationContext(), "Ingrese una clave para poder continuar con el decifrado", Toast.LENGTH_LONG);
                            message.show();
                        } else {
                            if (!(txtClave.getText().toString().length() == 10)) {
                                Toast message = Toast.makeText(getApplicationContext(), "La longitud de la clave binaria debe de ser de 10 bits", Toast.LENGTH_LONG);
                                message.show();
                            } else {
                                if(revisarBinario(txtClave.getText().toString())){
                                    Intent intent2 = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                                    intent2.addCategory(Intent.CATEGORY_OPENABLE);
                                    intent2.setType("text/plain");
                                    intent2.putExtra(Intent.EXTRA_TITLE, DecypherSDES.nombreArchivo.replace(".scif", "").concat("Decifrado.txt"));
                                    startActivityForResult(intent2, 12345);
                                }
                                else{
                                    Toast message = Toast.makeText(getApplicationContext(), "El numero ingresado no es un binario de 10 bits", Toast.LENGTH_LONG);
                                    message.show();
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast message = Toast.makeText(getApplicationContext(), "Error en decifrado de texto", Toast.LENGTH_LONG);
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
                if (name.contains(".scif")) {
                    Toast message = Toast.makeText(getApplicationContext(), "Archivo seleccionado exitosamente", Toast.LENGTH_LONG);
                    message.show();
                    labelArchivo.setText(name);
                    labelContenido.setText(leerTextoDeUri(selectedFile));
                    DecypherSDES.nombreArchivo = name;
                    DecypherSDES.file1 = selectedFile;
                } else {
                    labelArchivo.setText(null);
                    labelContenido.setText(null);
                    DecypherSDES.file1 = null;
                    Toast message = Toast.makeText(getApplicationContext(), "El archivo seleccionado no posee la extension .scif para decifrar. Por favor seleccione un archivo de extension .scif", Toast.LENGTH_LONG);
                    message.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (resultCode == RESULT_CANCELED) {
            Toast message = Toast.makeText(getApplicationContext(), "Por favor seleccione un archivo para continuar con el decifrado", Toast.LENGTH_LONG);
            message.show();
        }
        else if (requestCode == 12345 && resultCode == RESULT_OK && name.contains(".txt")) {
            try {
                Uri selectedFile = data.getData();
                DecypherSDES.file2 = selectedFile;
                decifrado.Llaves.generarLlaves(txtClave.getText().toString());
                decifrado.Metodos.setearSBoxes();
                String text = "";
                for(int i = 0; i < labelContenido.getText().toString().toCharArray().length; i++){
                    text += decifrado.Descifrar(labelContenido.getText().toString().charAt(i));
                }
                escribirTextoAUri(DecypherSDES.file2, text);
            } catch (Exception e) {
                e.printStackTrace();
                Toast message = Toast.makeText(getApplicationContext(), "Error al escribir al archivo decifrado", Toast.LENGTH_LONG);
                message.show();
            }
            if (uriExiste(DecypherSDES.this.getApplicationContext(), DecypherSDES.file2)) {
                Toast message = Toast.makeText(getApplicationContext(), "El archivo se a creado exitosamente", Toast.LENGTH_LONG);
                message.show();
                borrarCampos();
                finish();
                startActivity(new Intent(DecypherSDES.this, MainActivity.class));
            } else {
                Toast message = Toast.makeText(getApplicationContext(), "El archivo no existe", Toast.LENGTH_LONG);
                message.show();
                borrarCampos();
                finish();
                startActivity(new Intent(DecypherSDES.this, MainActivity.class));
            }
        } else if (!name.contains(".txt")) {
            Toast message = Toast.makeText(getApplicationContext(), "Por favor utilice la extension .txt para guardar el archivo decifrado", Toast.LENGTH_LONG);
            message.show();
        }
    }

    public static boolean uriExiste(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        return (cursor != null && cursor.moveToFirst());
    }

    public void escribirTextoAUri(Uri uri, String caracter) throws IOException{
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
                        startActivity(new Intent(DecypherSDES.this, MainActivity.class));
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
        DecypherSDES.file1 = null;
        DecypherSDES.file2 = null;
        labelArchivo.setText(null);
        labelContenido.setText(null);
        txtClave.setText(null);
        decifrado = new SDES();
    }
}
