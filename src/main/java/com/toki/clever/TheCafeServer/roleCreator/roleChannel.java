package com.toki.clever.TheCafeServer.roleCreator;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateNameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class roleChannel extends ListenerAdapter {

    private final String FILEPATH = "data/usersRoleData.json";

    private int notifCount;

    private enum State {
        NONE, ROLE_NAME, ROLE_COLOR, EDIT_NAME, EDIT_COLOR
    }

    private final HashMap<User, State> states = new HashMap<>();

    private final HashMap<Member, String> roleNames = new HashMap<>();
    private final HashMap<Member, Color> roleColors = new HashMap<>();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.isFromType(ChannelType.PRIVATE)) return;
        Channel channel = event.getChannel();
        TextChannel textChannel = event.getChannel().asTextChannel();
        if(!channel.getId().equals("1175605228863766558"))
            return;

        Member member = event.getMember();
        User user = event.getAuthor();
        String message = event.getMessage().getContentRaw();
        Guild guild = event.getGuild();

        if(user.isBot())
            return;

        this.states.putIfAbsent(user, State.NONE);
        State state = this.states.get(user);


        switch (state) {
            case NONE -> this.noState(guild, member, textChannel, message);
            case ROLE_NAME -> this.roleName(member, textChannel, message);
            case ROLE_COLOR -> this.roleColor(guild, member, textChannel, message);
            case EDIT_NAME -> this.editRoleName(guild, member, textChannel, message);
            case EDIT_COLOR -> this.editRoleColor(guild, member, textChannel, message);
        }

    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        JSONObject newSave = this.loadUserRole();
        String userName = event.getUser().getName();
        String roleName;
        try {
            roleName = newSave.getString(userName);
        }
        catch (org.json.JSONException e) {
            return;
        }
        List<Role> roles = event.getGuild().getRolesByName(roleName, true);
        Role role = roles.isEmpty() ? null : roles.get(0);
        if(role != null)
            role.getManager().getRole().delete().queue();
        newSave.remove(userName);
        this.saveUserRole(newSave);
    }

    @Override
    public void onUserUpdateName(UserUpdateNameEvent event) {
        JSONObject newSave = this.loadUserRole();
        String oldName = event.getOldName();
        String newName = event.getNewName();
        String roleName;
        try {
            roleName = newSave.getString(oldName);
        }
        catch (org.json.JSONException e) {
            return;
        }
        newSave.remove(oldName);
        newSave.put(newName, roleName);
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        String mention;
        try {
            mention = Objects.requireNonNull(event.getMember()).getAsMention();
        }
        catch(Exception e) {
            mention = Objects.requireNonNull(event.getMember()).getEffectiveName();
        }
        if(event.getComponentId().equals("button_name"))
        {
            event.reply(mention + " Type your new role name :kissing_heart:" +
                    "\nType cancel to prevent name change").setEphemeral(true).queue();
            this.states.replace(event.getUser(), State.EDIT_NAME);
        }
        else if(event.getComponentId().equals("button_color"))
        {
            event.reply(mention + " Type your new role color :smiling_face_with_3_hearts:" +
                    "\nType **cancel** to prevent color change" +
                    "\n:art: Type like the bold" +
                    "\nColor: **Red** Hex: **#FFD700** RGB: **255,182,193**").setEphemeral(true).queue();
            this.states.replace(event.getUser(), State.EDIT_COLOR);
        }
        else if(event.getComponentId().equals("button_delete"))
        {
            this.deleteRole(event.getGuild(), event.getMember());
            event.reply(mention + ", Your role has been deleted. :face_with_peeking_eye:" +
                    "\nType role to create a new one anytime").setEphemeral(true).queue();
        }
    }

    private void noState(Guild guild, Member member, TextChannel channel, String message)
    {
        if(message.equalsIgnoreCase("role")) {
            JSONObject roleObj = this.loadUserRole();

            if(!roleObj.has(member.getUser().getName())) {
                channel.sendMessage(member.getAsMention() +
                        ", Lets create your custom role!\n" +
                        "**What do you want your role to be called?** :blush:").queue();
                this.states.replace(member.getUser(), State.ROLE_NAME);
                return;
            }

            // Edit Role
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle(member.getEffectiveName() + "'s Custom Role");
            String roleName = roleObj.getString(member.getUser().getName());
            List<Role> roles = guild.getRolesByName(roleName, true);
            Role role = roles.isEmpty() ? null : roles.get(0);

            if(role != null && member.getRoles().contains(role)) {
                Color roleColor = role.getColor();
                String rgb = roleColor.getRed() + ", " + roleColor.getGreen() + ", " + roleColor.getBlue();
                embed.setDescription("Name: " + roleName + "    |    " + "Color: " + rgb);
                embed.setColor(roleColor);
            }
            else {
                JSONObject newSave = loadUserRole();
                newSave.remove(member.getUser().getName());
                this.saveUserRole(newSave);
                channel.sendMessage(member.getAsMention() + ", You don't have a role or it changed\nSo now you can add one. Type role").queue();
                return;
            }
            Button button1 = Button.secondary("button_name", "Edit Name");
            Button button2 = Button.secondary("button_color", "Edit Color");
            Button button3 = Button.danger("button_delete", "*' Delete '*");
            channel.sendMessageEmbeds(embed.build()).setActionRow(button1, button2, button3) // Add as many buttons as you want here
                    .queue();
        }
        else {
            if(notifCount == 5) {
                this.notifCount = 0;
                return;
            }
            JSONObject roleObj = this.loadUserRole();
            if(notifCount == 0) {
                if(this.getRole(guild, member, roleObj) != null)
                    channel.sendMessage(member.getAsMention() + ", To edit your role, say role").queue();
                else
                    channel.sendMessage(member.getAsMention() + ", For your role, say role").queue();
            }
            notifCount++;
        }
    }

    private void roleName(Member member, TextChannel channel, String message) {
        if(message.equalsIgnoreCase("cancel")) {
            channel.sendMessage(member.getAsMention() + ", You have successfully canceled the name change :pleading_face:").queue();
            this.states.replace(member.getUser(), State.NONE);
            return;
        }
        channel.sendMessage(member.getAsMention() +
                ", Your roles name is: \"" + message + "\"" +
                "\n:art: **Choose a role color!** Any Color :)" +
                "\nColor: **Green** Hex: **#87CEEB** RGB: **114,137,218**").queue();
        this.roleNames.put(member,message);
        this.states.replace(member.getUser(), State.ROLE_COLOR);
    }

    private void roleColor(Guild guild, Member member, TextChannel channel, String message) {
        if(message.equalsIgnoreCase("cancel")) {
            channel.sendMessage(member.getAsMention() + ", You have successfully canceled the color change :pleading_face:").queue();
            this.states.replace(member.getUser(), State.NONE);
            return;
        }
        try {
            Color color = this.getColor(message);
            if(color == null) {
                channel.sendMessage(member.getAsMention() + " " + message +
                        " is not a valid color.\nTry again or cancel :relaxed:").queue();
                return;
            }
            this.roleColors.put(member, color);
            this.createRole(guild, member);
            this.roleNames.remove(member);
            this.roleColors.remove(member);
            this.states.replace(member.getUser(), State.NONE);
            channel.sendMessage(member.getAsMention() + ", Your role has been created! :smiling_face_with_3_hearts:\n" +
                    "You can edit it anytime, just type role!").queue();
        }
        catch(Exception ignored) {
            channel.sendMessage(member.getAsMention() + ", That's an invalid color, try again :upside_down:").queue();
        }
    }

    private void createRole(Guild guild, Member member)
    {
        String name = this.roleNames.get(member);
        Color color = this.roleColors.get(member);
        guild.createRole()
                .setName(name) // Role Name
                .setColor(color) // Role Color
                .queue(role -> guild.modifyRolePositions()
                        .selectPosition(role)
                        .moveTo(4) // Depends on Clever role being in pos 3
                        .queue(success -> guild.addRoleToMember(member, role).queue(
                                addSuccess -> System.out.println(member.getEffectiveName() + " created a new role!"),
                                Throwable::printStackTrace // Handle failure in adding role
                        ), Throwable::printStackTrace), Throwable::printStackTrace);
        JSONObject newSave = this.loadUserRole();
        newSave.put(member.getUser().getName(), name);
        this.saveUserRole(newSave);
    }

    private void editRoleName(Guild guild, Member member, TextChannel textChannel, String message) {
        if(message.equalsIgnoreCase("cancel")) {
            textChannel.sendMessage(member.getAsMention() + ", You have successfully canceled the name change :pleading_face:").queue();
            this.states.replace(member.getUser(), State.NONE);
            return;
        }
        JSONObject roleObj = this.loadUserRole();
        Role role = this.getRole(guild, member, roleObj);
        role.getManager().setName(message).queue();
        textChannel.sendMessage(member.getAsMention() + ", Your new role name is " + message + " :yum:").queue();
        this.states.replace(member.getUser(), State.NONE);

        String name = member.getUser().getName();
        JSONObject newSave = this.loadUserRole();
        newSave.remove(name);
        newSave.put(name, message);
        this.saveUserRole(newSave);
    }

    private void editRoleColor(Guild guild, Member member, TextChannel textChannel, String message) {
        if(message.equalsIgnoreCase("cancel")) {
            textChannel.sendMessage(member.getAsMention() + ", You have successfully canceled the color change :pleading_face:").queue();
            this.states.replace(member.getUser(), State.NONE);
            return;
        }
        Color color = this.getColor(message);
        if(color == null) {
            textChannel.sendMessage(member.getAsMention() + " " + message +
                    " is not a valid color.\nTry again or cancel :relaxed:").queue();
            return;
        }
        JSONObject roleObj = this.loadUserRole();
        Role role = this.getRole(guild, member, roleObj);
        role.getManager().setColor(color).queue();
        textChannel.sendMessage(member.getAsMention() + ", Your role color is now " + message + " :art:").queue();
        this.states.replace(member.getUser(), State.NONE);
    }

    private void deleteRole(Guild guild, Member member) {
        JSONObject roleObj = this.loadUserRole();
        Role role = this.getRole(guild, member, roleObj);
        role.getManager().getRole().delete().queue();
        roleObj.remove(member.getUser().getName());
        this.saveUserRole(roleObj);
    }

    private Role getRole(Guild guild, Member member, JSONObject roleObj) {
        String roleName = roleObj.getString(member.getUser().getName());
        List<Role> roles = guild.getRolesByName(roleName, true);
        return roles.isEmpty() ? null : roles.get(0);
    }

    private JSONObject loadUserRole() {
        JSONObject obj = new JSONObject();
        Path path = Paths.get(this.FILEPATH);
        try {
            if (Files.size(path) > 0) {
                String content = new String(Files.readAllBytes(path));
                obj = new JSONObject(content);
            }
            else {
                obj.put("Example", "Role Name");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file: " + e.getMessage());
        }
        return obj;
    }

    private void saveUserRole(JSONObject newSave) {
        // Write upload count to JSON
        try (FileWriter file = new FileWriter(this.FILEPATH)) {
            file.write(newSave.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Color getColor(String message) {
        if (message.matches("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$"))
            return Color.decode(message);

        switch (message.toLowerCase()) {
            case "red": return Color.RED;
            case "light red": return new Color(255, 102, 102);
            case "dark red": return new Color(139, 0, 0);
            case "green": return Color.GREEN;
            case "light green": return new Color(102, 255, 102);
            case "dark green": return new Color(0, 100, 0);
            case "blue": return Color.BLUE;
            case "light blue": return new Color(173, 216, 230);
            case "dark blue": return new Color(0, 0, 139);
            case "yellow": return Color.YELLOW;
            case "light yellow": return new Color(255, 255, 153);
            case "dark yellow": return new Color(128, 128, 0);
            case "orange": return Color.ORANGE;
            case "light orange": return new Color(255, 165, 0);
            case "dark orange": return new Color(255, 69, 0);
            case "pink": return Color.PINK;
            case "light pink": return new Color(255, 182, 193);
            case "dark pink": return new Color(255, 105, 180);
            case "purple": return new Color(128, 0, 128);
            case "light purple": return new Color(192, 128, 255);
            case "dark purple": return new Color(75, 0, 130);
            case "brown": return new Color(139, 69, 19);
            case "light brown": return new Color(205, 133, 63);
            case "dark brown": return new Color(101, 67, 33);
            case "teal": return new Color(0, 128, 128);
            case "light teal": return new Color(0, 206, 209);
            case "dark teal": return new Color(0, 100, 100);
            case "cyan": return Color.CYAN;
            case "light cyan": return new Color(0, 255, 255);
            case "dark cyan": return new Color(0, 139, 139);
            case "magenta": return Color.MAGENTA;
            case "light magenta": return new Color(255, 0, 255);
            case "dark magenta": return new Color(139, 0, 139);
            case "white": return Color.WHITE;
            case "light white": return new Color(245, 245, 245);
            case "dark white": return new Color(220, 220, 220);
            case "black": return Color.BLACK;
            case "light black": return new Color(60, 60, 60);
            case "dark black": return new Color(30, 30, 30);
            case "gray": return Color.GRAY;
            case "light gray": return Color.LIGHT_GRAY;
            case "dark gray": return Color.DARK_GRAY;
            case "blurple": return new Color(114, 137, 218);
            case "grayple": return new Color(153, 170, 181);
            case "violet": return new Color(148, 0, 211);
            case "light violet": return new Color(199, 21, 133);
            case "dark violet": return new Color(93, 51, 93);
            case "indigo": return new Color(75, 0, 130);
            case "light indigo": return new Color(111, 0, 255);
            case "dark indigo": return new Color(49, 0, 98);
            case "lime": return new Color(0, 255, 0);
            case "light lime": return new Color(204, 255, 0);
            case "dark lime": return new Color(51, 204, 0);
            case "maroon": return new Color(128, 0, 0);
            case "light maroon": return new Color(195, 33, 72);
            case "dark maroon": return new Color(88, 0, 0);
            case "beige": return new Color(245, 245, 220);
            case "light beige": return new Color(255, 250, 205);
            case "dark beige": return new Color(210, 180, 140);
            case "turquoise": return new Color(64, 224, 208);
            case "light turquoise": return new Color(175, 238, 238);
            case "dark turquoise": return new Color(0, 206, 209);
            case "coral": return new Color(255, 127, 80);
            case "light coral": return new Color(240, 128, 128);
            case "dark coral": return new Color(205, 91, 69);
            case "gold": return new Color(255, 215, 0);
            case "light gold": return new Color(250, 250, 210);
            case "dark gold": return new Color(218, 165, 32);
            case "mint": return new Color(189, 252, 201);
            case "light mint": return new Color(245, 255, 250);
            case "dark mint": return new Color(32, 178, 170);
            case "olive": return new Color(128, 128, 0);
            case "light olive": return new Color(173, 255, 47);
            case "dark olive": return new Color(85, 107, 47);
            case "sage green": return new Color(188, 236, 172);
            case "light sage green": return new Color(207, 239, 193);
            case "dark sage green": return new Color(156, 197, 141);
            case "rose gold": return new Color(183, 110, 121);
            case "light rose gold": return new Color(231, 172, 172);
            case "dark rose gold": return new Color(152, 89, 96);
            case "charcoal": return new Color(54, 69, 79);
            case "light charcoal": return new Color(112, 123, 130);
            case "dark charcoal": return new Color(34, 40, 49);
            case "aubergine": return new Color(61, 48, 84);
            case "light aubergine": return new Color(104, 85, 144);
            case "dark aubergine": return new Color(48, 37, 65);
            case "peach": return new Color(255, 229, 180);
            case "light peach": return new Color(255, 239, 213);
            case "dark peach": return new Color(255, 218, 185);
            case "lavender": return new Color(230, 230, 250);
            case "light lavender": return new Color(237, 237, 255);
            case "dark lavender": return new Color(150, 123, 182);
            case "mustard": return new Color(255, 219, 88);
            case "light mustard": return new Color(255, 239, 161);
            case "dark mustard": return new Color(204, 174, 68);
            case "teal blue": return new Color(54, 117, 136);
            case "light teal blue": return new Color(144, 195, 212);
            case "dark teal blue": return new Color(0, 77, 82);
            case "cerulean": return new Color(42, 82, 190);
            case "light cerulean": return new Color(155, 196, 226);
            case "dark cerulean": return new Color(0, 51, 102);
            case "salmon": return new Color(250, 128, 114);
            case "light salmon": return new Color(255, 160, 122);
            case "dark salmon": return new Color(233, 150, 122);
            case "navy": return new Color(0, 0, 128);
            case "light navy": return new Color(0, 0, 205);
            case "dark navy": return new Color(0, 0, 78);
            case "navy blue": return new Color(0, 0, 128);
            case "light navy blue": return new Color(0, 0, 205);
            case "dark navy blue": return new Color(0, 0, 78);
            case "chartreuse": return new Color(127, 255, 0);
            case "light chartreuse": return new Color(223, 255, 0);
            case "dark chartreuse": return new Color(87, 138, 52);
            case "amber": return new Color(255, 191, 0);
            case "light amber": return new Color(255, 204, 79);
            case "dark amber": return new Color(204, 133, 0);
            case "emerald": return new Color(80, 200, 120);
            case "light emerald": return new Color(146, 223, 173);
            case "dark emerald": return new Color(4, 99, 7);
            case "ivory": return new Color(255, 255, 240);
            case "light ivory": return new Color(255, 255, 224);
            case "dark ivory": return new Color(238, 238, 224);
            case "plum": return new Color(221, 160, 221);
            case "light plum": return new Color(238, 187, 238);
            case "dark plum": return new Color(142, 69, 133);
            case "sienna": return new Color(160, 82, 45);
            case "light sienna": return new Color(203, 133, 63);
            case "dark sienna": return new Color(136, 45, 23);
            case "fuchsia": return new Color(255, 0, 255);
            case "light fuchsia": return new Color(255, 119, 255);
            case "dark fuchsia": return new Color(139, 0, 139);
            case "silver": return new Color(192, 192, 192);
            case "light silver": return new Color(211, 211, 211);
            case "dark silver": return new Color(169, 169, 169);
        }

        String[] rgbComponents = message.split(",");
        if (rgbComponents.length == 3) {
            try {
                int r = Integer.parseInt(rgbComponents[0].trim());
                int g = Integer.parseInt(rgbComponents[1].trim());
                int b = Integer.parseInt(rgbComponents[2].trim());

                if (r >= 0 && r <= 255 && g >= 0 && g <= 255 && b >= 0 && b <= 255)
                    return new Color(r, g, b);
            } catch(NumberFormatException ignored){}
        }
        return null; // Should be caught
    }
}
