package br.ufs.dcomp.ChatRabbitMQ.strategies.group;

import java.util.Map;

import com.rabbitmq.client.Channel;

import br.ufs.dcomp.ChatRabbitMQ.chat.Input;
import br.ufs.dcomp.ChatRabbitMQ.strategies.ActionStrategy;

public class RemoveGroup implements ActionStrategy {
	
	@Override
	public void run(Map<String, Channel> channels, Input input) throws Exception{
		try {
			channels.get("mensagens").exchangeDelete(input.getArgs(0));
			channels.get("arquivos").exchangeDelete(input.getArgs(0));
		}
		catch(Exception e) {
			System.out.println("Error");
		}
	}

}