package br.ufs.dcomp.ChatRabbitMQ.strategies.group;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.rabbitmq.client.Channel;

import br.ufs.dcomp.ChatRabbitMQ.chat.Input;
import br.ufs.dcomp.ChatRabbitMQ.strategies.ActionStrategy;

public class ListUsersFromGroup extends AbstractListInfo implements ActionStrategy {
	@Override
	public void run(Map<String, Channel> channels, Input input) throws Exception {
		try {
			String exchange = input.getArgs(0);
			String restResource = "http://100.25.215.65:15672";
			String path = "/api/exchanges/%2f/" + exchange + "/bindings/source";
			String json = getJsonFromPath(restResource, path);
			action(json);
		} catch(Exception e) {
			System.out.println("Error");
		}
	}

	@Override
	public void action(String json) {
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(json);
			JSONArray users = (JSONArray) obj;
			List<String> listUsers = new ArrayList<>();
			for (int i = 0; i < users.size(); i++) {
				JSONObject user = (JSONObject) users.get(i);
				if (!(user.get("destination").toString().contains("arquivos"))) {
					listUsers.add((String) user.get("destination"));
				}
			}
			System.out.println(String.join(",", listUsers));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}