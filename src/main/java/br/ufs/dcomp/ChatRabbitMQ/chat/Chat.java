package br.ufs.dcomp.ChatRabbitMQ.chat;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;


import br.ufs.dcomp.ChatRabbitMQ.Main;
import br.ufs.dcomp.ChatRabbitMQ.util.PROTO;
import br.ufs.dcomp.ChatRabbitMQ.strategies.ActionStrategy;

public class Chat {

	private String username; // obs: o nome do usuario User: eh o nome da fila queue
	private String host;
	private String name;
	private String password;

	private static Map<String, Channel> channels = new HashMap<>();
	private static Connection connection;

	public static Map<String, Channel> getChannels() {
		return channels;
	}

	public static Connection getConnection() {
		return connection;
	}

	public Chat(String username) {
		this.username = username;
		this.host = "100.25.215.65";
		this.name = "tony";
		this.password = "123456";
	}

	public void channelSetup(String usr) throws IOException, Exception {
		if (connection == null) {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(host); // IP da máquina virtual
			factory.setUsername(name);
			factory.setPassword(password);
			factory.setVirtualHost("/");
			connection = factory.newConnection();
		}
		// Cria dois canais
		channels.put("mensagens", connection.createChannel());
		channels.put("arquivos", connection.createChannel());

		// Cria duas filas em dois canais distintos para cada usuário
		declareQueues(channels, username);
	}
	
	public static void declareQueues(Map<String, Channel> channels, String queueName) throws IOException { 
		channels.get("mensagens").queueDeclare(queueName, false, false, false, null);
		channels.get("arquivos").queueDeclare(queueName + ".arquivos", false, false, false, null);
	}
	
	public void waitMessage() throws Exception {
		DeliverCallback consumerMessages = PROTO.constructCallback("mensagens", username);
		DeliverCallback consumerFiles = PROTO.constructCallback("arquivos", username);
		channels.get("mensagens").basicConsume(username, true, consumerMessages, consumerTag -> {
		}); // a fila tem o mesmo nome do username
		channels.get("arquivos").basicConsume(username + ".arquivos", true, consumerFiles, consumerTag -> {
		}); // a fila tem o mesmo nome do username
	}

	public void execute(ActionStrategy strategy, String arrow, String input) throws Exception {
		strategy.run(channels, new Input(arrow, input, username));
	}
}
