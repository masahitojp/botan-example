package com.github.masahitojp.botan.handlers;

import com.github.masahitojp.botan.Botan;
import com.github.masahitojp.botan.adapter.MockAdapter;
import com.github.masahitojp.botan.brain.LocalBrain;
import com.github.masahitojp.botan.exception.BotanException;
import com.github.masahitojp.botan.message.BotanMessage;
import com.github.masahitojp.botan.message.BotanMessageSimple;

import mockit.Mock;
import mockit.MockUp;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

public class EchoMessageHandlersTest {
    Botan botan;
    @Before
    public void startUp() throws BotanException {
        botan = new Botan.BotanBuilder()
                .setAdapter(new MockAdapter())
                .setBrain(new LocalBrain())
                .setMessageHandlers(new EchoMessageHandlers())
                .build();
        botan.start();
    }

    @After
    public void tearDown() {
        botan.stop();
    }

    @Test
    public void handlersRegistrationTest() {
        assertThat(botan.getHandlers().size(), is(1));
    }
    @Test
    public void regexTest() {
        final AtomicInteger times = new AtomicInteger();
        MockUp<Consumer<BotanMessage>> spy = new MockUp<Consumer<BotanMessage>>(){
            @Mock
            public void accept(BotanMessage message) {
                times.incrementAndGet();
            }
        };
        botan.getHandlers()
                .stream()
                .filter(handler -> handler.getDescription().equals("echo your message"))
                .forEach(handler -> handler.setHandle(spy.getMockInstance()));
        botan.receive(new BotanMessageSimple("botan echo aaa"));
        assertThat(times.get(), is(1));
    }
    @Test
    public void MessageReplyTest() {
        final AtomicReference<String> replayMessage = new AtomicReference<>();
        new MockUp<BotanMessage>(){
            @Mock
            public void reply(String message) {
                replayMessage.set(message);
            }
        };
        botan.receive(new BotanMessageSimple("botan echo aaa"));
        assertThat(replayMessage.get(), is("aaa"));
    }
}
