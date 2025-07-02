package com.example.idalanstudio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BancoControllerUsuarios {
    private SQLiteDatabase db;
    private final CriaBanco banco;

    public BancoControllerUsuarios(Context context) {
        banco = new CriaBanco(context);
    }

    public int getLastInsertedId() {
        db = banco.getReadableDatabase();

        //select de dados da ultima Activity
        Cursor cursor = db.rawQuery("SELECT last_insert_rowid()", null);
        int id = -1;
        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return id;
    }

    public String insereDados(String _nome, String _cpf, String _telefone, String _email, String _senha) {
        ContentValues valores;
        long resultado;
        db = banco.getWritableDatabase();

        valores = new ContentValues();
        valores.put("nome", _nome);
        valores.put("cpf", _cpf);
        valores.put("email", _email);
        valores.put("telefone", _telefone);
        valores.put("senha", _senha);

        resultado = db.insert("usuarios", null, valores);
        db.close();

        if (resultado == -1)
            return "Erro ao efetuar o Cadastre-se";
        else
            return "Cadastro efetuado com sucesso";
    }


    public Cursor ConsultaDadosLogin(String _email, String _senha) {
        db = banco.getReadableDatabase();
        String[] campos = {"codigo", "nome", "cpf", "email", "telefone", "senha"};
        String where = "email = ? AND senha = ?";
        String[] whereArgs = {_email, _senha};

        return db.query("usuarios", campos, where, whereArgs, null, null, null);
    }


    public Cursor ConsultaPorEmail(String _email) {
        Cursor cursor;
        String[] campos = {"codigo", "nome", "cpf", "email", "telefone", "senha"};
        String where = "email = ?";
        String[] whereArgs = {_email};
        db = banco.getReadableDatabase();

        cursor = db.query("usuarios", campos, where, whereArgs, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }

    // Refinamento com ID para facilitar a busca dos demais dados
    public Cursor ConsultaPorId(int userId) {
        Cursor cursor;
        String[] campos = {"codigo", "nome", "cpf", "email", "telefone", "senha"};
        String where = "codigo = ?";
        String[] whereArgs = {String.valueOf(userId)};
        db = banco.getReadableDatabase();

        cursor = db.query("usuarios", campos, where, whereArgs, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }

}