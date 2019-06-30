package br.com.b2w.starwars.api.dao;

import java.util.List;

public interface IDynamoDbDao<T> {
    void criarTabela();
    T buscarPorId(String id);
    void deletarPorId(String id);
    void incluir(T objeto);
    List<T> retornarTodos();
    T buscarPorNome(String nome);
    void deletarTabela();
}
