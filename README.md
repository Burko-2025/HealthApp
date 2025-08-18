# HealthApp

A simple Java console application for tracking fitness, nutrition, and calories.

## Features
- Track indivual users
- Track daily meals and calories
- Log workouts and calories burned
- Automatic calorie goal suggestions
- Save/load data across sessions
- Daily & monthly summaries

## How to Run
```bash
javac HealthApp.java
java HealthApp

📦 HealthApp – Installation Guide (macOS)

Follow these steps to install and run HealthApp globally from the terminal.

1. Compile the program

Open Terminal in the folder containing HealthApp.java and run:

javac HealthApp.java
jar cfe HealthApp.jar HealthApp *.class

✅ This creates HealthApp.jar.

Test it with:

java -jar HealthApp.jar


2. Move the JAR to a global location
Put the JAR where the system can find it:

sudo mv HealthApp.jar /usr/local/bin/

3. Create the launcher script
Create a new file:
nano ~/healthapp

Paste in the following:

#!/bin/bash
java -jar /usr/local/bin/HealthApp.jar "$@"

Save and exit Nano:
Press CTRL + O → Enter
Press CTRL + X

4. Make the script executable
chmod +x ~/healthapp

5. Move the script into your PATH
sudo mv ~/healthapp /usr/local/bin/

6. Run the app globally
Now you can launch your app from anywhere with:

healthapp


🎉 Done! You now have a globally accessible command for your Java Health Tracker app.

If you would like to download the app as an icon here are the steps you follow
STEP 1: Open Automator

Press Cmd + Space, type Automator, and press Enter.

When prompted, choose “Application”.

This is important because applications created in Automator are clickable apps on your Desktop.

STEP 2: Add a “Run Shell Script” Action

In the left-hand search bar, type “Run Shell Script”.

Drag the Run Shell Script action to the right panel (the workflow area).

STEP 3: Configure the Shell Script
Shell: /bin/bash (default)
Pass input: as arguments
In the text box, type the command to open Terminal and run your Java program:

osascript -e 'tell application "Terminal"
    do script "cd /path/to/your/jar && java -jar HealthApp.jar"
    activate
end tell'


Important:
Replace /path/to/your/jar with the actual folder containing HealthApp.jar.
For example: /Users/username/Documents/HealthApp.
This command tells macOS to open Terminal, go to your program folder, and run your Java app.

STEP 4: Save Your Automator App
Press Cmd + S.
Name it something like HealthApp.
Choose Desktop as the location.
Now you have HealthApp.app on your Desktop.

STEP 5: Add a Custom Icon (Optional)
Create your .icns file (see earlier instructions).
Right-click HealthApp.app → Get Info.
Drag your .icns file onto the small icon at the top-left of the Info window.
Your app now has a custom icon.

STEP 6: Test Your App
Double-click the HealthApp.app.
Terminal should open automatically and run your Java program.
Your console app should now be fully interactive.