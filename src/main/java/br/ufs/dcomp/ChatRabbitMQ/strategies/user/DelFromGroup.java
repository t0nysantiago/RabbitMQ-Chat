package br.ufs.dcomp.ChatRabbitMQ.strategies.user;

import java.util.Map;

import com.rabbitmq.client.Channel;

import br.ufs.dcomp.ChatRabbitMQ.chat.Input;
import br.ufs.dcomp.ChatRabbitMQ.strategies.ActionStrategy;

public class DelFromGroup implements ActionStrategy {
	
	@Override
	public void run(Map<String, Channel> channels, Input input) throws Exception{
		try {
			String user = input.getArgs(0);
			try {
				String group = input.getArgs(1);			
				channels.get("mensagens").queueUnbind(user, group, "");
				channels.get("arquivos").queueUnbind(user+".arquivos", group, "");
			}
			catch(Exception e) {
				System.out.println("Error");
			}
		} catch(Exception e) {
			System.out.println("Error");
		}
	}
 

}