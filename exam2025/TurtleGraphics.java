package exam2025;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;


import java.io.FileWriter;
import java.io.PrintWriter;

import java.io.BufferedReader;
import java.io.FileReader;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

import uk.ac.leedsbeckett.oop.LBUGraphics;

public class TurtleGraphics extends LBUGraphics {


    private List<String> commandHistory = new ArrayList<>();

    private final String[] VALID_COMMANDS = {      // valid commands
            "about", "move", "reverse", "right", "left", "penup", "pendown", "red", "green", "black", "white", "pink", "blue", "penwidth", "saveimage", "loadimage", "savecommands", "loadcommands", "reset", "clear", "help", "dance", "undo", "theme", "square", "triangle", "pencolour", "circle", "exit"
    };


    private final int ERROR = 0;
    private final int WARNING = 1;
    private final int INFO = 2;

    private boolean removeMessages = false;

    private JFrame MainFrame;


    public TurtleGraphics() {
        MainFrame = new JFrame();                //create a Jframe to display the turtle panel
        MainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Make sure the app exits when closed
        MainFrame.setLayout(new FlowLayout());  //not strictly necessary

        MainFrame.add(this);                                    //"this" is this object that extends turtle graphics, so we are adding a turtle graphics panel to the frame
        MainFrame.pack();                                               //set the frame to a decent size
        MainFrame.setVisible(true);// display it
    }


    private boolean isImageSaved = true;


    public void processCommand(String command)      //this method must be provided because LBUGraphics will call it when it's JTextField is used
    {
        String[] commands = command.split(" ");// splits command into parts


        if (!command.startsWith("savecommands") && !command.startsWith("loadcommands") && !command.startsWith("saveimage") && !command.startsWith("loadimage") && !command.startsWith("clear") && !command.startsWith("theme") && !command.startsWith("help")){ // these commands wont be saved on the text files created.
            commandHistory.add(command);
        }


        if (!isCommandValid(commands[0])) {
            displayMessage(commands[0] + ": Invalid command. \nIf you need further information on valid commands simply type 'help'", WARNING);   //checks if command is valid, if not error message displayed
            return;
        }


        switch (commands[0]) {
            case "about":
                doAbout(commands);
                break;

            case "dance":
                dance(2);
                break;

            case "move":
                move(commands);
                break;

            case "reverse":
                reverse(commands);
                break;

            case "left":
                left(commands);
                break;

            case "right":
                right(commands);
                break;


            case "penup":
                displayMessage("Pen lifted. Turtle will move without drawing.", INFO);
                drawOff();
                isImageSaved = false;
                break;

            case "pendown":
                displayMessage("Pen down. Turtle will be drawing as its moving.", INFO);
                drawOn();
                isImageSaved = false;
                break;


            case "red":
                displayMessage("Ink changed to red!", INFO);
                setPenColour(Color.RED);
                isImageSaved = false;
                break;

            case "green":
                displayMessage("Ink changed to green!", INFO);
                setPenColour(Color.GREEN);
                isImageSaved = false;
                break;

            case "black":
                displayMessage("Ink changed to black!", INFO);
                setPenColour(Color.BLACK);
                isImageSaved = false;
                break;

            case "white":
                displayMessage("Ink changed to white!", INFO);
                setPenColour(Color.WHITE);
                isImageSaved = false;
                break;

            case "blue":
                displayMessage("Ink changed to blue!", INFO);
                setPenColour(Color.BLUE);
                isImageSaved = false;
                break;

            case "pink":
                displayMessage("Ink changed to pink!", INFO);
                setPenColour(Color.PINK);
                isImageSaved = false;
                break;

            case "pencolour":
                if (commands.length != 4) { // need 4 inputs (pencolour, red,green,blue)
                    displayMessage("Command: pencolour <red> <green> <blue>. Enter valid integers!\nMake sure the colours are separated by a space only no commas!", WARNING);
                    break;
                }
                try {
                    int red = Integer.parseInt(commands[1]);
                    int green = Integer.parseInt(commands[2]);
                    int blue = Integer.parseInt(commands[3]);

                    // make sure they are valid integers between 0-255
                    if (red < 0 || red > 255 || green < 0 || green > 255 || blue < 0 || blue > 255) {
                        displayMessage("Invalid RGB values. Please enter values between 0 and 255 for each color.", WARNING);
                        break;
                    }

                    // Create and set the pen color
                    Color pen = new Color(red, green, blue);
                    setPenColour(pen);
                    displayMessage("Pen color set to RGB(" + red + ", " + green + ", " + blue + ")", INFO);
                } catch (NumberFormatException e) {
                    displayMessage("Invalid input. Please enter valid integers for RGB values.\nMake sure the colours are separated by a space only no commas!", WARNING);
                }
                break;

            case "penwidth":
                penwidth(commands);
                break;

            case "square":
                if (commands.length != 2) {
                    displayMessage("command: square <length>. You need to enter your length!", WARNING);
                    break;
                }
                try {
                    int length = Integer.parseInt(commands[1]);
                    new Square(this, length).draw();

                } catch (NumberFormatException e) {
                    displayMessage("Invalid input. Please enter a number for square length.", WARNING);
                }
                break;

            case "triangle":
                if (commands.length != 2) {
                    displayMessage("command: triangle <size> OR triangle <a,b,c>. You need to enter sides of your equilateral triangle or your custom triangle!\nMake sure there are no spaces included in between your sides for your custom triangle.", WARNING);
                    break;
                }

                if (commands[1].contains(",")) {  //if there is a comma
                    // custom triangle
                    String[] parts = commands[1].split(",");
                    if (parts.length != 3) {
                        displayMessage("Please enter exactly 3 sides separated by commas and without any space, like: triangle 100,140,150", WARNING);
                        break;
                    }

                    try {
                        int[] sides = new int[]{
                                Integer.parseInt(parts[0].trim()),
                                Integer.parseInt(parts[1].trim()),
                                Integer.parseInt(parts[2].trim())
                        };
                        new CustomTriangle(this, sides).draw();
                    } catch (NumberFormatException e) {
                        displayMessage("All 3 values for the side must be integers!.", WARNING);
                    }

                } else {
                    // equilateral triangle
                    try {
                        int size = Integer.parseInt(commands[1]);
                        new EquilateralTriangle(this, size).draw();
                    } catch (NumberFormatException e) {
                        displayMessage("Please enter a valid number for triangle size.", WARNING);
                    }
                }
                break;


            case "circle":
                if (commands.length < 2) {
                    displayMessage("Please enter the radius of your circle!", WARNING);
                } else {
                    drawCircle(commands);
                }
                break;

            case "saveimage":
                if (commands.length < 2) {
                    displayMessage("Please enter a name to save the image.", WARNING);
                } else {
                    saveImage(commands[1]);
                }
                break;

            case "loadimage":
                if (commands.length < 2) {
                    displayMessage("Please enter a name to load the image.", WARNING);
                } else {
                    loadImage(commands[1]);
                }
                break;

            case "savecommands":
                if (commands.length < 2) {
                    displayMessage("Please enter a filename to save the commands.", WARNING);
                } else {
                    saveCommands(commands[1]);
                }
                break;

            case "loadcommands":
                if (commands.length < 2) {
                    displayMessage("Please enter a filename to load the commands.", WARNING);
                } else {
                    loadCommands(commands[1]);
                }
                break;

            case "theme":
                if (commands.length == 1) {
                    displayMessage("Please choose a theme. Try: 'theme light' or 'theme dark'", INFO);
                } else if (commands.length == 2) {
                    String themeChoice = commands[1];

                    if (themeChoice.equals("light")) {
                        setTheme("light");

                    } else if (themeChoice.equals("dark")) {
                        setTheme("dark");
                    } else {
                        displayMessage("Unknown theme '" + themeChoice + "'. Please choose from: 'theme light' or 'theme dark'.", ERROR);
                    }
                } else {
                    displayMessage("Please choose a theme. Try 'theme light' or 'theme dark'", INFO );
                }
                break;


            case "help":
                help();
                break;

            case "reset":
                reset();
                isImageSaved = false;
                break;

            case "clear":
                handleClear();
                break;

            case "exit":
                exit();
                break;

            default:
                displayMessage(commands[0] + " not yet implemented.", WARNING);

        }
    }

    private void penwidth(String[] commands) {

        if (commands.length < 2) {
            // only when typed "penwidth" without a number:
            displayMessage("You can now change the width of you lines! Make sure your integer is between 1-10.", INFO);
            return;
        }
        // if user puts invalid integers this method will print the error
        integerHandling(commands[1], 1, 10, "Penwidth", width -> { // only if integer will be valid and not random char (abc)
            setStroke(width);// width = only the valid input between 1-10
            isImageSaved = false;

            displayMessage("Penwidth set to " + width, INFO);
        });
    }


    private void drawCircle(String[] commands){
        try {
            int radius = Integer.parseInt(commands[1]);
            circle(radius);
            isImageSaved = false;
        } catch (Exception e) {
            displayMessage("Invalid radius for your circle. Enter an integer", ERROR);
        }
    }
    private void left(String[] commands) {
        try {

            int angle = Integer.parseInt(commands[1]);// Try to parse the angle
            if (angle < 0 || angle > 360) {
                displayMessage("Error: Angle for left turn must be between 0 and 360 degrees.", ERROR);
                //return;
            } else
                left(commands[1]);
            isImageSaved = false;

        } catch (Exception e) {
            displayMessage("Error: Invalid angle for left turn! Please enter an integer ", ERROR);
        }
    }


    private void right(String[] commands) {
        try {
            int angle = Integer.parseInt(commands[1]);// Try to parse the angle
            if (angle < 0 || angle > 360) {
                displayMessage("Error: Angle for right turn must be between 0 and 360 degrees.", ERROR);

            } else
                right(commands[1]);
            isImageSaved = false;

        } catch (Exception e) {
            displayMessage("Error: Invalid angle for right turn! Please enter an integer ", ERROR);
        }
    }

    private void move(String[] commands) {
        try {
            int distance = Integer.parseInt(commands[1]);

            // Check if the distance is within a reasonable range
            if (distance < 0) {
                displayMessage("Distance cannot be negative. Please enter a positive number.", ERROR);
            } else {
                forward(distance);
            }
            isImageSaved = false;

        } catch (Exception e) {
            displayMessage("Invalid distance for move. Enter an integer after the move statement", ERROR);
        }
    }

    // Reverse method handles backward movement by using the built-in forward method
    private void reverse(String[] commands) {
        try {
            // Convert the string distance to an integer
            int distance = Integer.parseInt(commands[1]);
            // Move the turtle backward by built-in method with a negative value
            forward(-distance);
            isImageSaved = false;

        } catch (NumberFormatException e) {
            displayMessage("Invalid distance for reverse command. Please enter an integer", ERROR);
        }
    }

    //returning messages via a small window
    public void displayMessage(String message, int alert_status) {

        if (removeMessages) return; // remove pop up messages when needed

        switch (alert_status) {
            case ERROR:
                JOptionPane.showMessageDialog(MainFrame, message, "ERROR", JOptionPane.ERROR_MESSAGE);
                break;
            case WARNING:
                JOptionPane.showMessageDialog(MainFrame, message, "WARNING", JOptionPane.WARNING_MESSAGE);
                break;
            case INFO:
                JOptionPane.showMessageDialog(MainFrame, message, "INFO", JOptionPane.INFORMATION_MESSAGE);
                break;
        }
    }

    private void doAbout(String[] commands) {
        // if more than just 'about' is typed error shown
        if (commands.length > 1) {
            displayMessage("About doesnt take additional parameters");
        } else about(); // this is built-in command
    }

    private void handleClear() {
        if (!isImageSaved) {
            int result = JOptionPane.showConfirmDialog(MainFrame,
                    "You haven’t saved your drawing. Are you sure you want to clear everything?",
                    "Confirm Clear",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (result != JOptionPane.YES_OPTION) {
                displayMessage("Clear cancelled. Drawing not lost.", INFO);
                return;
            } else
                displayMessage("Everything has now been cleared!!", WARNING);
            reset();
        }

        clear();
        isImageSaved = true;
    }


    // checks for valid command
    private boolean isCommandValid(String commands) {

        for (String valid_command : VALID_COMMANDS) {
            if (commands.equals(valid_command)) return true;

        }
        return false;
    }


    private void integerHandling(String value, int min, int max, String commandName, Consumer<Integer> action) {
        try {
            int number = Integer.parseInt(value);
            if (number < min || number > max) {
                displayMessage("Error: " + commandName + " value must be between " + min + " and " + max + ".", ERROR); // if input isnt in between the boundary
            } else {
                action.accept(number);
            }
        } catch (Exception e) {
            displayMessage("Error: Invalid value for " + commandName + "! Please enter an integer.", ERROR);  // if letter is typed
        }
    }

    public void saveImage(String filename) {

        if (!filename.endsWith(".png")) {
            filename += ".png"; // Add the .png extension if it's not there
        }

        // the folder where image needs to be saved
        String folderName = "savedImages";
        File folder = new File(folderName);



        File imageFile = new File(folder, filename); // this is the path of the file which is to be saved, the folder name followed by the name of the file itslef

        // Check if file already exists
        if (imageFile.exists()) {
            int choice = JOptionPane.showConfirmDialog(MainFrame,
                    "An image with this name already exists.\nDo you want to replace it?",
                    "Confirm replace ",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (choice == JOptionPane.NO_OPTION) {  // if user selects no and doesnt wanna replace
                displayMessage("Save cancelled. Image not replaced.", INFO);
                return;
            }
        }

        try {
            BufferedImage image = getBufferedImage();
            ImageIO.write(image, "png", imageFile);
            displayMessage("Image saved successfully as " + filename, INFO);
            isImageSaved = true; // marks as saved (true)


        } catch (IOException e) {    // error msg if couldnt save
            displayMessage("Failed to save image: " + filename, ERROR);
        }
    }

    public void loadImage(String filename) {
        if (!filename.endsWith(".png")) {
            filename += ".png"; // Make sure .png is added only once
        }

        // folder which saved images in
        String folderName = "savedImages";
        File inputFile = new File(folderName, filename);

        // if file doesnt exist
        if (!inputFile.exists()) {
            displayMessage("Image file does not exist: " + filename, ERROR);
            return;
        }

        try {
            BufferedImage image = ImageIO.read(inputFile);  // reading the image
            setBufferedImage(image); //setting the image file
            repaint(); // show the image again
            displayMessage("Image loaded from " + filename, INFO);
        } catch (IOException e) {
            displayMessage("Failed to load image: " + e.getMessage(), ERROR);
        }
    }

    public void saveCommands(String filename) {

        String folderName = "savedCommands";
        File folder = new File(folderName);


        if (!filename.endsWith(".txt")) {
            filename += ".txt"; // will add .txt if needed
        }

        File commandFile = new File(folder, filename);

        // If a file with the same name exists already---
        if (commandFile.exists()) {
            displayMessage("A text file with this name already exists! Try renaming your file.", INFO);
            return;
        }

        // writing to the text files
        try (PrintWriter writer = new PrintWriter(new FileWriter(commandFile))) {
            for (String cmd : commandHistory) {
                writer.println(cmd);
            }
            displayMessage("Commands saved to " + filename, INFO);
        } catch (IOException e) {
            displayMessage("Failed to save commands: " + e.getMessage(), ERROR);
        }
    }


    public void loadCommands(String filename) {
        if (!filename.endsWith(".txt")) {
            filename += ".txt"; // Ensure it ends with .txt
        }

        String folderName = "savedCommands";
        File inputFile = new File(folderName, filename);


        if (!inputFile.exists()) {
            displayMessage("Command file does not exist: " + filename, ERROR);
            return;
        }

        removeMessages = true; // removes pop up messages on the screen


        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                processCommand(line);
            }
            removeMessages = false; // Done, so we turn messages back on
            displayMessage("Commands loaded and executed from " + filename, INFO);
        } catch (IOException e) {
            removeMessages = false; // Just in case there is an error#
            displayMessage("Failed to load commands: " + e.getMessage(), ERROR);
        }
    }


    private void help() {
        String helpMessage = "List of Valid Commands:\n" +
                "===============================\n" +
                "- about\n" +
                "- move [distance]\n" +
                "- reverse [distance]\n" +
                "- right [angle (0–360)]\n" +
                "- left [angle (0–360)]\n" +
                "- penup\n" +
                "- pendown\n" +
                "- penwidth [1–10]; handles the width\n" +
                "- reset\n" +
                "- clear\n" +
                "- exit\n" +
                "For pen ink, type:\n" +
                "- red\n" +
                "- green\n" +
                "- black\n" +
                "- white\n" +
                "- pink\n" +
                "- pencolour <[0-255]><[0-255]><[0-255]>  -for custom RGB colour\n" +
                "For your files:\n" +
                "- saveimage\n" +
                "- loadimage\n" +
                "- savecommands\n" +
                "- loadcommands\n" +
                "For your Themes:\n" +
                "- theme light\n" +
                "- theme dark\n" +
                "To draw shapes:\n" +
                "- square <side>\n" +
                "- triangle <size>\n" +
                "- triangle <side1><side2><side3>\n" +
                "- circle <radius>\n";

        displayMessage(helpMessage, INFO);
    }

    public void setTheme(String theme) {

        if (theme.equals("light")) {


            int choice = JOptionPane.showConfirmDialog(MainFrame,
                    "Your work will discard! Save your work if you dont want to lose it!\nDo you want to save?",
                    "Confirm save",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (choice == JOptionPane.YES_OPTION) {  // if user selects yes and does wanna save
                displayMessage("To save your current work type: 'saveimage' ", INFO);

            } else {
                setBackground_Col(Color.WHITE);
                setPenColour(Color.BLACK);
                //handleClear();
                clear();
                reset();
                displayMessage("Light theme applied.", INFO);
            }


        } else if (theme.equals("dark")) {

            int choice = JOptionPane.showConfirmDialog(MainFrame,
                    "Your work will discard! Save your work if you dont want to lose it!\nDo you want to save?",
                    "Confirm save",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (choice == JOptionPane.YES_OPTION) {  // if user selects yes and does wanna save
                displayMessage("To save your current work type: 'saveimage' ", INFO);
            } else {
                setBackground_Col(Color.BLACK);
                setPenColour(Color.RED);
                displayMessage("Dark theme applied.", INFO);
                //handleClear();
                clear();
                reset();

            }
        }



    }

    private void exit(){
        if (!isImageSaved) {
            int choice = JOptionPane.showConfirmDialog(MainFrame,
                    "You haven’t saved your drawing. Do you want to save it before exiting?",
                    "Confirm Exit",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (choice == JOptionPane.YES_OPTION) {
                displayMessage("To save your current work type: 'saveimage'", INFO);
                return;
            }
        }

        displayMessage("Goodbye! Closing Turtle Graphics application.\n\n \t\t   Created by Vanshika Singh", INFO);
        System.exit(0); // Now exit
    }

}
