package com.github.masahitojp.botan.handlers;

import com.github.masahitojp.botan.Botan;
import com.github.masahitojp.botan.adapter.MockAdapter;
import com.github.masahitojp.botan.exception.BotanException;
import com.github.masahitojp.botan.message.BotanMessageSimple;
import com.github.masahitojp.botan.utils.BotanMessageMock;

import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(JMockit.class)
public class PingMessageHandlersTest {

    public Botan botan;

    @Before
    public void before() throws BotanException {
        botan = new Botan.BotanBuilder().setAdapter(new MockAdapter()).build();
        botan.start();
    }

    @Test
    public void pingpong() {
        final BotanMessageMock mock = new BotanMessageMock();
        botan.receive(new BotanMessageSimple(String.format("%s ping", botan.getName()), null, null, null, 0));
        assertThat(mock.getResult().toString(), is("pong"));
    }
}
