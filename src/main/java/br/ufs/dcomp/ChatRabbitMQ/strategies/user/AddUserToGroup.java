package br.ufs.dcomp.ChatRabbitMQ.strategies.user;

import java.util.Map;

import com.rabbitmq.client.Channel;

import br.ufs.dcomp.ChatRabbitMQ.chat.Input;
import br.ufs.dcomp.ChatRabbitMQ.chat.Chat;
import br.ufs.dcomp.ChatRabbitMQ.strategies.ActionStrategy;

public class AddUserToGroup implements ActionStrategy {

	@Override
	public void run(Map<String, Channel> channels, Input input) throws Exception {
		try {
			String userQueue = input.getArgs(0);
			try {
				String exchange = input.getArgs(1);

				// Se a fila não for criada e fizer o bind, ele fecha o canal e tem que reiniciar o programa
				Chat.declareQueues(channels, userQueue);
				//
				channels.get("mensagens").queueBind(userQueue, exchange, "*.mensagens"); // deve ter pelo menos um ponto na string (RabbitMQ)
				channels.get("arquivos").queueBind(userQueue+".arquivos", exchange, "*.arquivos"); //  deve ter pelo menos um ponto na string (RabbitMQ)
			} catch(Exception e) { 
				System.out.println("Error");
			}
		} catch(Exception e) {
			System.out.println("Error");
		}
	}
}