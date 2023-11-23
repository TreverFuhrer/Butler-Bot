package com.toki.clever.TheCafeServer.roleCreator;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateFeaturesEvent;
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
            if(notifCount == 0)
                channel.sendMessage(member.getAsMention() + ", For your role, say role").queue();
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
                "\n:art: Type like the bold **Your roles color?**" +
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
