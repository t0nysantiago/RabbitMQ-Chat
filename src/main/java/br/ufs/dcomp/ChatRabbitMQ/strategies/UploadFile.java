package br.ufs.dcomp.ChatRabbitMQ.strategies;

import java.util.Map;

import com.rabbitmq.client.Channel;

import br.ufs.dcomp.ChatRabbitMQ.chat.Input;
import br.ufs.dcomp.ChatRabbitMQ.util.PROTO;

public class UploadFile implements ActionStrategy {
	private String currentQueue="";
	private String currentExchange="";

	public UploadFile(String currentQueue, String currentExchange) {
		this.currentQueue = currentQueue;
		this.currentExchange = currentExchange;
	}
	
	@Override
	public void run(Map<String, Channel> channels, Input input) throws Exception {			
		String username = input.getSource();	
		String recipient = !input.getName().equals("") ? input.getName() : username;
		String queue = recipient + ".arquivos";
		new Thread() {
			@Override
			public void run() {
				try {
					String filename = input.getArgs(0);
					try {		
						byte[] msgProto = PROTO.fileBytes(filename, username, currentExchange);

						//sleep(10*1000); // atraso proposital
						
						channels.get("arquivos").basicPublish(currentExchange, queue, null, msgProto);
						System.out.println("\nArquivo \"" + filename + "\" foi enviado para "+ input.getPromptSymbol() + recipient + "!");
						System.out.print(input.getPrompt());

					} catch (Exception e) {
						System.out.println("Error");
					}
				} catch(Exception e) {
					System.out.println("Error");
				}
				
			}
		}.start();
	}
}