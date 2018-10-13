package com.example.sthephan.laboratorio_3_cifrado;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CypherZigZag extends AppCompatActivity {


    public static Uri file;
    public static ZigZag zzCypher = new ZigZag();

    @BindView(R.id.labelArchivo)
    TextView labelArchivo;
    @BindView(R.id.labelContenido)
    TextView labelContenido;
    @BindView(R.id.txtNivel)
    EditText txtNivel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cypher_zig_zag);
        ButterKnife.bind(this);
        labelContenido.setMovementMethod(new ScrollingMovementMethod());
        txtNivel.clearFocus();
    }

    @OnClick({R.id.btnBuscar, R.id.btnCancelar, R.id.btnCifrar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnBuscar:
                if(CypherZigZag.file != null){
                    borrarCampos();
                }
                else{
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
                if(CypherZigZag.file != null){
                    try{
                        if(txtNivel.getText().toString().equals("")){
                            Toast message = Toast.makeText(getApplicationContext(), "Ingrese un nivel para poder continuar con el cifrado", Toast.LENGTH_LONG);
                            message.show();
                        }
                        else{
                            String textoCifrado = zzCypher.cifrado(txtNivel.getText().toString(), leerTextoDeUri(CypherZigZag.file));
                            CypherZigZagResult.textoCifrado = textoCifrado;
                            CypherZigZagResult.file1 = CypherZigZag.file;
                            borrarCampos();
                            finish();
                            startActivity(new Intent(CypherZigZag.this, CypherZigZagResult.class));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast message = Toast.makeText(getApplicationContext(), "Error en cifrado de texto", Toast.LENGTH_LONG);
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
                String[] prueba = selectedFile.getPath().split("/");
                if(prueba[prueba.length - 1].contains(".txt")){
                    Toast message = Toast.makeText(getApplicationContext(), "Archivo seleccionado exitosamente", Toast.LENGTH_LONG);
                    message.show();
                    labelArchivo.setText(prueba[prueba.length - 1]);
                    labelContenido.setText(leerTextoDeUri(selectedFile));
                    CypherZigZag.file = selectedFile;
                }
                else{
                    borrarCampos();
                    Toast message = Toast.makeText(getApplicationContext(), "El archivo seleccionado no posee la extension .txt para cifrado. Por favor seleccione un archivo de extension .txt", Toast.LENGTH_LONG);
                    message.show();
                }
            }catch(Exception e){
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
                        startActivity(new Intent(CypherZigZag.this, MainActivity.class));
                    }
                }).create().show();
    }

    private String leerTextoDeUri(Uri uri) throws IOException {
        InputStream input = getContentResolver().openInputStream(uri);
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
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

    public void borrarCampos(){
        CypherZigZag.file = null;
        labelArchivo.setText(null);
        labelContenido.setText(null);
        txtNivel.setText(null);
        CypherZigZag.zzCypher = new ZigZag();
    }
}
