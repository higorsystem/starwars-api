package br.com.b2w.starwars.api.dao.impl;

import br.com.b2w.starwars.api.StarwarsConfiguration;
import br.com.b2w.starwars.api.dao.IDynamoDbDao;
import br.com.b2w.starwars.api.model.Planeta;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DynamoDbPlanetaDao implements IDynamoDbDao<Planeta> {
    private DynamoDB dynamoDB;
    private String tableName = "Planeta";

    private AmazonDynamoDB client;
    private DynamoDBMapper mapper;

    public DynamoDbPlanetaDao(StarwarsConfiguration configuration) {
        client = AmazonDynamoDBClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2"))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(configuration.getAccessKey(), configuration.getSecretKey())))
                .build();
        dynamoDB = new DynamoDB(client);
        mapper = new DynamoDBMapper(client);
    }

    @Override
    public void criarTabela() {
        var existeTabela = client.listTables();

        if(!existeTabela.getTableNames().isEmpty()){
            return;
        }
        // Criando a tabela Planeta.
        List<AttributeDefinition> attributeDefinitions = new ArrayList<>();
        attributeDefinitions.add(new AttributeDefinition().withAttributeName("Id").withAttributeType("S"));

        List<KeySchemaElement> keySchema = new ArrayList<KeySchemaElement>();
        keySchema.add(new KeySchemaElement().withAttributeName("Id").withKeyType(KeyType.HASH)); // Partition key

        CreateTableRequest request = new CreateTableRequest().withTableName(tableName).withKeySchema(keySchema)
                .withAttributeDefinitions(attributeDefinitions).withProvisionedThroughput(
                        new ProvisionedThroughput().withReadCapacityUnits(5L).withWriteCapacityUnits(6L));

        try {
            System.out.println("Criando tabela " + tableName + "...");
            Table table = dynamoDB.createTable(request);

            System.out.println("Aguarde a criação da tabela " + tableName + ", carregando...");
            table.waitForActive();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void deletarTabela() {
        Table table = dynamoDB.getTable(tableName);
        try {
            System.out.println("Emitindo a solicitação DeleteTable para " + tableName);
            table.delete();
            System.out.println("Aguardando por" + tableName
                    + " para ser apagado ... isso pode demorar um pouco ...");
            table.waitForDelete();

        } catch (Exception e) {
            System.err.println("Falha na solicitação DeleteTable para " + tableName);
            System.err.println(e.getMessage());
        }
    }

    @Override
    public Planeta buscarPorId(String id) {
        return mapper.load(Planeta.class, id);
    }

    @Override
    public void deletarPorId(String id) {
        DynamoDBMapperConfig config = new DynamoDBMapperConfig(DynamoDBMapperConfig.ConsistentReads.CONSISTENT);
        Planeta deleteItem = mapper.load(Planeta.class, id, config);
        mapper.delete(deleteItem);
    }

    @Override
    public void incluir(Planeta objeto) {
        mapper.save(objeto);
    }

    @Override
    public List<Planeta> retornarTodos() {
        return mapper.scan(Planeta.class, new DynamoDBScanExpression());
    }

    @Override
    public Planeta buscarPorNome(String nome) {
        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":nome", new AttributeValue().withS(nome));

        ScanRequest scanRequest = new ScanRequest()
                .withTableName(tableName)
                .withFilterExpression("Nome = :nome")
                .withExpressionAttributeValues(expressionAttributeValues)
                .withProjectionExpression("Id, Nome, Clima, Terreno, QuantidadeAparicoes")
                .withLimit(10);
        var scan = client.scan(scanRequest);
        var entidade = scan.getItems().stream().map(DynamoDbPlanetaDao::map).collect(Collectors.toList());

        if (entidade.size() == 0)
            return null;

        return entidade.get(0);
    }

    private static Planeta map(Map<String, AttributeValue> resultado){
        var entidade = new Planeta(
                resultado.get("Id").getS(),
                resultado.get("Nome").getS(),
                resultado.get("Clima").getS(),
                resultado.get("Terreno").getS(),
               Integer.parseInt(resultado.get("QuantidadeAparicoes").getN()));

        return entidade;
    }
}
