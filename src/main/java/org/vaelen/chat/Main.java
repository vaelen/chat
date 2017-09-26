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

public class Main {
    public static void main(String[] args) {
        DataLayer data = new DataLayer("mongodb://localhost");

        User andrew = data.users().findByEmail("andrew@vaelen.org");
        andrew.setName("Andrew");
        andrew.seen();
        data.users().save(andrew);

        Channel channel = data.channels().findByName("testing");
        channel.setOwner(andrew);
        channel.setDescription("A test channel");
        data.channels().save(channel);

        User 田中 = data.users().findByEmail("tanaka@example.co.jp");
        田中.setName("田中");
        田中.seen();
        data.users().save(田中);

        Location austin = new Location(-97.74035, 30.274665);
        Location 東京 = new Location(-97.74035, 30.274665);

        data.messages().save(createMessage(andrew, austin, "This is a test message."));
        data.messages().save(createMessage(田中, 東京, "これはテストです。"));

        data.messages().findByChannel("testing").forEach(message -> System.out.println(message));
    }

    private static Message createMessage(User user, Location location, String content) {
        Message message = new Message();
        message.setChannel("testing");
        message.setUser(user);
        message.setContent(content);
        message.setLocation(location);
        return message;
    }
}
