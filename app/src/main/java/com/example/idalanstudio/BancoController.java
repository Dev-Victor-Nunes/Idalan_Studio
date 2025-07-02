package com.example.idalanstudio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BancoController {
    private final CriaBanco banco;
    private SQLiteDatabase db;

    public BancoController(Context context) {
        banco = new CriaBanco(context);
    }

    public String insereDados(String nome, String email) {
        SQLiteDatabase db = banco.getWritableDatabase();
        try {
            ContentValues valores = new ContentValues();
            valores.put("nome", nome);
            valores.put("email", email);

            long resultado = db.insert("usuarios", null, valores);
            return resultado == -1 ? "Erro ao inserir" : "Sucesso";
        } finally {
            db.close();
        }
    }

    public Cursor carregarUsuario(int id) {
        return banco.getReadableDatabase().query(
                "usuarios",
                new String[]{"codigo", "telefone", "email"},
                "codigo = ?",
                new String[]{String.valueOf(id)},
                null, null, null
        );
    }

    public String alteraDados(int id, String telefone, String email, String senha) {

        String msg = "Dados alterados com sucesso!!!";

        db = banco.getReadableDatabase();

        ContentValues valores = new ContentValues();
        valores.put("telefone", telefone);
        valores.put("email", email);
        valores.put("senha", senha);

        String condicao = "codigo = " + id;

        int linha;
        linha = db.update("usuarios", valores, condicao, null);

        if (linha < 1) {
            msg = "Erro ao alterar os dados";
        }

        db.close();
        return msg;
    }

    public String excluirDados(int id) {
        String msg = "Registro Excluído";

        db = banco.getReadableDatabase();

        String condicao = "codigo = " + id;

        int linhas;
        linhas = db.delete("usuarios", condicao, null);

        if (linhas < 1) {
            msg = "Erro ao Excluir";
        }

        db.close();
        return msg;
    }
}
