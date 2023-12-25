package com.toki.clever.Casino.Commands.Games;

import com.toki.clever.Casino.Casino;
import com.toki.clever.Casino.Users.Player;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Connect4 extends ListenerAdapter {
    public static void connect4Command(SlashCommandInteractionEvent event, User user)
    {
        double bet;

        // Gets user input from command
        OptionMapping optionBet = event.getOption("bet");
        if(optionBet != null)
            bet = optionBet.getAsDouble();
        else return;

        // Check if player can bet
        Player player = Casino.getPlayer(user);
        if(bet > player.getCash()) {
            event.reply("You don't have enough to bet that much").setEphemeral(true).queue();
            return;
        }

        // Create Message
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Color.BLUE);
        embed.setTitle("Connect4!");
        embed.setDescription(user.getEffectiveName() + " wants to play with you!\n" + "They want to bet $" + bet);
        embed.setFooter(user.getEffectiveName(), user.getAvatarUrl());

        // Send Message
        event.replyEmbeds(embed.build())
                .addActionRow(
                        Button.success("C42P:"+bet, "Play!"),
                        Button.danger("C4C", "Cancel")
                )
                .queue();
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event)
    {
        try {
            if(!(Objects.equals(event.getMessage().getEmbeds().get(0).getTitle(), "Connect4!"))) return;
        } catch (Exception e) {return;}

        String id = event.getComponentId();

        // Check if the button ID matches
        if (id.contains("C42P"))
        {
            // Gets Bet
            double bet = Double.parseDouble(id.substring(id.indexOf(':')+1));

            // Get initial users
            String user1Name = Objects.requireNonNull(event.getMessage().getEmbeds().get(0).getFooter()).getText();
            if(user1Name == null) throw new NullPointerException();
            User user1 = Objects.requireNonNull(event.getGuild()).getMembersByEffectiveName(user1Name, true).get(0).getUser();
            User user2 = event.getUser();

            // Check if same player
            if(user1.equals(user2)) {
                event.reply("You cant race yourself :grimacing:").setEphemeral(true).queue();
                return;
            }

            // Check if player can bet
            Player player2 = Casino.getPlayer(user2);
            if(bet > player2.getCash()) {
                event.editMessageEmbeds(new EmbedBuilder()
                        .setDescription("You don't have enough to bet that much")
                        .setFooter(user2.getEffectiveName(), user2.getAvatarUrl()).build())
                        .setComponents(Collections.emptyList())
                        .queue();
                return;
            }

            // Create Game Board Description for Message
            StringBuilder board = new StringBuilder();
            board.append(">>> ");
            for(int j = 0; j < 6; j++)
                board.append("<:C4Slot:1187962918604648519>".repeat(7)).append("\n");

            // Create Action Row
            ActionRow row1 = ActionRow.of(Button.secondary("C4Col1", "1"),
                    Button.secondary("C4Col2", "2"),
                    Button.secondary("C4Col3", "3"),
                    Button.secondary("C4Col4", "4"),
                    Button.secondary("C4Col5", "5"));
            ActionRow row2 = ActionRow.of(Button.secondary("C4Col6", "6"),
                    Button.secondary("C4Col7", "7"));

            // Create Game Board Message
            event.editMessageEmbeds(new EmbedBuilder()
                    .setTitle("Connect4!")
                    .setColor(Color.decode("#75BFEC"))
                    .setDescription(board.toString())
                    .addField("Bet  -  $" + bet,"It's " + user2.getAsMention() + "'s turn!", false)
                    .addField("", user1.getAsMention() + "<:C4CY:1187991986817470536> vs " + user2.getAsMention() + "<:C4CR:1187992009269579907>", false)
                    .build())
                    .setComponents(row1,row2)
                    .queue();
        }
        else if (id.contains("C4Col"))
        {
            // Get current move
            int col = Integer.parseInt(event.getButton().getLabel())-1;

            // Gets Users
            MessageEmbed.Field field1 = event.getMessage().getEmbeds().get(0).getFields().get(0);
            MessageEmbed.Field field2 = event.getMessage().getEmbeds().get(0).getFields().get(1);
            String footerText = field2.getValue();
            if(footerText == null) throw new NullPointerException();
            String name1 = footerText.substring(footerText.indexOf('@')+1, footerText.indexOf('>'));
            String secondMention = footerText.substring(footerText.lastIndexOf('@')+1);
            String name2 = secondMention.substring(0, secondMention.indexOf('>'));

            User user1 = Objects.requireNonNull(Objects.requireNonNull(event.getGuild()).getMemberById(name1)).getUser();
            User user2 = Objects.requireNonNull(event.getGuild().getMemberById(name2)).getUser();
            double bet = Double.parseDouble(Objects.requireNonNull(field1.getName()).substring(field1.getName().indexOf('$')+1));

            // Gets Whose Next Turn It is
            boolean user1Turn = (Objects.requireNonNull(field1.getValue()).contains(user1.getAsMention()));
            String newMention = user1Turn ? user2.getAsMention() : user1.getAsMention();
            String chip = user1Turn ? "yellow" : "red";

            // Check if user matches whose turn it is
            User currentTurn = (user1Turn) ? user1 : user2;
            if(!(event.getUser().equals(currentTurn))) {
                event.reply("It's not your turn dud :slight_smile:").setEphemeral(true).queue();
                return;
            }

            // Get Board Layout
            String boardStr = event.getMessage().getEmbeds().get(0).getDescription();
            if(boardStr == null) throw new NullPointerException();
            boardStr = boardStr.substring(4);
            String[][] board = new String[6][7];
            for(int j = 0; j < 6; j++) {
                for(int k = 0; k < 7; k++) {
                    board[j][k] = boardStr.substring(boardStr.indexOf('<'),boardStr.indexOf('>')+1);
                    boardStr = boardStr.substring(boardStr.indexOf('>')+1);
                }
            }

            // Get Move's Index
            int moveIndex = -1;
            for(int i = 0; i < 6; i++)
                if (board[i][col].equals("<:C4Slot:1187962918604648519>"))
                    moveIndex = i;
            // If col was full ask for new move
            if(moveIndex == -1) {
                event.reply("That's not a possible move... :grimacing:").setEphemeral(true).queue();
                return;
            }

            String[] yellow = {"<a:y1:1187963132262481970>", "<a:y2:1187963133562736742>", "<a:y3:1187963136389685258>", "<a:y4:1187963137593454763>", "<a:y5:1187963139812237384>", "<a:y6:1187963141674520636>"};
            String[] red = {"<a:r1:1187981853571956786>", "<a:r2:1187981854624722964>", "<a:r3:1187981856264695808>", "<a:r4:1187981856927383573>", "<a:r5:1187981859712421918>", "<a:r6:1187981861343993936>"};

            // Create Static message & check if won
            boolean won;
            if(chip.equals("yellow")) {
                board[moveIndex][col] = "<:C4Yellow:1187962970437853247>";
                won = Connect4.checkWon(board, "yellow");
            }
            else {
                board[moveIndex][col] = "<:C4Red:1187963037764833330>";
                won = Connect4.checkWon(board, "red");
            }

            StringBuilder staticMessage = new StringBuilder();
            staticMessage.append(">>> ");
            for(String[] row : board) {
                for(String str : row)
                    staticMessage.append(str);
                staticMessage.append("\n");
            }

            // Add animated emojis
            int emojiVal = 0;
            for(int i = moveIndex; i >= 0; i--) {
                if(chip.equals("yellow"))
                    board[i][col] = yellow[emojiVal];
                else
                    board[i][col] = red[emojiVal];
                emojiVal++;
            }

            // Create Animated message
            StringBuilder animMessage = new StringBuilder();
            animMessage.append(">>> ");
            for(String[] row : board) {
                for(String str : row)
                    animMessage.append(str);
                animMessage.append("\n");
            }

            // Check if move was done too fast
            String message = event.getMessage().getEmbeds().get(0).getDescription();
            if(message == null) throw new NullPointerException();
            for(int i = 1; i <= 6; i++) {
                if(message.contains("r" + i) || message.contains("y" + i)) {
                    event.reply("Too fast bucko :/").setEphemeral(true).queue();
                    return;
                }
            }

            // Create Action Row
            ActionRow row1 = ActionRow.of(Button.secondary("C4Col1", "1"),
                    Button.secondary("C4Col2", "2"),
                    Button.secondary("C4Col3", "3"),
                    Button.secondary("C4Col4", "4"),
                    Button.secondary("C4Col5", "5"));
            ActionRow row2 = ActionRow.of(Button.secondary("C4Col6", "6"),
                    Button.secondary("C4Col7", "7"));

            // Create Game Board Message
            event.editMessageEmbeds(new EmbedBuilder()
                            .setTitle("Connect4!")
                            .setColor(Color.decode("#75BFEC"))
                            .setDescription(animMessage)
                            .addField("Bet  -  $" + bet,"It's " + newMention + "'s turn!", false)
                            .addField("", user1.getAsMention() + "<:C4CY:1187991986817470536> vs " + user2.getAsMention() + "<:C4CR:1187992009269579907>", false)
                            .build())
                    .setComponents(row1,row2)
                    .queue();

            // Change to Static Message
            event.getHook().editOriginalEmbeds(new EmbedBuilder()
                    .setTitle("Connect4!")
                    .setColor(Color.decode("#75BFEC"))
                    .setDescription(staticMessage)
                    .addField("Bet  -  $" + bet,"It's " + newMention + "'s turn!", false)
                    .addField("", user1.getAsMention() + "<:C4CY:1187991986817470536> vs " + user2.getAsMention() + "<:C4CR:1187992009269579907>", false)
                    .build())
                    .setComponents(row1,row2)
                    .queueAfter(800, TimeUnit.MILLISECONDS);

            // Check if someone won methods
            if(won) {
                // Modify players cash values
                Player player1 = Casino.getPlayer(user1);
                Player player2 = Casino.getPlayer(user2);
                if(chip.equals("yellow")) {
                    player1.addCash(bet);
                    player2.removeCash(bet);
                }
                else {
                    player2.addCash(bet);
                    player1.removeCash(bet);
                }
                // Edit to winner message
                event.getHook().editOriginalEmbeds(new EmbedBuilder()
                                .setTitle("Connect4!")
                                .setColor(Color.GREEN)
                                .setDescription(staticMessage)
                                .addField("You got the $" + bet + "!",":confetti_ball: " + (user1Turn ? user1.getAsMention() : user2.getAsMention()) + " won! :tada:", false)
                                .addField("", user1.getAsMention() + "<:C4CY:1187991986817470536> vs " + user2.getAsMention() + "<:C4CR:1187992009269579907>", false)
                                .build())
                        .setComponents(Collections.emptyList())
                        .queueAfter(1100, TimeUnit.MILLISECONDS);
            }
        }
        else if(id.equals("C4C")) {
            String user1Name = Objects.requireNonNull(event.getMessage().getEmbeds().get(0).getFooter()).getText();
            if(user1Name == null) throw new NullPointerException();
            User user1 = Objects.requireNonNull(event.getGuild()).getMembersByEffectiveName(user1Name, true).get(0).getUser();
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.red);
            embed.setTitle("Connect4!");
            embed.setDescription("Canceled the game");
            embed.setFooter(user1.getEffectiveName(), user1.getAvatarUrl());
            event.editMessageEmbeds(embed.build()).setComponents(Collections.emptyList()).queue();
        }
    }

    public static boolean checkWon(String[][] board, String color) {
        int ROWS = 6;
        int COLUMNS = 7;
        String CHECK = (color.equals("red")) ? "<:C4Red:1187963037764833330>" : "<:C4Yellow:1187962970437853247>";

        // Horizontal Check
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS - 3; col++) {
                if (CHECK.equals(board[row][col])
                        && CHECK.equals(board[row][col+1])
                        && CHECK.equals(board[row][col+2])
                        && CHECK.equals(board[row][col+3])) {
                    return true;
                }
            }
        }

        // Vertical Check
        for (int col = 0; col < COLUMNS; col++) {
            for (int row = 0; row < ROWS - 3; row++) {
                if (CHECK.equals(board[row][col])
                        && CHECK.equals(board[row+1][col])
                        && CHECK.equals(board[row+2][col])
                        && CHECK.equals(board[row+3][col])) {
                    return true;
                }
            }
        }

        // Diagonal Check (Top-left to bottom-right)
        for (int row = 0; row < ROWS - 3; row++) {
            for (int col = 0; col < COLUMNS - 3; col++) {
                if (CHECK.equals(board[row][col])
                        && CHECK.equals(board[row+1][col+1])
                        && CHECK.equals(board[row+2][col+2])
                        && CHECK.equals(board[row+3][col+3])) {
                    return true;
                }
            }
        }

        // Diagonal Check (Top-right to bottom-left)
        for (int row = 0; row < ROWS - 3; row++) {
            for (int col = 3; col < COLUMNS; col++) {
                if (CHECK.equals(board[row][col])
                        && CHECK.equals(board[row+1][col-1])
                        && CHECK.equals(board[row+2][col-2])
                        && CHECK.equals(board[row+3][col-3])) {
                    return true;
                }
            }
        }

        return false; // No winner found
    }
}