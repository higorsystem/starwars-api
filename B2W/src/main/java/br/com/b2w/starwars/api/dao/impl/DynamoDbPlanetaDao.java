package br.com.b2w.starwars.api.dao.impl;

import br.com.b2w.starwars.api.StarwarsConfiguration;
import br.com.b2w.starwars.api.dao.IDynamoDbDao;
import br.com.b2w.starwars.api.model.Planeta;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.*;

import java.util.ArrayList;
import java.util.List;

public class DynamoDbPlanetaDao implements IDynamoDbDao<Planeta> {
    static DynamoDB dynamoDB;
    private StarwarsConfiguration configuration;
    final String tableName = "Planeta";

    private AmazonDynamoDB client;
    private DynamoDBMapper mapper;

    public DynamoDbPlanetaDao(StarwarsConfiguration configuration) {
        this.configuration = configuration;
        client = AmazonDynamoDBClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2"))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(this.configuration.getAccessKey(), this.configuration.getSecretKey())))
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
        List<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
        attributeDefinitions.add(new AttributeDefinition().withAttributeName("Id").withAttributeType("N"));

        List<KeySchemaElement> keySchema = new ArrayList<KeySchemaElement>();
        keySchema.add(new KeySchemaElement().withAttributeName("Id").withKeyType(KeyType.HASH)); // Partition        // key

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
            System.out.println("Issuing DeleteTable request for " + tableName);
            table.delete();
            System.out.println("Waiting for " + tableName
                    + " to be deleted...this may take a while...");
            table.waitForDelete();

        } catch (Exception e) {
            System.err.println("DeleteTable request failed for " + tableName);
            System.err.println(e.getMessage());
        }
    }

    @Override
    public Planeta buscarPorId(int id) {
        return mapper.load(Planeta.class, id);
    }

    @Override
    public void deletarPorId(int id) {
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
}
