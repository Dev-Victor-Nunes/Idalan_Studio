package com.example.idalanstudio;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Button btnAgendarEgipcio, btnAgendarBrasileiro, btnAgendarHenna;
    private ImageView menuIcon;
    private Button btnMeuPerfil;
    private TextView suporte, contato, feedback;
    private Button btnAgendamentos;
    private Button btnLogoff;
    private int userId;
    private String emailUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Recupera ID do Intent
        userId = getIntent().getIntExtra("USER_ID", -1);
        emailUsuario = getIntent().getStringExtra("email");

        // Armazena o ID em SharedPreferences
        SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE);
        prefs.edit().putInt("usuarioId", userId).apply();

        // Referências
        drawerLayout = findViewById(R.id.drawerLayout);
        btnAgendarBrasileiro = findViewById(R.id.btnAgendarBrasileiro);
        btnAgendarEgipcio = findViewById(R.id.btnAgendarEgipcio);
        btnAgendarHenna = findViewById(R.id.btnAgendarHenna);
        menuIcon = findViewById(R.id.menuIcon);
        btnMeuPerfil = findViewById(R.id.btnMeuPerfil);
        suporte = findViewById(R.id.suporte);
        contato = findViewById(R.id.contato);
        feedback = findViewById(R.id.feedback);
        btnAgendamentos = findViewById(R.id.btnAgendamentos);
        btnLogoff = findViewById(R.id.btnLogoff);

        // Botões de agendamento
        btnAgendarEgipcio.setOnClickListener(v -> abrirTelaAgendamento(
                "Cílios Egípcio", 115.00,
                "O volume egípcio ou cílios em W...",
                R.drawable.egipcio_sombra));

        btnAgendarBrasileiro.setOnClickListener(v -> abrirTelaAgendamento(
                "Cílios Brasileiro", 110.00,
                "Extensão de cílios volume brasileiro...",
                R.drawable.brasileiro));

        btnAgendarHenna.setOnClickListener(v -> abrirTelaAgendamento(
                "Design de Sobrancelha", 45.00,
                "Henna feita das folhas em pó da Lawsonia...",
                R.drawable.design_henna));

        // Menu lateral
        menuIcon.setOnClickListener(v -> {
            if (!drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                drawerLayout.openDrawer(GravityCompat.END);
            } else {
                drawerLayout.closeDrawer(Gravity.LEFT);
            }
        });

        btnMeuPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MeuPerfil.class);
            intent.putExtra("codigo", userId);
            startActivity(intent);
        });

        View.OnClickListener abrirSuporte = v -> {
            Intent intent = new Intent(MainActivity.this, InformacoesActivity.class);
            startActivity(intent);
        };

        suporte.setOnClickListener(abrirSuporte);
        contato.setOnClickListener(abrirSuporte);
        feedback.setOnClickListener(abrirSuporte);

        btnAgendamentos.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MeusAgendamentos.class);
            intent.putExtra("usuarioId", userId);
            startActivity(intent);
        });

        btnLogoff.setOnClickListener(v -> new AlertDialog.Builder(MainActivity.this)
                .setTitle("Confirmação")
                .setMessage("Deseja realmente sair da conta?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    startActivity(new Intent(MainActivity.this, Login.class));
                    finish();
                })
                .setNegativeButton("Não", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show());
    }

    private void abrirTelaAgendamento(String servico, double preco, String descricao, int imagemResId) {
        Intent intent = new Intent(MainActivity.this, Agendamento.class);
        intent.putExtra("servico", servico);
        intent.putExtra("preco", preco);
        intent.putExtra("descricao", descricao);
        intent.putExtra("imagem", imagemResId);
        intent.putExtra("codigo", userId);
        intent.putExtra("email", emailUsuario);
        startActivity(intent);
    }
}
