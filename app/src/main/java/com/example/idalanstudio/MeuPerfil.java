package com.example.idalanstudio;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MeuPerfil extends AppCompatActivity {

    private Button btReturn, btSalvar, btDeletConta;
    private EditText numTel, txtEmail, txtSenha, txtConfirmar;

    private BancoController bancoController;
    private BancoControllerUsuarios bancoControllerUsuarios;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meu_perfil);

        bancoController = new BancoController(this);
        bancoControllerUsuarios = new BancoControllerUsuarios(this);

        // Leitura de ID
        userId = getIntent().getIntExtra("codigo", -1);

        // Métodos para inicialização da Activity
        initViews();
        setupListeners();
        carregarDadosUsuario();
    }

    private void initViews() {
        btReturn = findViewById(R.id.btReturn);
        btSalvar = findViewById(R.id.btSalvar);
        btDeletConta = findViewById(R.id.btDeletConta);

        numTel = findViewById(R.id.numTel);
        txtEmail = findViewById(R.id.txtEmail);
        txtSenha = findViewById(R.id.txtSenha);
        txtConfirmar = findViewById(R.id.txtConfirmar);
    }

    private void setupListeners() {
        btReturn.setOnClickListener(v -> {
            finish(); // Volta para a tela anterior
        });

        btSalvar.setOnClickListener(v -> {
            salvarAlteracoes();
        });

        btDeletConta.setOnClickListener(v -> {
            deletarConta();
        });
    }

    private void carregarDadosUsuario() {
        if (userId == -1) {
            Toast.makeText(this, "Erro ao carregar perfil", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Usando o BancoController para carregar os dados
        Cursor cursor = bancoController.carregarUsuario(userId);

        if (cursor != null && cursor.moveToFirst()) {
            txtEmail.setText(cursor.getString(cursor.getColumnIndexOrThrow("email")));
            numTel.setText(cursor.getString(cursor.getColumnIndexOrThrow("telefone")));
            cursor.close();
        } else {
            Toast.makeText(this, "Erro ao carregar dados do usuário", Toast.LENGTH_SHORT).show();
        }
    }

    private void salvarAlteracoes() {
        String telefone = numTel.getText().toString();
        String email = txtEmail.getText().toString();
        String senha = txtSenha.getText().toString();
        String confirmarSenha = txtConfirmar.getText().toString();

        // Validações básicas
        if (email.isEmpty()) {
            txtEmail.setError("Email é obrigatório");
            return;
        }

        if (!senha.isEmpty() && !senha.equals(confirmarSenha)) {
            txtConfirmar.setError("As senhas não coincidem");
            return;
        }

        // Atualiza no banco de dados
        String resultado;
        if (senha.isEmpty()) {
            // Atualiza apenas email e telefone
            resultado = bancoController.alteraDados(userId, telefone, email, "");
        } else {
            // Atualiza tudo incluindo a senha
            resultado = bancoController.alteraDados(userId, telefone, email, senha);
        }

        Toast.makeText(this, resultado, Toast.LENGTH_SHORT).show();
    }

    private void deletarConta() {
        // Confirmação antes de deletar
        new android.app.AlertDialog.Builder(this)
                .setTitle("Confirmar exclusão")
                .setMessage("Tem certeza que deseja excluir sua conta? Esta ação não pode ser desfeita.")
                .setPositiveButton("Excluir", (dialog, which) -> {
                    String resultado = bancoController.excluirDados(userId);
                    Toast.makeText(MeuPerfil.this, resultado, Toast.LENGTH_SHORT).show();

                    // Redireciona para a tela de login após exclusão
                    Intent intent = new Intent(MeuPerfil.this, Login.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Fecha qualquer conexão com o banco se necessário
    }
}