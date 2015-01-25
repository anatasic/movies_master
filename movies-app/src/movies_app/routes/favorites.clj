(ns movies-app.routes.favorites
  (:require [compojure.core :refer :all]
            [movies-app.views.layout :as layout]
            [movies-app.util.extract :as util]
            [hiccup.element :refer :all]
            [hiccup.core :refer :all]
            [clojure.string :as cs]
            [hiccup.form :refer :all]))
  
  (defn favorites[]
    (layout/common
      [:h2 "Your favorite movies"]
      )
    )
  
  (defroutes favorites-routes
     
    (POST "/favorite" [username movie] (favorites))
    )