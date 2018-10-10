package com.example.sthephan.laboratorio_3_cifrado;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CypherZigZagResult extends AppCompatActivity {

    @BindView(R.id.labelNombre)
    TextView labelNombre;
    @BindView(R.id.labelContenido)
    TextView labelContenido;
    @BindView(R.id.labelGrafico1)
    TextView labelGrafico1;

    private static Charset UTF8 = Charset.forName("UTF-8");
    public static String textoCifrado;
    public static Uri file1;
    public static Uri file2;
    public static String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cypher_zig_zag_result);
        ButterKnife.bind(this);
        labelContenido.setMovementMethod(new ScrollingMovementMethod());
        String[] prueba = CypherZigZagResult.file1.getPath().split("/");
        fileName = prueba[prueba.length - 1].replace(".txt", "");
        String prueba2 = prueba[prueba.length - 1].replace(".txt", ".cif");
        labelNombre.setText(prueba2);
        labelContenido.setText(CypherZigZagResult.textoCifrado);
    }

    @OnClick({R.id.btnGuardar, R.id.btnBorrar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnGuardar:
                Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_TITLE, labelNombre.getText().toString());
                startActivityForResult(intent, 1234);
                break;
            case R.id.btnBorrar:
                Toast message = Toast.makeText(getApplicationContext(), "El archivo se a borrado exitosamente", Toast.LENGTH_LONG);
                message.show();
                borrarCampos();
                finish();
                startActivity(new Intent(CypherZigZagResult.this, MainActivity.class));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1234 && resultCode == RESULT_OK && data.getData().getPath().contains(".cif")) {
            try{
                Uri selectedFile = data.getData();
                CypherZigZagResult.file2 = selectedFile;
                ParcelFileDescriptor file = this.getContentResolver().openFileDescriptor(selectedFile, "w");
                FileOutputStream fos = new FileOutputStream(file.getFileDescriptor());
                Writer wr = new OutputStreamWriter(fos, UTF8);
                String textContent = CypherZigZagResult.textoCifrado;
                wr.write(textContent);
                wr.flush();
                wr.close();
                fos.close();
                file.close();
            }catch(Exception e){
                e.printStackTrace();
                Toast message = Toast.makeText(getApplicationContext(), "Error al escribir al archivo cifrado", Toast.LENGTH_LONG);
                message.show();
            }
        } else if (resultCode == RESULT_CANCELED) {
            Toast message = Toast.makeText(getApplicationContext(), "Por favor seleccione un archivo para continuar con el cifrado", Toast.LENGTH_LONG);
            message.show();
        }
        else if (!data.getData().getPath().contains(".cif")){
            Toast message = Toast.makeText(getApplicationContext(), "Por favor utilice la extension .cif para guardar el archivo cifrado", Toast.LENGTH_LONG);
            message.show();
        }
        if (checkURIResource(CypherZigZagResult.this.getApplicationContext(), CypherZigZagResult.file2)) {
            Toast message = Toast.makeText(getApplicationContext(), "El archivo se a creado exitosamente", Toast.LENGTH_LONG);
            message.show();
            borrarCampos();
            finish();
            startActivity(new Intent(CypherZigZagResult.this, MainActivity.class));
        } else {
            Toast message = Toast.makeText(getApplicationContext(), "El archivo no existe", Toast.LENGTH_LONG);
            message.show();
            borrarCampos();
            finish();
            startActivity(new Intent(CypherZigZagResult.this, MainActivity.class));
        }
    }

    public static boolean checkURIResource(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        return (cursor != null && cursor.moveToFirst());
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
                        startActivity(new Intent(CypherZigZagResult.this, MainActivity.class));
                    }
                }).create().show();
    }

    public void borrarCampos(){
        CypherZigZagResult.textoCifrado = null;
        CypherZigZagResult.file1 = null;
        CypherZigZagResult.fileName = null;
        labelNombre.setText(null);
        labelContenido.setText(null);
    }
}
