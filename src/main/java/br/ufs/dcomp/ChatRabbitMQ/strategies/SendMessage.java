package br.ufs.dcomp.ChatRabbitMQ.strategies;

import java.util.Map;

import com.rabbitmq.client.Channel;

import br.ufs.dcomp.ChatRabbitMQ.chat.Input;
import br.ufs.dcomp.ChatRabbitMQ.chat.Symbols;
import br.ufs.dcomp.ChatRabbitMQ.date.FormattedDate;
import br.ufs.dcomp.ChatRabbitMQ.proto.MensagemProto;
import br.ufs.dcomp.ChatRabbitMQ.util.PROTO;

public class SendMessage implements ActionStrategy {

  private String currentQueue = "";
  private String currentExchange = "";

  public SendMessage(String currentQueue, String currentExchange) {
    this.currentQueue = currentQueue;
    this.currentExchange = currentExchange;
  }

  @Override
  public void run(Map<String, Channel> channels, Input input) throws Exception {
    String username = input.getSource();
    if (input.getPrompt().startsWith(Symbols.GROUP)) {
      currentQueue = currentQueue + ".mensagens";
    }

    FormattedDate date = new FormattedDate();
    MensagemProto.Conteudo conteudo = PROTO.createConteudoProto(
      "text/plain",
      input.getInput().getBytes("UTF-8"),
      ""
    ); // mensagens
    byte[] mensagemProto = PROTO.createMensagemProto(
      username,
      date.getDay(),
      date.getHour(),
      currentExchange,
      conteudo
    );

    channels.get("mensagens").basicPublish(currentExchange, currentQueue, null, mensagemProto);
  }
}