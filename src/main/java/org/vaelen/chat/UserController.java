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

import java.util.Optional;
import java.util.function.Supplier;

public class UserController implements Streamable<User> {

    private MongoDatabase db;
    private MongoCollection<User> users;

    public UserController(MongoClient client) {
        this.db = client.getDatabase("chat");
        this.users = db.getCollection("users", User.class);
    }

    public User findByEmail(String emailAddress) {
        Optional<User> user = stream(users.find(Filters.eq("email", emailAddress)).limit(1))
                .findFirst();

        Supplier<User> newChannel = () -> {
            User u = new User(emailAddress);
            save(u);
            return u;
        };

        return user.orElse(newChannel.get());
    }

    public void save(User user) {
        users.replaceOne(Filters.eq("_id", user.getId()), user,
                new UpdateOptions().upsert(true));
    }

}
