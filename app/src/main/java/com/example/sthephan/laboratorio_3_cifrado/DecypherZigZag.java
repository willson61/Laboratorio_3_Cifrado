package com.example.sthephan.laboratorio_3_cifrado;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decypher_zig_zag);
        ButterKnife.bind(this);
        labelContenido.setMovementMethod(new ScrollingMovementMethod());
    }

    @OnClick({R.id.btnBuscar, R.id.btnCancelar, R.id.btnDecifrar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnBuscar:
                break;
            case R.id.btnCancelar:
                break;
            case R.id.btnDecifrar:
                break;
        }
    }
}
