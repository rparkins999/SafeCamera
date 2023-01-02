# SafeCamera
This is a simple Android app for a Samsung phone which runs the built-in camera app
with Bluetooth disabled.

The reason for this is that after the latest (December 2022) OS update on
Samsung Galaxy S21 phones, and probably some other models, the built-in camera app
refuses to run unless it is given permission to access "nearby devices",
by which it means Bluetooth. I don't know why it calls the permission
"nearby devices": all other references to Bluetooth call it "Bluetooth",
and you can also access a nearby device using Wi-Fi or even NFC if the device
is close enough.

It doesn't actually need Bluetooth to take photographs, or even to record movies,
but you *might* have a Bluetooth microphone (I don't) and you *might* want to use
it to get better sound quality when recording a movie.

Of course the Android best practice advice at
https://developer.android.com/guide/topics/permissions/overview
is not to ask for runtime permissions unless and until you need them,
but Samsung aren't doing that.

I don't want the camera app to announce my presence by probing the Bluetooth headset of
anyone who happens to be near me, and they might regard such an action as intrusive.
So I don't want to give the camera app its nearby devices permission, but it won't
run at all without it.

Hence this little app which runs the camera app with Bluetooth disabled.
The the app can have its permission but it can't use it.

What this app actually does is to see if Bluetooth is enabled and remember if it
was. Then it disables Bluetooth and launches the camera app. This happens so quickly
that you probably won't notice the delay. When the camera app exits, the remembered
state of Bluetooth is restored. For this to work properly, you have to exit the
camera app using the Back button, and not the Home or Recents buttons, since these
leave the camera app and this app sitting on the back stack and the Bluetooth state
dosn't get restored.

The way to use it is to put this app onto your home screen instead of the camera app.
It has the same icon just to make it easy for you.
