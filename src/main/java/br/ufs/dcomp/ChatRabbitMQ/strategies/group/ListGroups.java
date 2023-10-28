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

public class ListGroups extends AbstractListInfo implements ActionStrategy {

	@Override
	public void run(Map<String, Channel> channels, Input input) throws Exception {
		try {
			String username = input.getSource();
			
			String restResource = "http://100.25.215.65:15672";
			String path = "/api/queues/%2f/" +  username + "/bindings";
			String json = getJsonFromPath(restResource, path);
			action(json);
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void action(String json) {
		try {
			JSONParser parser = new JSONParser();
            JSONArray groups = (JSONArray) parser.parse(json);
			List<String> listGroups = new ArrayList<>();
			for (int i = 0; i < groups.size(); i++) {
				JSONObject group = (JSONObject) groups.get(i);
				if (!group.get("source").toString().isEmpty()) {
					listGroups.add((String) group.get("source"));
				}
			}
			System.out.println(String.join(", ", listGroups));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}