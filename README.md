# Fitness App CPRE388




BitFitX App


Description:
This application will track a user’s fitness. A user will have the option to input modes of exercise such as running, swimming, bicycling, lifting weights, etc. and track how many calories they have lost. The user will be able to set goals on how many pounds they wish to lose and see how on track they are per week or per month. There will also be a social media aspect to the app in which users can add other users as friends and see the progress of their friends in their fitness goals. Uploading images of weight loss or videos of workouts can be uploaded on a user’s profile.

Functionality for the app:
1. User sign in to an account using username and password
2. User can track multiple forms of exercises by starting a timer and logging to the app. 
3. User can manually log exercises to the app.
4. Can view other people’s activity.
5. Share photos of workouts
6. Ability to navigate between multiple views.
7. Ability to view content using any device.
8. Supports both portrait and landscape mode.
9. Ability to delete or modify the workout.
10. Use modern Android gestures and animations. 
11. (Bonus) User can view their progress over a certain amount of time

Features with use cases:
1. ViewModel
    - Recyclerviews
2. ~~Fragments~~
    - ~~Content on screen~~ We are using activities
3. Threading (AsyncTask, Service, WorkManager, AlarmManager, DownloadManager...)
    - ~~Downloading user data~~ Timer stopwatch
4. Persistent storage via files or SharedPreferences
    - ~~Recent Activities/Workouts~~ Used to store email-id
5. SQLite
    - Realm Java implementation
6. Gestures
    - Lists, navigation, and layouts.
7. Animations
    - Transition between views
8. ~~Camera~~
    - ~~Adding photos to profile~~
9. Internet connectivity
    - Communication with the backend (e.g. Firebase)




