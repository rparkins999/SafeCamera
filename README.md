# SafeCamera
This is a simple Android app for a Samsung phone which runs the built-in camera app
with Bluetooth disabled.

The reason for this is that after the latest (December 2022) OS update on
Samsung Galaxy S21 phones, and probably some other models, the built-in camera app
refuses to run unless it is given permission to access "Nearby devices",
by which it means Bluetooth. I don't know why it calls the permission
"Nearby devices": all other references to Bluetooth call it "Bluetooth",
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
Then the app can have its permission but it can't use it.

What this app actually does is to see if Bluetooth is enabled and remember if it
was. Then it disables Bluetooth and launches the camera app. This happens so quickly
that you probably won't notice the delay. When the camera app exits, the remembered
state of Bluetooth is restored. For this to work properly, you have to exit the
camera app using the Back button, and not the Home or Recents buttons, since these
leave the camera app and this app sitting on the back stack and the Bluetooth state
doesn't get restored.

This app itself has to have "Nearby devices" permission in order to disable
and enable Bluetooth, so it will ask you for that permission the first time
you run it. Of course it can't do anything useful without that permission,
so it will just exit if you refuse it.

The way to use it is to put this app onto your home screen instead of the camera app.
It has the same icon just to make it easy for you.

There is a pre-built release signed with my personal key available.
If you don't trust me to build from the source shown, you can clone
this repository and build it yourself.

To build with Android Studio you will need a signing key of your own
since Android no longer allows unsigned apps to be be installed
and I'm not giving you mine. Once you have a signing key set up,
clone this repository, edit the module ```build.gradle``` to
point to your signing key, create or copy a ```local.properties``` file
pointing to where you have installed the Android sdk, and you should
be able to just build a signed APK. If you try it and have problems,
raise an issue.
