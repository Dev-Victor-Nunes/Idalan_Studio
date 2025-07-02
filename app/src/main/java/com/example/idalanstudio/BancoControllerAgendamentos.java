package com.example.idalanstudio;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BancoControllerAgendamentos {
    private SQLiteDatabase db;
    private CriaBanco banco;

    public BancoControllerAgendamentos(Context context) {
        banco = new CriaBanco(context);
    }

    // Insere dados na tabela de Agendamento
    public String insereAgendamento(Agendamento agendamento) {
        try {
            db = banco.getWritableDatabase();

            ContentValues valores = new ContentValues();
            valores.put("data", agendamento.getData());
            valores.put("hora", agendamento.getHora());
            valores.put("email", agendamento.getEmail());
            valores.put("servico", agendamento.getServico());
            valores.put("valor", agendamento.getValor());
            valores.put("status", agendamento.getStatus());

            long resultado = db.insert("agendamento", null, valores);

            return resultado == -1 ? "Erro ao agendar horário" : "Agendamento realizado com sucesso!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro no banco de dados";
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    @SuppressLint({"Range", "Range"})
    public List<Agendamento> getAgendamentosPorUsuario(String email) {
        List<Agendamento> agendamentos = new ArrayList<>();
        Cursor cursor = null;

        try {
            db = banco.getReadableDatabase();

            String[] campos = {
                    "idAgendamento",
                    "data",
                    "hora",
                    "email",
                    "servico",
                    "valor",
                    "status"
            };

            // select de agendamentos pendentes
            String selection = "email = ? AND status != ?";
            String[] selectionArgs = {email, "Cancelado"};
            String orderBy = "data ASC, hora ASC";

            cursor = db.query(
                    "agendamento",
                    campos,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    orderBy
            );

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Agendamento agendamento = new Agendamento();
                    agendamento.setIdAgendamento(cursor.getInt(cursor.getColumnIndex("idAgendamento")));
                    agendamento.setData(cursor.getString(cursor.getColumnIndex("data")));
                    agendamento.setHora(cursor.getString(cursor.getColumnIndex("hora")));
                    agendamento.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                    agendamento.setServico(cursor.getString(cursor.getColumnIndex("servico")));
                    agendamento.setValor(cursor.getDouble(cursor.getColumnIndex("valor")));
                    agendamento.setStatus(cursor.getString(cursor.getColumnIndex("status")));

                    agendamentos.add(agendamento);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return agendamentos;
    }


    public boolean cancelarAgendamento(int idAgendamento) {
        Cursor c = null;
        try {
            db = banco.getWritableDatabase();

            //select apenas dos agendamentos salvos no banco
            c = db.rawQuery("SELECT * FROM agendamento WHERE idAgendamento = ?", new String[]{String.valueOf(idAgendamento)});
            if (c.moveToFirst()) {
                String statusAtual = c.getString(c.getColumnIndexOrThrow("status"));
                Log.d("DEBUG_CANCELAR", "Agendamento encontrado. Status atual: " + statusAtual);
            } else {
                Log.d("DEBUG_CANCELAR", "Agendamento NÃO encontrado no banco. ID: " + idAgendamento);
                return false;
            }

            ContentValues valores = new ContentValues();
            valores.put("status", "Cancelado");

            String whereClause = "idAgendamento = ?";
            String[] whereArgs = {String.valueOf(idAgendamento)};

            int rowsAffected = db.update("agendamento", valores, whereClause, whereArgs);
            Log.d("DEBUG_CANCELAR", "Linhas afetadas: " + rowsAffected);
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }
}


