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
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;

import java.util.stream.Stream;

public class MessageController implements Streamable<Message> {

    private MongoDatabase db;
    private MongoCollection<Message> messages;

    public MessageController(MongoClient client) {
        this.db = client.getDatabase("chat");
        this.messages = db.getCollection("messages", Message.class);
    }

    public Stream<Message> findByChannel(String channel) {
        return stream(messages.find(Filters.eq("channel", channel)));
    }

    public void save(Message message) {
        messages.replaceOne(Filters.eq("_id", message.getId()), message,
                new UpdateOptions().upsert(true));
    }

}
