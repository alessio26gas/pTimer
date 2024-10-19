# pTimer

#### Video Demo:  <https://youtu.be/0gTEl5h0z78>

### Description

**pTimer** is a project that I made while taking **CS50**, it stands for "**productivity timer**".

As a student myself, I know that sometimes it might be very hard to stay focused and study for a long period of time and often you end up doing nothing and losing a whole day. A very known time management method to maintain focus while studying is the "**pomodoro technique**": it consists in breaking work sessions into **many intervals**, separated by **short breaks**. pTimer is a simple desktop application that let you apply the pomodoro technique very easily during your work session.

It basically consists in a timer, at the begin you're asked to input two values, the duration (in minutes) of the work sessions and the duration of the break sessions, and that's it! When the time is over, it will warn you using a default sound.

### Usage
Insert your preferred work and break session durations and press start to set up the timer. You can pause, resume and reset the timer at any time!

You can use the right and left arrow keys on your keyboard to respectively add or subtract one minute from the currently active timer.

In the about page (accessible throught the '...' button in the bottom right corner) the overall work or break session time elapsed is displayed. You can reset these values at any time.

In the settings page, you can customize the behavior of the software as follows:
- Enable/Disable sound
- Enable/Disable always on top feature
- Enable/Disable 'q' shortcut to quit the application
- Enable/Disable 'm' shortcut to minimize the application
- Enable/Disable [Nord theme](https://www.nordtheme.com/) color palette
- Enable/Disable Stopwatch mode

Stopwatch mode is very simple: use the start button to start the stopwatch, then you can pause, resume and reset the stopwatch at any time.

### Files Description
I wrote this software using **Java Swing** in **IntelliJ IDEA**, it can run on Mac OS, Windows and Linux.

I have been trying to learn Java self-taught in parallel with CS50, I am pretty sure that the code can be very much optimized, but as it is now I am very satisfied with the result I got.

The main class is:
> src/me/alessioimprota/Main.java

The application icon is located in:
> src/me/alessioimprota/icon.png

### Nord Theme
This software is themed using [Nord theme](https://www.nordtheme.com/) color palette. Altough you can change the colors of the software to a more neutral tone in the settings page of the application.

### Undecorated movable frame
I used code from [this very helpful answer](https://stackoverflow.com/questions/16046824/making-a-java-swing-frame-movable-and-setundecorated) on Stack Overflow to allow the Java Swing frame to be movable while undecorated.