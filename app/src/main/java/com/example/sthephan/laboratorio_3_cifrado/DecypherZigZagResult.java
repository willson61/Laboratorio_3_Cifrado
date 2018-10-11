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

public class DecypherZigZagResult extends AppCompatActivity {

    @BindView(R.id.labelNombre)
    TextView labelNombre;
    @BindView(R.id.labelContenido)
    TextView labelContenido;
    @BindView(R.id.labelGrafico1)
    TextView labelGrafico1;

    private static Charset UTF8 = Charset.forName("UTF-8");
    public static String textoDecifrado;
    public static String nombreArchivo;
    public static Uri file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decypher_zig_zag_result);
        ButterKnife.bind(this);
        labelContenido.setMovementMethod(new ScrollingMovementMethod());
        labelContenido.setText(DecypherZigZagResult.textoDecifrado);
        labelNombre.setText(DecypherZigZagResult.nombreArchivo);
    }

    @OnClick({R.id.btnGuardar, R.id.btnBorrar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnGuardar:
                Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_TITLE, DecypherZigZagResult.nombreArchivo.replace(".cif", "").concat("Decifrado.txt"));
                startActivityForResult(intent, 12345);
                break;
            case R.id.btnBorrar:
                Toast message = Toast.makeText(getApplicationContext(), "El archivo se a borrado exitosamente", Toast.LENGTH_LONG);
                message.show();
                finish();
                startActivity(new Intent(DecypherZigZagResult.this, MainActivity.class));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String name = obtenerNombreDeArchivoDeUri(data.getData());
        if (requestCode == 12345 && resultCode == RESULT_OK && name.contains(".txt")) {
            try{
                Uri selectedFile = data.getData();
                DecypherZigZagResult.file = selectedFile;
                ParcelFileDescriptor file = this.getContentResolver().openFileDescriptor(selectedFile, "w");
                FileOutputStream fos = new FileOutputStream(file.getFileDescriptor());
                Writer wr = new OutputStreamWriter(fos, UTF8);
                String textContent = DecypherZigZagResult.textoDecifrado;
                wr.write(textContent);
                wr.flush();
                wr.close();
                fos.close();
                file.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        } else if (resultCode == RESULT_CANCELED) {
            Toast message = Toast.makeText(getApplicationContext(), "Por favor seleccione un archivo para continuar con el decifrado", Toast.LENGTH_LONG);
            message.show();
        }
        else if (!name.contains(".txt")){
            Toast message = Toast.makeText(getApplicationContext(), "Por favor utilice la extension .txt para guardar el archivo decifrado", Toast.LENGTH_LONG);
            message.show();
        }
        if (uriExiste(DecypherZigZagResult.this.getApplicationContext(), DecypherZigZagResult.file)){
            Toast message = Toast.makeText(getApplicationContext(), "El archivo se a creado exitosamente", Toast.LENGTH_LONG);
            message.show();
            finish();
            startActivity(new Intent(DecypherZigZagResult.this, MainActivity.class));
        }
        else{
            Toast message = Toast.makeText(getApplicationContext(), "El archivo no existe", Toast.LENGTH_LONG);
            message.show();
            finish();
            startActivity(new Intent(DecypherZigZagResult.this, MainActivity.class));
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

    public static boolean uriExiste(Context context, Uri uri) {
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
                        startActivity(new Intent(DecypherZigZagResult.this, MainActivity.class));
                    }
                }).create().show();
    }
}
