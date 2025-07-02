package com.example.idalanstudio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Cadastro extends AppCompatActivity implements View.OnClickListener {

    // Componentes da UI
    private Button btnVoltar, btnSalvar;
    private EditText edtNome, edtCpf, edtTelefone, edtEmail, edtSenha, edtConfirmarSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro);

        // Configuração para edge-to-edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cadastro), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inicializarComponentes();
        configurarListeners();
    }

    private void inicializarComponentes() {
        // Botões
        btnVoltar = findViewById(R.id.btCadReturn);
        btnSalvar = findViewById(R.id.btCadSalvar);

        // Campos de texto
        edtNome = findViewById(R.id.txtCadNome);
        edtTelefone = findViewById(R.id.numCadtel);
        edtCpf = findViewById(R.id.txtCadCpf);
        edtEmail = findViewById(R.id.txtCadEmail);
        edtSenha = findViewById(R.id.txtCadSenha);
        edtConfirmarSenha = findViewById(R.id.txtCadConfirmar);
    }

    private void configurarListeners() {
        btnVoltar.setOnClickListener(this);
        btnSalvar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btCadReturn) {
            // Ação para o botão Voltar - redireciona para tela de Login
            Intent log = new Intent(this, Login.class);
            startActivity(log);
            finish(); // Opcional: encerra a activity atual
        } else if (v.getId() == R.id.btCadSalvar) {
            processarCadastro();
        }
    }

    private void processarCadastro() {
        // Obter valores dos campos
        String _nome = edtNome.getText().toString().trim();
        String _cpf = edtCpf.getText().toString().trim();
        String _telefone = edtTelefone.getText().toString().trim();
        String _email = edtEmail.getText().toString().trim();
        String _senha = edtSenha.getText().toString().trim();
        String _confirmarSenha = edtConfirmarSenha.getText().toString().trim();

        // Validar campos
        if (_nome.isEmpty()) {
            mostrarMensagem("O campo NOME deve ser preenchido!");
            return;
        }

        if (_cpf.isEmpty()) {
            mostrarMensagem("O campo CPF deve ser preenchido!");
            return;
        }

        if (_telefone.isEmpty()) {
            mostrarMensagem("O campo TELEFONE deve ser preenchido!");
            return;
        }

        if (_email.isEmpty()) {
            mostrarMensagem("O campo E-MAIL deve ser preenchido!");
            return;
        }

        if (_senha.isEmpty()) {
            mostrarMensagem("O campo SENHA deve ser preenchido!");
            return;
        }

        if (_confirmarSenha.isEmpty()) {
            mostrarMensagem("O campo CONFIRMAR SENHA deve ser preenchido!");
            return;
        }

        if (!_senha.equals(_confirmarSenha)) {
            mostrarMensagem("Os campos SENHA e CONFIRMAR SENHA devem ser iguais!");
            return;
        }

        // Todos os campos estão válidos - proceder com o cadastro
        BancoControllerUsuarios banco = new BancoControllerUsuarios(getBaseContext());
        String resultado = banco.insereDados(_nome, _cpf, _telefone, _email, _senha);

        mostrarMensagem(resultado);

        if(resultado.contains("sucesso")) { // Se o cadastro foi bem sucedido
            limparCampos();
            // Opcional: redirecionar para Login após cadastro
            Intent log = new Intent(this, Login.class);
            startActivity(log);
            finish();
        }
    }

    private void mostrarMensagem(String mensagem) {
        Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show();
    }

    private void limparCampos() {
        edtNome.setText("");
        edtCpf.setText("");
        edtTelefone.setText("");
        edtEmail.setText("");
        edtSenha.setText("");
        edtConfirmarSenha.setText("");
    }
}