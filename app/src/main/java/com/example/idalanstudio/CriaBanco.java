package com.example.idalanstudio;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CriaBanco extends SQLiteOpenHelper {
    private static final String NOME_BANCO = "idalan_studio.db";
    private static final int VERSAO = 8;

    public CriaBanco(Context context) {
        super(context, NOME_BANCO, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE usuarios ("
                + "codigo integer primary key autoincrement,"
                + "nome text,"
                + "cpf  text,"
                + "email text,"
                + "telefone text,"
                + "senha text)";
        db.execSQL(sql);

        sql = "CREATE TABLE agendamento ("
                + "idAgendamento integer primary key autoincrement, "
                + "data text, "
                + "hora text, "
                + "email text, "
                + "servico text, "
                + "valor real, "
                + "status text)";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS servicos");
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        db.execSQL("DROP TABLE IF EXISTS agendamento");
        onCreate(db);
    }
}