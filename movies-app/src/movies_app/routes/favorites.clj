(ns movies-app.routes.favorites
  (:require [compojure.core :refer :all]
            [movies-app.views.layout :as layout]
            [movies-app.routes.movie :as details]
            [movies-app.models.database :as db]
            [movies-app.util.extract :as util]
            [hiccup.element :refer :all]
            [hiccup.core :refer :all]
            [clojure.string :as cs]
            [noir.session :as session]
            [hiccup.form :refer :all]))


(defn display-favorites [username]
  (layout/common
    [:h2 "Your favorite movies"]
    (for [movie (db/find-favorites username)]
      
      (let [about (util/get-details-movie (:movie movie))]
        (layout/common
          [:h2 (:title about)]
          [:img {:src (cs/replace (:detailed (:posters about)) "tmb" "det")}]        
          [:p (:synopsis about)]
          (form-to [:delete (str "/movie&" (:movie movie) "&" username)]
                   (submit-button "Delete")
                   )
          )
        
        )
      )
    )
  )


(defn favorites[username movie]
  (db/add-favorite-movie username movie)
  (display-favorites username)
  )

(defn delete-movie [id username]
  (db/delete-movie username id)
  (display-favorites username id)
  )

(defn get-favorites []
  (let [user (str (session/get :username))]
    
      (display-favorites user)
          
    )
  )

(defroutes favorites-routes  
  (POST "/add-to-favorites" [username movie] (favorites username movie))
  (GET "/favorites"[] (get-favorites))
  (DELETE "/movie&:id&:username" [id username] (delete-movie id username))
  )