package com.example.idalanstudio;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Locale;

public class Agendamento extends AppCompatActivity {
    // Atributos do modelo incorporados na Activity
    private int idAgendamento;
    private String data;
    private String hora;
    private String servico;
    private double valor;
    private String status;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agendamento);

        setupUI();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.agendamento), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupDatePicker();
        setupTimeSpinner();
        setupReturnButton();
        setupConfirmButton();
    }

    // Getters e Setters para os atributos
    public int getIdAgendamento() {
        return idAgendamento;
    }

    public void setIdAgendamento(int idAgendamento) {
        this.idAgendamento = idAgendamento;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getServico() {
        return servico;
    }

    public void setServico(String servico) {
        this.servico = servico;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private void setupUI() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            servico = extras.getString("servico");
            valor = extras.getDouble("preco");
            String descricao = extras.getString("descricao");
            int imagemResId = extras.getInt("imagem", R.drawable.egipcio_sombra);
            email = extras.getString("email");

            TextView serviceName = findViewById(R.id.serviceName);
            TextView servicePrice = findViewById(R.id.servicePrice);
            TextView serviceDescription = findViewById(R.id.serviceDescription);
            ImageView servicoImage = findViewById(R.id.servicoSelect);

            serviceName.setText(servico + ":");
            servicePrice.setText(String.format(Locale.getDefault(), "Valor: R$%.2f", valor));
            serviceDescription.setText(descricao);
            servicoImage.setImageResource(imagemResId);
        }
    }

    private void setupDatePicker() {
        DatePicker datePicker = findViewById(R.id.DataAge);
        Calendar calendar = Calendar.getInstance();
        datePicker.setMinDate(calendar.getTimeInMillis());
        calendar.add(Calendar.YEAR, 1);
        datePicker.setMaxDate(calendar.getTimeInMillis());

        try {
            Field[] fields = DatePicker.class.getDeclaredFields();
            for (Field field : fields) {
                if (field.getName().equals("mYearSpinner") ||
                        field.getName().equals("mMonthSpinner") ||
                        field.getName().equals("mDaySpinner")) {
                    field.setAccessible(true);
                    NumberPicker spinner = (NumberPicker) field.get(datePicker);
                    spinner.setWrapSelectorWheel(true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Locale locale = new Locale("pt", "BR");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    private void setupTimeSpinner() {
        Spinner spinnerTime = findViewById(R.id.spinnerTime);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.horarios_disponiveis,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTime.setAdapter(adapter);
    }


    private void setupReturnButton() {
        Button btAgendaReturn = findViewById(R.id.btAgendaReturn);
        btAgendaReturn.setOnClickListener(v -> finish());
    }

    private void setupConfirmButton() {
        Button btnAgendar = findViewById(R.id.btnAgendar);
        btnAgendar.setOnClickListener(v -> confirmarAgendamento());
    }

    private void confirmarAgendamento(){
        DatePicker datePicker = findViewById(R.id.DataAge);
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1;
        int year = datePicker.getYear();
        data = String.format(Locale.getDefault(), "%02d/%02d/%d", day, month, year);

        Spinner spinnerTime = findViewById(R.id.spinnerTime);
        hora = spinnerTime.getSelectedItem().toString();
        status = "Agendado";

        // Definindo valores da classe
        Agendamento agendamento = new Agendamento();
        agendamento.setData(data);
        agendamento.setHora(hora);
        agendamento.setServico(servico);
        agendamento.setValor(valor);
        agendamento.setStatus(status);
        agendamento.setEmail(email);

        BancoControllerAgendamentos dbController = new BancoControllerAgendamentos(this);
        String resultado = dbController.insereAgendamento(agendamento);

        Toast.makeText(this, resultado, Toast.LENGTH_LONG).show();

        if (!resultado.contains("Erro")) {
            setResult(RESULT_OK);
            finish();
        }
    }
}


