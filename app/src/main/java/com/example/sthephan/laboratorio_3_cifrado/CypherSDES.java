package com.example.sthephan.laboratorio_3_cifrado;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CypherSDES extends AppCompatActivity {

    @BindView(R.id.labelContenido)
    TextView labelContenido;
    @BindView(R.id.txtClave)
    EditText txtClave;

    public static Uri file;
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
                if (CypherSDES.file != null) {
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
                if (CypherSDES.file != null && !labelContenido.getText().toString().equals("")) {
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

                                    borrarCampos();
                                    finish();
                                    startActivity(new Intent(CypherSDES.this, CypherSDESResult.class));
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
        if (requestCode == 123 && resultCode == RESULT_OK) {
            try {
                Uri selectedFile = data.getData();
                String name = obtenerNombreDeArchivoDeUri(selectedFile);
                if (name.contains(".txt")) {
                    Toast message = Toast.makeText(getApplicationContext(), "Archivo seleccionado exitosamente", Toast.LENGTH_LONG);
                    message.show();
                    labelArchivo.setText(name);
                    labelContenido.setText(leerTextoDeUri(selectedFile));
                    CypherSDES.file = selectedFile;
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

    public Character leerCaracterDeTexto(String text, int posicion) {
        return text.charAt(posicion);
    }

    public boolean revisarBinario(String texto) {
        boolean val = false;
        for (int i = 0; i < texto.length(); i++) {
            Character e = new Character(texto.charAt(i));
            if ((e.equals("0")) || (e.equals("1"))) {
                val = true;
            } else {
                return false;
            }
        }
        return val;
    }

    public void borrarCampos() {
        CypherSDES.file = null;
        labelArchivo.setText(null);
        labelContenido.setText(null);
        txtClave.setText(null);
    }
}
