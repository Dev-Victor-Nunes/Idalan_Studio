package com.example.idalanstudio;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MeusAgendamentos extends AppCompatActivity {

    private Button btnReturnMenu;
    private Spinner spinnerUltimosAgendamentos;
    private TextView tvServicoSelecionado;
    private TextView tvDescricaoServico;
    private Button btCancelarAgendamento;

    private BancoControllerAgendamentos bancoController;
    private BancoControllerUsuarios bancoUsuarios;

    private List<Agendamento> agendamentosUsuario;
    private String userEmail;
    private Agendamento agendamentoSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_agendamentos);

        int userId = getIntent().getIntExtra("usuarioId", -1);

        // Fallback via SharedPreferences
        if (userId == -1) {
            SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE);
            userId = prefs.getInt("usuarioId", -1);
        }

        if (userId == -1) {
            Toast.makeText(this, "Usuário não identificado!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        bancoUsuarios = new BancoControllerUsuarios(this);
        userEmail = obterEmailPorId(userId);

        if (userEmail == null || userEmail.isEmpty()) {
            Toast.makeText(this, "E-mail não encontrado!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnReturnMenu = findViewById(R.id.btnReturnMenu);
        spinnerUltimosAgendamentos = findViewById(R.id.spinnerUltimosAgendamentos);
        tvServicoSelecionado = findViewById(R.id.tvServicoSelecionado);
        tvDescricaoServico = findViewById(R.id.tvDescricaoServico);
        btCancelarAgendamento = findViewById(R.id.btCancelarAgendamento);

        bancoController = new BancoControllerAgendamentos(this);

        carregarAgendamentos();

        btnReturnMenu.setOnClickListener(v -> finish());

        spinnerUltimosAgendamentos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                if (agendamentosUsuario != null && pos >= 0 && pos < agendamentosUsuario.size()) {
                    agendamentoSelecionado = agendamentosUsuario.get(pos);
                    exibirDetalhes(agendamentoSelecionado);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                tvDescricaoServico.setText("");
            }
        });

        btCancelarAgendamento.setOnClickListener(v -> {
            if (agendamentoSelecionado != null) {
                boolean sucesso = bancoController.cancelarAgendamento(agendamentoSelecionado.getIdAgendamento());
                if (sucesso) {
                    Toast.makeText(MeusAgendamentos.this, "Agendamento cancelado com sucesso!", Toast.LENGTH_LONG).show();
                    carregarAgendamentos();
                    agendamentoSelecionado = null;
                } else {
                    Toast.makeText(MeusAgendamentos.this, "Erro ao cancelar agendamento!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MeusAgendamentos.this, "Nenhum agendamento selecionado!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String obterEmailPorId(int userId) {
        Cursor cursor = bancoUsuarios.ConsultaPorId(userId);
        if (cursor != null && cursor.moveToFirst()) {
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            cursor.close();
            return email;
        }
        return null;
    }

        //Inicializa a Activity
    private void carregarAgendamentos() {
        agendamentosUsuario = bancoController.getAgendamentosPorUsuario(userEmail);

        if (agendamentosUsuario != null && !agendamentosUsuario.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_dropdown_item,
                    formatarNomesServicos(agendamentosUsuario)
            );
            spinnerUltimosAgendamentos.setAdapter(adapter);
            agendamentoSelecionado = agendamentosUsuario.get(0);
            exibirDetalhes(agendamentoSelecionado);
        } else {
            Toast.makeText(this, "Nenhum agendamento encontrado.", Toast.LENGTH_LONG).show();
            spinnerUltimosAgendamentos.setAdapter(null);
            tvServicoSelecionado.setText("");
            tvDescricaoServico.setText("");
        }
    }

    private String[] formatarNomesServicos(List<Agendamento> lista) {
        if (lista == null) return new String[0];
        String[] nomes = new String[lista.size()];
        for (int i = 0; i < lista.size(); i++) {
            Agendamento ag = lista.get(i);
            nomes[i] = ag.getServico() + " - " + ag.getData();
        }
        return nomes;
    }

    private void exibirDetalhes(Agendamento agendamento) {
        if (agendamento == null) return;

        String texto = agendamento.getServico() + ":\n\n"
                + "Data | " + agendamento.getData() + "\n"
                + "Horário | " + agendamento.getHora() + "\n"
                + "Valor | R$ " + String.format("%.2f", agendamento.getValor()) + "\n"
                + "Status | " + agendamento.getStatus();

        tvServicoSelecionado.setText("Serviço Selecionado:");
        tvDescricaoServico.setText(texto);
    }
}
