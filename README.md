# Setup Instructions

Open Android Studio

Select Checkout from version control --> select GIT and paste this URL and clone directory:

URL: https://markus.teach.cs.toronto.edu/git/csc207-2018-09-reg/group_0493

Select yes when you are prompted to create an android studio project for the sources

You will then be prompted to Import Project. Select the option to import project from
external model.

There will be 2 options, Android Gradle and Gradle --> Select gradle and press next

Select the "Use local gradle distribution", and set your Gradle Home: to your gradle directory
An example of the directory if you're having trouble finding it may be:
 \Program files\Android\Android Studio\gradle\gradle-4.6

Now, go to file --> Close project

You should now be back to the Android studio home page. Select Open an existing Android studio
project

Open up group_493 -> Phase 1 -> build.gradle

Create new virtual device: Pixel 2, Oreo API 27 Android 8.1

You can now run the app by clicking Run -> Run 'app'