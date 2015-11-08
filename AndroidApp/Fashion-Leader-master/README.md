# FashionLeader


1. Android > Select clothes
2. Android > Sends push notification
3. Arduino > Takes notification
4. Arduino > do something (move clothes, blink LEDs)



## Push of Android
### Send push from Adnroid
```
ParsePush push = new ParsePush();
push.setChannel("channelName");
push.setMessage("The is the push message of push notification".);
push.sendInBackground();
```

### Prepraraton to take push in Android
```
Parse.initialize(this, "---", "---);
ParseInstallation.getCurrentInstallation().saveInBackground(); 
ParsePush.subscribeInBackground("channelName");
```
