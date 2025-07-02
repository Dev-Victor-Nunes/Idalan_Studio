package com.example.idalanstudio;

import android.content.Intent;
import android.database.Cursor;
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

public class Login extends AppCompatActivity implements View.OnClickListener {

    // Constantes para evitar strings hardcoded
    private static final String EXTRA_EMAIL = "email";
    private static final String EXTRA_USER_ID = "USER_ID"; // Nova constante
    private static final String MSG_EMAIL_VAZIO = "O campo de E-mail deve ser preenchido";
    private static final String MSG_SENHA_VAZIA = "O campo de Senha deve ser preenchido";
    private static final String MSG_LOGIN_INVALIDO = "E-mail/Senha inválidos ou não cadastrados";

    // Componentes da UI
    private EditText txtUserLogin, txtSenhaLogin;
    private Button btLogin, btCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Configuração edge-to-edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inicializarViews();
        configurarListeners();
    }

    private void inicializarViews() {
        txtUserLogin = findViewById(R.id.txtUserLogin);
        txtSenhaLogin = findViewById(R.id.txtSenhaLogin);
        btLogin = findViewById(R.id.btLogin);
        btCadastro = findViewById(R.id.btCadastro);
    }

    private void configurarListeners() {
        btLogin.setOnClickListener(this);
        btCadastro.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btLogin) {
            tratarLogin();
        } else if (id == R.id.btCadastro) {
            iniciarCadastro();
        }
    }

    private void tratarLogin() {
        if (validarLogin()) {
            // Método iniciarMainActivity() agora é chamado dentro de verificarCredenciais()
        }
    }

    private boolean validarLogin() {
        String email = txtUserLogin.getText().toString().trim();
        String senha = txtSenhaLogin.getText().toString().trim();

        if (!validarCampos(email, senha)) {
            return false;
        }

        return verificarCredenciais(email, senha);
    }

    private boolean validarCampos(String email, String senha) {
        if (email.isEmpty()) {
            mostrarToast(MSG_EMAIL_VAZIO);
            return false;
        }

        if (senha.isEmpty()) {
            mostrarToast(MSG_SENHA_VAZIA);
            return false;
        }

        return true;
    }

    private boolean verificarCredenciais(String email, String senha) {
        BancoControllerUsuarios bd = new BancoControllerUsuarios(getBaseContext());

        try (Cursor dados = bd.ConsultaDadosLogin(email, senha)) {
            if (dados.moveToFirst()) {
                int userId = dados.getInt(dados.getColumnIndexOrThrow("codigo")); // Novo: obtém ID
                iniciarMainActivity(email, userId); // Novo: passa email e ID
                return true;
            } else {
                mostrarToast(MSG_LOGIN_INVALIDO);
                return false;
            }
        }
    }

    // Novo método sobrecarregado
    private void iniciarMainActivity(String email, int userId) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(EXTRA_EMAIL, email);
        intent.putExtra(EXTRA_USER_ID, userId); // Novo: envia o ID
        startActivity(intent);
    }


    private void iniciarCadastro() {
        startActivity(new Intent(this, Cadastro.class));
    }

    private void mostrarToast(String mensagem) {
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }
}