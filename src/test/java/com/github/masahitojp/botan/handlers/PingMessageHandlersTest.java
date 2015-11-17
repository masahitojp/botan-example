package com.github.masahitojp.botan.handlers;

import com.github.masahitojp.botan.Botan;
import com.github.masahitojp.botan.adapter.MockAdapter;
import com.github.masahitojp.botan.brain.LocalBrain;
import com.github.masahitojp.botan.exception.BotanException;
import com.github.masahitojp.botan.message.BotanMessage;
import com.github.masahitojp.botan.message.BotanMessageSimple;

import com.github.masahitojp.botan.utils.RegexTestPattern;
import mockit.Mock;
import mockit.MockUp;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

public class PingMessageHandlersTest {
    Botan botan;
    @Before
    public void startUp() throws BotanException {
        botan = new Botan.BotanBuilder()
                .setAdapter(new MockAdapter())
                .setBrain(new LocalBrain())
                .setMessageHandlers(new PingMessageHandlers())
                .build();
        botan.start();
    }

    @After
    public void after() {
        botan.stop();
    }

    @Test
    public void handlersRegistrationTest() {
        assertThat(botan.getHandlers().size(), is(1));
    }
    @Test
    public void regexTest() {
        Collections.singletonList(new RegexTestPattern(PingMessageHandlers.DESCRIPTION, "botan ping", 1))
                .stream()
                .forEach(pattern -> {
                    final AtomicInteger times = new AtomicInteger();
                    MockUp<Consumer<BotanMessage>> spy = new MockUp<Consumer<BotanMessage>>() {
                        @Mock
                        @SuppressWarnings("unused")
                        public void accept(BotanMessage message) {
                            times.incrementAndGet();
                        }
                    };
                    botan.getHandlers()
                            .stream()
                            .filter(handler -> handler.getDescription().equals(pattern.getDescription()))
                            .forEach(handler -> handler.setHandle(spy.getMockInstance()));

                    botan.receive(new BotanMessageSimple(pattern.getMessage()));
                    assertThat(times.get(), is(pattern.getTimes()));
                });
    }
    @Test
    public void MessageReplyTest() {
        final AtomicReference<String> replayMessage = new AtomicReference<>();
        new MockUp<BotanMessage>(){
            @Mock
            @SuppressWarnings("unused")
            public void reply(String message) {
                replayMessage.set(message);
            }
        };
        botan.receive(new BotanMessageSimple("botan ping"));
        assertThat(replayMessage.get(), is("pong"));
    }
}
