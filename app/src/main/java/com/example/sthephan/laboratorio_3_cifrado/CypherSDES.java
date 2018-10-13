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
    @BindView(R.id.labelNombre)
    TextView labelNombre;

    public static Uri file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cypher_sdes);
        ButterKnife.bind(this);
        labelContenido.setMovementMethod(new ScrollingMovementMethod());
    }

    @OnClick({R.id.btnBuscar, R.id.btnCancelar, R.id.btnCifrar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnBuscar:
                if(CypherSDES.file != null){
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
                break;
            case R.id.btnCifrar:
                break;
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
        CypherSDES.file = null;
        labelNombre.setText(null);
        labelContenido.setText(null);
        txtClave.setText(null);
    }
}
