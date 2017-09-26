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

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;

import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

@SuppressWarnings("WeakerAccess")
public class ChannelController implements Streamable<Channel> {

    private final MongoCollection<Channel> channels;

    public ChannelController(MongoDatabase db) {
        this.channels = db.getCollection("channels", Channel.class);
    }

    public Channel findByName(final String name) {
        Optional<Channel> channel = stream(channels.find(eq("_id", name))
                .limit(1)).findFirst();

        return channel.orElseGet(() -> {
            Channel c = new Channel(name);
            save(c);
            return c;
        });
    }

    public void save(final Channel channel) {
        channels.replaceOne(eq("_id", channel.getName()), channel,
                new UpdateOptions().upsert(true));
    }

    @SuppressWarnings("EmptyMethod")
    public void createIndexes() {
    }
}
