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

public class CypherSDES extends AppCompatActivity {

    @BindView(R.id.labelContenido)
    TextView labelContenido;
    @BindView(R.id.txtClave)
    EditText txtClave;

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
                break;
            case R.id.btnCancelar:
                break;
            case R.id.btnCifrar:
                break;
        }
    }
}
