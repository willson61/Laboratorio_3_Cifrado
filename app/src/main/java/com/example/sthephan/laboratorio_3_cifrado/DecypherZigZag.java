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
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DecypherZigZag extends AppCompatActivity {

    @BindView(R.id.labelArchivo)
    TextView labelArchivo;
    @BindView(R.id.labelContenido)
    TextView labelContenido;
    @BindView(R.id.txtNivel)
    EditText txtNivel;

    private static Charset UTF8 = Charset.forName("UTF-8");
    public static Uri file;
    public static ZigZag zzDecypher = new ZigZag();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decypher_zig_zag);
        ButterKnife.bind(this);
        labelContenido.setMovementMethod(new ScrollingMovementMethod());
        txtNivel.clearFocus();
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
                labelArchivo.setText(null);
                labelContenido.setText(null);
                DecypherZigZag.file = null;
                break;
            case R.id.btnDecifrar:
                if(DecypherZigZag.file != null){
                    try{
                        if(txtNivel.getText().toString().equals("")){
                            Toast message = Toast.makeText(getApplicationContext(), "Ingrese un nivel para poder continuar con el cifrado", Toast.LENGTH_LONG);
                            message.show();
                        }
                        else{
                            String textoDecifrado = zzDecypher.Descifrar(txtNivel.getText().toString(), leerTextoDeUri(DecypherZigZag.file));
                            DecypherZigZagResult.textoDecifrado = textoDecifrado;
                            DecypherZigZagResult.file = CypherZigZag.file;
                            DecypherZigZagResult.nombreArchivo = obtenerNombreDeArchivoDeUri(DecypherZigZag.file);
                            labelArchivo.setText(null);
                            labelContenido.setText(null);
                            DecypherZigZag.file = null;
                            finish();
                            startActivity(new Intent(DecypherZigZag.this, DecypherZigZagResult.class));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast message = Toast.makeText(getApplicationContext(), "Error en decifrado de texto: " + e.toString(), Toast.LENGTH_LONG);
                        message.show();
                    }
                }
                else{
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
            try{
                Uri selectedFile = data.getData();
                String name = obtenerNombreDeArchivoDeUri(selectedFile);
                if(name.contains(".cif")){
                    Toast message = Toast.makeText(getApplicationContext(), "Archivo seleccionado exitosamente", Toast.LENGTH_LONG);
                    message.show();
                    labelArchivo.setText(name);
                    labelContenido.setText(leerTextoDeUri(selectedFile));
                    DecypherZigZag.file = selectedFile;
                }
                else{
                    labelArchivo.setText(null);
                    labelContenido.setText(null);
                    DecypherZigZag.file = null;
                    Toast message = Toast.makeText(getApplicationContext(), "El archivo seleccionado no posee la extension .cif para decifrar. Por favor seleccione un archivo de extension .cif", Toast.LENGTH_LONG);
                    message.show();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        } else if (resultCode == RESULT_CANCELED) {
            Toast message = Toast.makeText(getApplicationContext(), "Por favor seleccione un archivo para continuar con el decifrado", Toast.LENGTH_LONG);
            message.show();
        }
    }

    public String obtenerNombreDeArchivoDeUri(Uri uri)
    {
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

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                        startActivity(new Intent(DecypherZigZag.this, MainActivity.class));
                    }
                }).create().show();
    }

    private String leerTextoDeUri(Uri uri) throws IOException {
        InputStream input = getContentResolver().openInputStream(uri);
        BufferedReader reader = new BufferedReader(new InputStreamReader(input, UTF8));
        StringBuilder stringbuilder = new StringBuilder();
        int line = 0;
        while ((line = reader.read()) != -1) {
            char val = (char)line;
            stringbuilder.append(val);
        }
        input.close();
        reader.close();
        return stringbuilder.toString();
    }
}
