package br.ufs.dcomp.ChatRabbitMQ.strategies;

import java.util.Map;

import com.rabbitmq.client.Channel;

import br.ufs.dcomp.ChatRabbitMQ.chat.Input;

public interface ActionStrategy {
	void run(Map<String,Channel> channels, Input input) throws Exception; // retorna a nova seta do chat >>
}