package com.github.masahitojp.botan.listener;

import com.github.masahitojp.botan.Botan;
import com.github.masahitojp.botan.adapters.MockAdapter;
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
public class EchoMessageListenerTest {

    public Botan botan;

    @Before
    public void before() throws BotanException {
        botan = new Botan.BotanBuilder(new MockAdapter()).build();
        botan.start();
    }

    @Test
    public void echo() {
        final BotanMessageMock mock = new BotanMessageMock();
        botan.receive(new BotanMessageSimple(String.format("%s echo  週報 OR 勉強会　開始５分前です", botan.getName()), null, null, null, 0));
        assertThat(mock.getResult().toString(), is("週報 OR 勉強会　開始５分前です"));
    }
}
