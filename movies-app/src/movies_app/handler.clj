(ns movies-app.handler
  (:require [compojure.core :refer [defroutes routes]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [hiccup.middleware :refer [wrap-base-url]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [movies-app.routes.login :refer [login-routes]]
            [movies-app.routes.home :refer [home-routes]]
             [movies-app.routes.movie :refer [movie-routes]] 
             [movies-app.routes.reviews :refer [reviews-routes]] 
             [movies-app.routes.favorites :refer [favorites-routes]]
            [noir.util.middleware :as noir-middleware] ))
 
(defn init []
  (println "Movies app is starting"))

(defn destroy []
  (println "Movies app is shutting down"))

(defroutes app-routes
  (route/resources "/")
  (route/not-found "Sorry, that page does not exist."))

(def app
(noir-middleware/app-handler 
    [login-routes home-routes movie-routes reviews-routes favorites-routes  app-routes ]))
  