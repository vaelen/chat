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
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Supplier;

public class ChannelController implements Streamable<Channel> {

    private MongoDatabase db;
    private MongoCollection<Channel> channels;

    public ChannelController(MongoClient client) {
        this.db = client.getDatabase("chat");
        this.channels = db.getCollection("channels", Channel.class);
    }

    public Channel findByName(final String name) {
        Optional<Channel> channel = stream(channels.aggregate(Arrays.asList(
                        Aggregates.match(Filters.eq("_id", name)),
                        Aggregates.lookup("users", "ownerId", "_id", "owner"),
                        Aggregates.limit(1)
        ))).findFirst();

        Supplier<Channel> newChannel = () -> {
            Channel c = new Channel(name);
            save(c);
            return c;
        };

        return channel.orElse(newChannel.get());
    }

    public void save(final Channel channel) {
        channels.replaceOne(Filters.eq("_id", channel.getName()), channel,
                new UpdateOptions().upsert(true));
    }

}
