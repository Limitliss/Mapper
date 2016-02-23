package io.unearthing.mapper;

import android.content.Context;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;

/**
 * Created by billybonks on 23/2/16.
 */
public class CloudentBuilder {

    public static CloudantClient buildClient(Context context){
        String username = context.getString(R.string.cloudant_username);
        String password = context.getString(R.string.cloudant_password);
        String account =  context.getString(R.string.cloudant_host);
        return ClientBuilder.account(account)
                .username(username)
                .password(password)
                .build();
    }

    public static Database getDatabase(Context context,String name) {
        return CloudentBuilder.buildClient(context).database(name, false);
    }

    public static Database getDatabase(Context context) {
        return CloudentBuilder.getDatabase(context, context.getString(R.string.cloudant_database));
    }
}
