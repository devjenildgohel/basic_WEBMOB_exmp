# Basic Random User Data APP with LoadMore()

This is the source code for an Android app that displays a list of users, loaded from an API. The app uses Retrofit for network requests and Room for database storage. When the app starts, it loads the first page of users from the API and shows them in a RecyclerView. If the device is not connected to the internet, the app tries to load users from the local database. As the user scrolls down the RecyclerView, the app loads more pages of users from the API (if connected to the internet) or from the local database. The visibility of a progress bar is managed to show the loading state of the data.


## Screenshots

![Data Via API](https://www.linkpicture.com/q/WhatsApp-Image-2023-02-02-at-04.59.27.jpg)

![Load More](https://www.linkpicture.com/q/WhatsApp-Image-2023-02-02-at-04.59.2.jpg)


