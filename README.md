# OpenCV-SpaceApps
Here is the open computer vision(OpenCV) code that we wrote for this challenge, everything still being in beta version.

All you have to do is create an Android app, using this Kotlin source code as one of the scripts and the contents of activity_main.xml being added to your main activity XML file and you're done!
You have successfully implemented our app in your device to test and see how it works!

Quick explanation of the code:

-->It takes real-time camera input and begins the processing of every frame.

-->Initializes multiple arrays of values, one of them which will be used for storing grayscale values of the image.

-->Then we analyze drastic differences in the grayscale values and draw the contours over them.

-->At last we output the edited frame into an imgae preview, and get back to square one, for processing another video frame.

The image processing is done very fast, but the camera preview transfer from the surfaceView of the app to the imageView of the app is sometimes slower(really depends on which device is used and it's specs).

Future updates will be shown in this app, including optimizing the video output of the detected damage, using ML(Machine Learning) to even tell us what type of damage has been done to our spacecraft.
The base idea is for us to send this autonomous drone out into space, let it surveil our spacecraft and once it finds some sort of damage, send us back a picture of the damage and the type of damage we are seeing there, including the depth(if it is a dent or a hole) or the temperature(if it is damage caused by passing high-temperature stars, like our Sun).
We plan to calculate the position of our drone in space based on the signal of 4 RFID beacons that sit strategically on the spacecraft, and the orientation of the drone related to the spacecraft by using 8 IR(Infrared) sensors, from which we will calculate how much our drone has turned around and correct it by triggering which thrusters need to be used, in order to correct our orientation.

We have also thought of using angular momentum to keep our drone in the right orientation, but it means that we need to consume more power and we have to start and stop the rotors in order to make our drone follow the spacecraft and not fly into outer space without getting back.We will get back at this some other time, when we have more ideas that can make this possible.

Our returning system of the drone records the place it has been powered on(coordinates from the RFID beacons) and at the end of the surveillance or when asked to, it returns to those coordinates, thus making retrieving of the drone much easier.
