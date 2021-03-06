/*
Chat, a simple chat application.
Copyright (C) 2017, Andrew Young <andrew@vaelen.org>

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.vaelen.chat;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.io.Closeable;
import java.io.IOException;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@SuppressWarnings("WeakerAccess")
public class DataLayer implements Closeable {
    private final MongoClient client;
    private final UserController userController;
    private final MessageController messageController;
    private final ChannelController channelController;

    public DataLayer(final String url) {
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        this.client = new MongoClient(new MongoClientURI(url,
                MongoClientOptions.builder().codecRegistry(pojoCodecRegistry)));
        MongoDatabase db = client.getDatabase("chat");
        this.userController = new UserController(db);
        this.messageController = new MessageController(db);
        this.channelController = new ChannelController(db);
        createIndexes();
    }

    public void createIndexes() {
        userController.createIndexes();
        messageController.createIndexes();
        channelController.createIndexes();
    }

    public UserController users() {
        return userController;
    }

    public MessageController messages() {
        return messageController;
    }

    public ChannelController channels() {
        return channelController;
    }

    @Override
    public void close() throws IOException {
        if (client != null) {
            client.close();
        }
    }
}
