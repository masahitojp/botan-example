package com.github.masahitojp.botan;

import com.github.masahitojp.botan.adapter.SlackAdapter;
import com.github.masahitojp.botan.exception.BotanException;

import java.util.Optional;

public class SlackBot {

    static public String envToOpt(final String envName) {
        Optional<String> javaDirectory = Optional.ofNullable(System.getenv(envName));
        return javaDirectory.orElse("");
    }
	static public void main(String[] Args) {

        final String team = envToOpt("SLACK_TEAM");
        final String user = envToOpt("SLACK_USERNAME");
        final String pswd = envToOpt("SLACK_PASSWORD");
        final String room = envToOpt("SLACK_ROOM");

        final Botan botan = new Botan.BotanBuilder(new SlackAdapter(team, user, pswd, room))
                .build();

        try {
            botan.start();
        } catch (final BotanException ex) {
            ex.printStackTrace();
        }

	}
}
