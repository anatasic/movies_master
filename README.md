# Tomatoer - Application for movie recommendation

Tomatoer is simple, but useful application for movie recommendation. This application collects data about movies based on the search terms that user enters. Except search Tomatoer provides to its users other interesting functionalities.User can mark movie as favorite and can access the list of its favorites movies, delete some movies or add new ones. 

Based on the search term, Tomatoer displays corresponding list of movies. User can select movie in order to see different movie details - such as movie title, actors, sinopsys, genres etc. Ratings are something that can be interesting for the users - this application shows the end user two types of rating, audience and critics. User can also see reviews for every movie he is interested in. As its name says, the applications also displays five similar movies of the movie which is currently selected.

In order to get information such as movie details, similar movies and movie ratings this application uses [Rotten Tomatoes API](http://developer.rottentomatoes.com/docs/read/Home).

Tomatoer also gives an option to its users to create new account or update it at some point of time. When the application is run for the first time, user has to register (create its own account).

There are also authorization rules that do not allow users to access the pages before they login.

## Prerequisites

This application uses NoSQL database **mongoDB**. It can be downloaded on the following [link](http://www.mongodb.org). Once it is downloaded it can be started by running this command *../bin/mongodb.exe*. The application should make connection with localhost after that. 

You are also supposed to download and install [Leiningen](http://leiningen.org) in order to run this application.  It enables its user to create, bulid, test and deploy projects.

## Libraries used

### [Ring](https://github.com/ring-clojure/ring) 

This library, similarly to Compojure provides the native Clojure API for working with servlets. Ring acts as a wrapper around Java servlet and allows web applications to be constructed of modular components that can be shared among a variety of applications, web servers, and web frameworks.

[ring "1.3.0"]

### [Compojure](https://github.com/weavejester/compojure)

Compojure is used to map request-handler functions to specific URLs. This is routing library for Ring.

[compojure "1.1.8"]

### [Hiccup](https://github.com/weavejester/hiccup) 

Hiccup is library that is used for representing HTML in Clojure.

### [Lib Noir](https://github.com/noir-clojure/lib-noir)

Lib Noir presents set of utillities and helpers for handling common operations that can be found in web application. It involves features such as sessions, cookies, input validations etc.

[lib-noir "0.8.4"]

## References

[Practical Clojure](http://www.amazon.com/Practical-Clojure-Experts-Voice-Source-ebook/dp/B003VM7G3S)

Useful book for learning functional programming and basic functions in Clojure.

[Clojure Programming](http://www.amazon.com/Clojure-Programming-Chas-Emerick/dp/1449394701/ref=pd_sim_b_1?ie=UTF8&refRID=0KCSHHVCSA3Z3YCX6JAF)

This books helps to learn Clojure in depth in efficient way - comparing it with the languages that are more popular and well-known.

[Web Development with Clojure](http://www.amazon.com/Web-Development-Clojure-Build-Bulletproof/dp/1937785645/ref=pd_sim_b_3?ie=UTF8&refRID=0KCSHHVCSA3Z3YCX6JAF)

This book shows you how to apply Clojure programming fundamentals to build real-world solutions.

## License

Distributed under the Eclipse Public License, the same as Clojure.
