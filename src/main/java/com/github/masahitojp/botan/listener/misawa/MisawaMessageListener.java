package com.github.masahitojp.botan.listener.misawa;

import com.github.masahitojp.botan.exception.BotanException;
import com.github.masahitojp.botan.listener.BotanMessageListenerRegister;
import com.github.masahitojp.botan.utils.BotanUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class MisawaMessageListener implements BotanMessageListenerRegister {

    @Override
    public void register() {
        BotanUtils.respond(
                "misawa( +(.*))?",
                "misawa image",
                messge -> {
                    final OkHttpClient client = new OkHttpClient();
                    final String url = "http://horesase.github.io/horesase-boys/meigens.json";
                    final Request request = new Request.Builder()
                                .url(url)
                                .build();

                    final Response response;
                    try {
                        response = client.newCall(request).execute();
                        final String src = response.body().string();
                        Gson gson = new Gson();
                        Type collectionType = new TypeToken<Collection<Meigen>>(){}.getType();

                        List<Meigen> rootAsMap =  gson.fromJson(src, collectionType);
                        Random r = new Random();
                        int i = r.nextInt(rootAsMap.size());
                        messge.reply(rootAsMap.get(i).image);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (BotanException e) {
                        e.printStackTrace();
                    }
                }
        );
        BotanUtils.beforeShutdown(() -> {
            System.out.println("shutdown");
            return true;
        });
    }
}
