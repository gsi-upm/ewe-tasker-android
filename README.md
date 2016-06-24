![Ewe Tasker Logo](./app/src/main/res/drawable/ewetaskermobile.jpg)
#Installation
First of all, clone the git project locally and access to ewe-tasker directory.

```
git clone https://github.com/gsi-upm/ewe-tasker-android.git
cd ewe-tasker-android

```
Open and compile the project from Android Studio or drag and drop the apk file to your smartphone


```
EWETaskerMobile.apk

```
#User Guide
#Rules Framework
##Rule Definition Module
In this section we will create a rule using the default channels hosted in the public server. You can start by pressing the '+' button. The rule we want to create will have an **ECA** (Event - Condition - Action) structure i.e. ***If Bluetooth is enabled then show a notification saying "You have enabled Bluetooth!"*** . First of all we will choose the Bluetooth channel as **IF** actor selecting the *Turn On* as triggering event. Then, we will select the Notification module as **DO** performer, filling in the *Show* action the text we want to show (*You have enabled Bluetooth!*). Finally, it is necessary to complete the rule context data like title, description or place.

<p align="center">
<img src="./app/src/main/res/drawable/ruletest.jpg" width="400">
</p>

##Rule Execution Module
We can test the test rule created in the previous step by enabling the Bluetooth. As result we obtain the customized notifification created in the rule definition process.
<p align="middle">
<img src="./app/src/main/res/drawable/BT.jpg" width="80" >
<img src="./app/src/main/res/drawable/arrow_github.png" width="80">
<img src="./app/src/main/res/drawable/notif.jpg" width="400" >
</p>
##Beacon Listener
In this screen the application will be listening all the beacons located near the smartphone every 10 seconds. This channel gets is named *Presence Sensor*, and requires Bluetooth enabled. The parameters are the sensor ID and the distance between the beacon and the smartphone.

###Open Door
###Door opening
In particular, we will explain how the rule ***when I arrive work (less than 2 meters), open the smart door***. The smartphone must be in the listening beacon screen. When the phone is located at a distance less than 2 meters from the beacon, an allert dialog will pop up requesting the password and opening the door if success.
