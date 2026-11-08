package com.example.tarea;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    // Campos de texto del formulario
    private EditText etNombres, etApellidos, etNumeroId;

    // Menús desplegables (AutoCompleteTextView dentro de TextInputLayout)
    private AutoCompleteTextView actvTipoDoc, actvDia, actvMes, actvAnio;

    // Opciones de género
    private RadioButton rbHombre, rbMujer, rbOtro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_main);

        // Ajustar el contenido para que no quede debajo de la barra de estado / navegación
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Vincular vistas
        etNombres = findViewById(R.id.etNombres);
        etApellidos = findViewById(R.id.etApellidos);
        etNumeroId = findViewById(R.id.etNumeroId);

        actvTipoDoc = findViewById(R.id.actvTipoDoc);
        actvDia = findViewById(R.id.actvDia);
        actvMes = findViewById(R.id.actvMes);
        actvAnio = findViewById(R.id.actvAnio);

        rbHombre = findViewById(R.id.rbHombre);
        rbMujer = findViewById(R.id.rbMujer);
        rbOtro = findViewById(R.id.rbOtro);

        configurarDesplegables();
    }

    /**
     * Llena los cuatro menús desplegables con información real
     * (tipo de documento, día, mes y año) y los deja listos para abrirse al tocarlos.
     */
    private void configurarDesplegables() {

        // --- Tipo de documento ---
        String[] tiposDocumento = getResources().getStringArray(R.array.tipos_documento);
        actvTipoDoc.setAdapter(adaptador(tiposDocumento));

        // --- Día: 1 .. 31 ---
        String[] dias = new String[31];
        for (int i = 0; i < dias.length; i++) {
            dias[i] = String.valueOf(i + 1);
        }
        actvDia.setAdapter(adaptador(dias));

        // --- Mes ---
        String[] meses = getResources().getStringArray(R.array.meses);
        actvMes.setAdapter(adaptador(meses));

        // --- Año: del año actual hacia 1950 ---
        int anioActual = Calendar.getInstance().get(Calendar.YEAR);
        int cantidad = anioActual - 1950 + 1;
        String[] anios = new String[cantidad];
        for (int i = 0; i < cantidad; i++) {
            anios[i] = String.valueOf(anioActual - i);
        }
        actvAnio.setAdapter(adaptador(anios));

        // Al tocar cualquier campo se despliega la lista con las opciones
        prepararApertura(actvTipoDoc);
        prepararApertura(actvDia);
        prepararApertura(actvMes);
        prepararApertura(actvAnio);
    }

    /**
     * Adaptador de texto plano: muestra la opción elegida dentro del campo
     * y la lista completa al desplegarlo.
     */
    private ArrayAdapter<String> adaptador(String[] items) {
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View vista = super.getView(position, convertView, parent);
                        if (vista instanceof TextView) {
                            TextView tv = (TextView) vista;
                            tv.setTextSize(18);
                            tv.setTextColor(ContextCompat.getColor(getContext(), R.color.texto_campo));
                            tv.setSingleLine(true);
                        }
                        return vista;
                    }
                };
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        return adapter;
    }

    /**
     * Abre la lista de opciones cuando el usuario toca el campo.
     */
    private void prepararApertura(final AutoCompleteTextView campo) {
        campo.setThreshold(1);
        campo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                campo.showDropDown();
            }
        });
    }

    /**
     * Devuelve el valor elegido en un desplegable ("" si aún no se elige nada).
     */
    private String valorDe(AutoCompleteTextView campo) {
        return campo.getText().toString().trim();
    }

    /**
     * Devuelve el género seleccionado ("" si no hay ninguno).
     */
    private String generoSeleccionado() {
        if (rbHombre.isChecked()) return getString(R.string.genero_hombre);
        if (rbMujer.isChecked()) return getString(R.string.genero_mujer);
        if (rbOtro.isChecked()) return getString(R.string.genero_otro);
        return "";
    }

    /**
     * Lectura completa del formulario.
     * Se puede llamar desde un botón "Registrar" cuando lo agregues.
     */
    public String resumenFormulario() {
        StringBuilder sb = new StringBuilder();
        sb.append("Nombres: ").append(etNombres.getText().toString().trim()).append("\n");
        sb.append("Apellidos: ").append(etApellidos.getText().toString().trim()).append("\n");
        sb.append("Tipo de documento: ").append(valorDe(actvTipoDoc)).append("\n");
        sb.append("Número de identidad: ").append(etNumeroId.getText().toString().trim()).append("\n");
        sb.append("Género: ").append(generoSeleccionado()).append("\n");
        sb.append("Fecha de nacimiento: ")
                .append(valorDe(actvDia)).append(" de ")
                .append(valorDe(actvMes)).append(" de ")
                .append(valorDe(actvAnio));
        return sb.toString();
    }
}
