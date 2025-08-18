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