package com.example.idalanstudio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class InformacoesActivity extends AppCompatActivity {
   //Uma Activity destinada a contatos e informações não interativas
    private Button btnReturnToMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_informacoes);

        btnReturnToMenu = findViewById(R.id.btnReturnToMenu);

        //apenas retorna a MainActivity
        btnReturnToMenu.setOnClickListener(v -> {
            finish(); // Volta para a tela anterior
        });
    }

}