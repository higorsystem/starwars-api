package br.com.b2w.starwars.api.dao;

import java.util.List;

public interface IDynamoDbDao<T> {
    void criarTabela();
    T buscarPorId(int id);
    void deletarPorId(int id);
    void incluir(T objeto);
    List<T> retornarTodos();
    void deletarTabela();
}
