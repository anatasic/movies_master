# Tomatoer - Application for movie recommendation

Tomatoer is simple, but useful application for movie recommendation. This application collects data about movies based on the search terms that user enters. Except search Tomatoer provides to its users other interesting functionalities.User can mark movie as favorite and can access the list of its favorites movies, delete some movies or add new ones. 

Based on the search term, Tomatoer displays corresponding list of movies. User can select movie in order to see different movie details - such as movie title, actors, sinopsys, genres etc. Ratings are something that can be interesting for the users - this application shows the end user two types of rating, audience and critics. User can also see reviews for every movie he is interested in. As its name says, the applications also displays five similar movies of the movie which is currently selected.

Tomatoer also gives an option to its users to create new account or update it at some point of time. When the application is run for the first time, user has to register (create its own account).

There are also authorization rules that do not allow users to access the pages before they login.

## Prerequisites

This application uses NoSQL database **mongoDB**. It can be downloaded on the following [link](http://www.mongodb.org). Once it is downloaded it can be started by running this command *../bin/mongodb.exe*. The application should make connection with localhost after that. 

You are also supposed to download and install [Leiningen](http://leiningen.org) in order to run this application.  It enables its user to create, bulid, test and deploy projects.

## Libraries used
