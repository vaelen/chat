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
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.UpdateOptions;

import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

@SuppressWarnings("WeakerAccess")
public class UserController implements Streamable<User> {

    private final MongoCollection<User> users;

    public UserController(MongoDatabase db) {
        this.users = db.getCollection("users", User.class);
    }

    public User findByEmail(String emailAddress) {
        Optional<User> user = stream(users.find(eq("email", emailAddress)).limit(1))
                .findFirst();

        return user.orElseGet(() -> {
            User u = new User(emailAddress);
            save(u);
            return u;
        });
    }

    public void save(User user) {
        users.replaceOne(eq("_id", user.getId()), user,
                new UpdateOptions().upsert(true));
    }

    public void createIndexes() {
        users.createIndex(Indexes.ascending("email"), new IndexOptions().unique(true).background(true));
        users.createIndex(Indexes.geo2dsphere("location"), new IndexOptions().background(true));
        users.createIndex(Indexes.descending("lastSeen"), new IndexOptions().background(true));
    }
}
