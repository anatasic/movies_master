(ns movies-app.routes.favorites
  (:require [compojure.core :refer :all]
            [movies-app.views.layout :as layout]
            [movies-app.routes.movie :as details]
            [movies-app.models.database :as db]
            [movies-app.routes.home :as home]
            [movies-app.util.extract :as util]
            [hiccup.element :refer :all]
            [hiccup.core :refer :all]
            [clojure.string :as cs]
            [noir.session :as session]
            [noir.util.route :refer [restricted]]
            [noir.response :refer [redirect]]
            [hiccup.form :refer :all])
  
  )


(defn display-favorites [username]
  (let [movies (db/find-favorites username)]
    (layout/common    
      (home/logout)  
      [:h2.favTitle "Your favorite movies"]
      (if (= (count movies) 0)
        [:p "Wierd... You have no favorite movies."]
        )
      (for [movie (db/find-favorites username)]
        (let [about (util/get-details-movie (:movie movie))]
          (layout/common
            [:div.favorites
             [:h2 (:title about)]
             [:img {:src (cs/replace (:detailed (:posters about)) "tmb" "det")}]        
             [:p (:synopsis about)]
             (form-to [:delete (str "/movie&" (:movie movie) "&" username)]
                      [:input {:type "submit" :id "dltBtn" :value "Delete"} 
                       ]
                      ;          [:a {:href (str "/movie&" (:movie movie) "&" username)} "Delete"]
                      )
             ]
            )
          )
        )
      )
    )
  )
(defn movie-exists? [username movie]
  (> (count (db/movie-exists? username movie)) 0)
  )

(defn favorites[username movie]
  (if (true?(movie-exists? username movie))
;    (db/add-favorite-movie username movie)
;    (redirect "/favorites")
    (do
      
      (session/put! :movie-exists "Movie is already in your list of favorite movies.")
      
      (redirect (str "/movie&" movie))         
      )
    (if (false?(movie-exists? username movie))
      (do
       
        (db/add-favorite-movie username movie)
        (display-favorites username))
      )
    )
)
  
  (defn delete-movie [id username]
    (db/delete-movie username id)
    (display-favorites username)
    )
  
  (defn get-favorites []
    (let [user (str (session/get :username))]    
      (display-favorites user)    
      )
    )
  
  (defroutes favorites-routes  
    (POST "/add-to-favorites" [username movie] (restricted (favorites username movie)))
    (GET "/favorites"[] (restricted (get-favorites)))
    (DELETE "/movie&:id&:username" [id username] (restricted (delete-movie id username)))
    )